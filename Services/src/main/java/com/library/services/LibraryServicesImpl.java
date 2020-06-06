package com.library.services;

import com.library.dto.Author;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.error.BookNotAvailableException;
import com.library.error.BorrowerExistsException;
import com.library.error.BorrowerThresholdException;
import com.library.error.NoSuchBorrowerException;
import com.library.modal.*;
import com.library.nlp.*;
import com.library.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibraryServicesImpl implements LibraryServices {

    private static final Logger LOGGER = LogManager.getLogger(LibraryServicesImpl.class);

    @Autowired
    BorrowerRepository borrowerRepository;

    @Autowired
    BookLoanRepository bookLoanRepository;

    @Autowired
    FineRepository fineRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    NLPQuery nlpQuery;

    public LibraryServicesImpl() {
        nlpQuery = NLPFactory.getSource(NLPResource.STANFORD);
    }


    /**
     * Add Borrower
     */
    public com.library.dto.Borrower addBorrower(Borrower borrower) {

        if (borrowerRepository.findBySsn(borrower.getSsn()).isPresent()) {
            throw new BorrowerExistsException("Borrower exist with ssn number " + borrower.getSsn());
        }

        Borrower borrower1 = borrowerRepository.save(borrower);

        com.library.dto.Borrower borrowerDto = new com.library.dto.Borrower();
        borrowerDto.setName(borrower1.getbName());
        borrowerDto.setCardId(borrower1.getCardId());
        borrowerDto.setAddress(borrower1.getAddress());
        return borrowerDto;
    }

    public List<FineResponse> getAllFines() {

        List<Fine> fines = fineRepository.findAllFinesWithSum();
//        Session session = this.sessionFactory.openSession();
//        StringBuilder builder = new StringBuilder();
//
//        builder.append("select b.card_id , sum(fine_amt) from fine f ,book_loan b where b.loan_id= f.loan_id and f.paid=0 group by b.card_id");
//
//        Query query = session.createSQLQuery(builder.toString());


//        List<Object[]> object = query.list();
//
//        List<FineResponse> fineResponses = new ArrayList<>();
//
//        for (Object[] object2 : object) {
//            FineResponse fineResponse = new FineResponse();
//            fineResponse.setCardId((Integer) object2[0]);
//            fineResponse.setAmount(Double.toString((Double) object2[1]));
//
//            fineResponses.add(fineResponse);
//        }

        // session.close();


        return new ArrayList<>();
    }

    public String calculateFines() {
        List<BookLoan> bookLoans = bookLoanRepository.findAllByDateInIsNull();
        Fine fine;
        LocalDateTime date = LocalDateTime.now();
        for (BookLoan bookLoan : bookLoans) {
            if (bookLoan.getDueDate().isBefore(date)) {

                if (bookLoan.getFine() == null) {
                    fine = new Fine(bookLoan);
                } else {
                    fine = bookLoan.getFine();
                }
                long daysOverDue = Duration.between(date.toLocalDate(), bookLoan.getDueDate().toLocalDate()).toDays();
                fine.setFineAmount(Double.toString(daysOverDue * 0.25));
                fineRepository.save(fine);
            }
        }

        return "All fines calculated";
    }

    @Override
    public String payFine(int cardId) {
        Borrower borrower = borrowerRepository.findByCardId(cardId).get();
        if (borrower == null) {
            throw new NoSuchBorrowerException("No such borrower exists");
        }

        List<BookLoan> bookLoans = borrower.getBookLoans();
        if (bookLoans.isEmpty()) {
            throw new BorrowerThresholdException("Borrower has loaned three books.");
        }

        for (BookLoan bookLoan : bookLoans) {
            bookLoan.getFine().setPaid(true);
            bookLoanRepository.save(bookLoan);
        }
        return "Paid";
    }

    @Override
    public String checkInBook(CheckInBook book) {
        bookRepository.findById(book.getIsbn()).get();
        BookLoan bookLoan = bookLoanRepository.findByBorrowerAndBook(borrowerRepository.findByCardId(book.getCardId()).get(),
                bookRepository.findById(book.getIsbn()).get()).get(0);
        bookLoan.setDateIn(LocalDateTime.now());
        Book book1 = bookRepository.findById(book.getIsbn()).get();
        book1.setAvailable(true);
        bookRepository.save(book1);
        bookLoanRepository.save(bookLoan);
        return "Book Checked In`";

    }

    @Override
    public String addBookLoan(BookLoanRequest bookLoanRequest) {

        String isbn = bookLoanRequest.getIsbn();
        int borrowerId = bookLoanRequest.getBorrowerId();

        if (!borrowerRepository.findByCardId(borrowerId).isPresent()) {
            throw new NoSuchBorrowerException("No such borrower");
        }

        List<BookLoan> bookLoans = bookLoanRepository.findByBorrower(borrowerRepository.findByCardId(borrowerId).get()).stream().filter(x -> x.getDateIn() == null)
                .collect(Collectors.toList());
        if (bookLoans.size() > 2) {
            throw new BorrowerThresholdException("3 Book have been issued to the borrower");
        }

        Book book = bookRepository.findById(isbn).get();
        if (book == null || !book.isAvailable()) {
            throw new BookNotAvailableException("Book is not available. All books have been checked out");
        }

        Borrower borrower = borrowerRepository.findByCardId(borrowerId).get();
        Book book1 = bookRepository.findById(isbn).get();
        BookLoan bookLoan = new BookLoan(borrower, book1);
        bookLoan.setDateOut(LocalDateTime.now());
        bookLoan.setDueDate(LocalDateTime.now().minusDays(-14));
        bookLoan.setBorrower(borrower);
        book.setAvailable(false);
        bookLoanRepository.save(bookLoan);
        bookRepository.save(book);

        return "Book checked out";
    }


    @Override
    public List<Fine> getFineForCardId(int cardId) {
        Borrower borrower = borrowerRepository.findByCardId(cardId).get();
        List<BookLoan> bookLoans = borrower.getBookLoans();
        return bookLoans.stream().map(x -> x.getFine()).collect(Collectors.toList());
    }

    @Override
    public List<com.library.dto.Book> searchBooks(String query, int page, int size) {

        if (query.matches("^(\\d{13})?$")) {
            Book book = bookRepository.findById(query).get();
            List<Book> books = new ArrayList<>();
            books.add(book);
            return mapBooksToBooksDto(books);
        }
        Feature feature = this.nlpQuery.getQuery(query);
        LOGGER.info("Feature is " + feature);
        if (feature.getFeatureType() == FeatureType.AUTHOR) {
            List<com.library.entity.Author> authors = authorRepository.findByNameIgnoreCaseContaining(feature.getQuery());
            List<Book> bookList = authors.stream().flatMap(x -> x.getBooks().stream()).collect(Collectors.toList());
            return mapBooksToBooksDto(bookList);
        } else {
            if (size > 100) {
                size = 100;
            }
            return mapBooksToBooksDto(bookRepository.findByTitleIgnoreCaseContaining(feature.getQuery(), PageRequest.of(page, size)).getContent());
        }
    }

    @Override
    public List<com.library.dto.Book> searchBooksForBorrower(String borrowerName, int cardId, String isbn) {

        Optional<Borrower> borrower = borrowerRepository.findByCardId(cardId);
        if (!borrower.isPresent()) {
            throw new NoSuchBorrowerException("No borrower exists for this card Id");
        }

        List<Book> bookList = borrower.get().getBookLoans().stream().filter(y -> y.getDateIn() == null).map(x -> x.getBook()).collect(Collectors.toList());

        List<com.library.dto.Book> list =  mapBooksToBooksDto(bookList);
        list.stream().forEach( x -> {
            com.library.dto.Borrower borrower1 = new com.library.dto.Borrower();
            borrower1.setName(borrower.get().getbName());
            borrower1.setCardId(borrower.get().getCardId());
            x.setBorrower(borrower1);
        });
        return list;
    }


    /**
     * Map books to books DTO
     *
     * @param books
     * @return
     */
    private List<com.library.dto.Book> mapBooksToBooksDto(List<Book> books) {

        List<com.library.dto.Book> list = new ArrayList<>();

        books.forEach(x -> {

            com.library.dto.Book book = new com.library.dto.Book();
            book.setISBN(x.getIsbn());
            book.setTitle(x.getTitle());
            book.setCover(x.getCover());
            book.setPages(x.getPages());
            book.setAvailable(x.isAvailable());
            List<Author> author = new ArrayList<>();
            x.getAuthors().forEach(y -> {
                Author author1 = new Author();
                author1.setName(y.getName());
                author.add(author1);
            });

            book.setAuthor(author);
            list.add(book);
        });


        return list;

    }

}
