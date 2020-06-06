package com.library.services;

import com.library.dto.Author;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.error.BorrowerExistsException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {

        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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

    public RestResponse addFine() {

        RestResponse response = new RestResponse();

        try {
            List<BookLoan> bookLoans = bookLoanRepository.findAllByDateInIsNull();
            Fine fine = null;
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
            response.setSuccess(true);
            response.setResult("Fine updated successfully");

        } catch (Exception e) {
            response.setError("Error while updating. Try Again");
            response.setSuccess(false);
        }

        return response;
    }

    @Override
    public RestResponse payFine(int cardId) {
        RestResponse response = new RestResponse();
        Borrower borrower = borrowerRepository.findByCardId(cardId).get();
        if (borrower == null) {
            throw new NoSuchBorrowerException("No such borrower exists");
        }

        List<BookLoan> bookLoans = borrower.getBookLoans();
        if (bookLoans.isEmpty()) {
            response.setError("This borrower has not loaned this book");
            response.setSuccess(false);
            return response;
        }

        for (BookLoan bookLoan : bookLoans) {
            bookLoan.getFine().setPaid(true);
            bookLoanRepository.save(bookLoan);
        }

        response.setResult("Fine paid successfully");
        response.setSuccess(true);
        return response;
    }

    @Override
    public RestResponse checkInBook(CheckInBook book) {

        RestResponse response = new RestResponse();

        try {
            bookRepository.findById(book.getIsbn()).get();
            BookLoan bookLoan = bookLoanRepository.findByBorrowerAndBook(borrowerRepository.findByCardId(book.getCardId()).get(),
                    bookRepository.findById(book.getIsbn()).get()).get(0);
            bookLoan.setDateIn(LocalDateTime.now());
            Book book1 = bookRepository.findById(book.getIsbn()).get();
            book1.setAvailable(true);
            bookRepository.save(book1);
            bookLoanRepository.save(bookLoan);

            response.setSuccess(true);
            response.setResult("Book Checked In Successfully");
        } catch (Exception exception) {

            response.setSuccess(false);
            response.setError("Error Occurred. Try after some time");
        }

        return response;

    }

    public List<SearchResult> searchCheckedInBooks(CheckInBook book) {

        if (book.getIsbn().length() > 0 && book.getIsbn().length() == 13) {
            SearchQuery query = new SearchQuery();
            query.setQuery(book.getIsbn());
            List<SearchResult> result = new ArrayList<>();

//            for (SearchResult result2 : this.search(query)) {
//                if (!result2.isAvailable()) {
//                    result.add(result2);
//                }
//            }
            return result;
        }

        String name = book.getName();
        int cardId = book.getCardId();

        List<Borrower> borrower = new ArrayList<>();

        if (name.length() > 0 && cardId != 0) {
            /*queryString = "from Borrower where cardId=" + cardId + " or bName like '%" + name
                    + "%'";
*/
            borrower.addAll(borrowerRepository.findByCardIdOrBNameIgnoreCaseContaining(cardId, name));

        } else if (name.length() > 0) {
            //queryString = "from Borrower where bName like '%" + name + "%'";
            borrower.addAll(borrowerRepository.findByBNameIgnoreCaseContaining(name));
        } else if (name.length() == 0) {
            //queryString = "from Borrower where cardId=" + cardId;

            borrower.add(borrowerRepository.findByCardId(cardId).get());
        }
        List<SearchResult> list = new ArrayList<>();
        for (Borrower borrower2 : borrower) {

            List<BookLoan> bookLoans = borrower2.getBookLoans();

            for (BookLoan bookLoan : bookLoans) {
                SearchResult result = new SearchResult();
                if (bookLoan.getDateIn() == null) {
                    Book book1 = bookRepository.findById(bookLoan.getBook().getIsbn()).get();

                    result.setCover(book1.getCover());
                    result.setISBN(book1.getIsbn());
                    result.setTitle(book1.getTitle());
                    result.setBorrower(borrower2);
                    list.add(result);
                }

            }
        }

        return list;


    }

    @Override
    public RestResponse addBookLoan(BookLoanRequest bookLoanRequest) {

        String isbn = bookLoanRequest.getIsbn();
        int borrowerId = bookLoanRequest.getBorrowerId();
        RestResponse response = new RestResponse();

        if (!borrowerRepository.findByCardId(borrowerId).isPresent()) {
            response.setError("Borrower not in the database");
            response.setSuccess(false);
            return response;
        }

        List<BookLoan> bookLoans = bookLoanRepository.findByBorrower(borrowerRepository.findByCardId(borrowerId).get()).stream().filter(x -> x.getDateIn() == null)
                .collect(Collectors.toList());
        if (bookLoans.size() > 2) {
            response.setError("3 Book have been issued to the borrower");
            response.setSuccess(false);
            return response;
        }

        Book book = bookRepository.findById(isbn).get();
        if (book == null || !book.isAvailable()) {
            response.setSuccess(false);
            response.setError("Book is not available. It's checked out");
            return response;
        }

        Borrower borrower = borrowerRepository.findByCardId(borrowerId).get();
        Book book1 = bookRepository.findById(isbn).get();
        BookLoan bookLoan = new BookLoan(borrower, book1);
        bookLoan.setDateOut(LocalDateTime.now());
        bookLoan.setDueDate(LocalDateTime.now().minusDays(-14));
        book.setAvailable(false);
        bookRepository.save(book);
        response.setResult("Added Successfully");
        response.setSuccess(true);

        return response;
    }

    /**
     * Searches book catalog and returns result with author details and
     * availability
     *//*
    public List<SearchResult> search(SearchQuery searchQuery) {

        List<SearchResult> result = new ArrayList<>();

        // regex for 13 digits i.e. ISBN number with 13 digits
        if (searchQuery.getQuery().matches("^(\\d{13})?$")) {

            Book book = bookRepository.findById(searchQuery.getQuery()).get();
            List<Book> books = new ArrayList<>();
            books.add(book);
            prepareSearchResult(result, books);
            return result;
        }

        StringBuffer bookNameQuery = new StringBuffer();
        StringBuffer authorQuery = new StringBuffer();

        StringBuilder capitalizeString = new StringBuilder();
        String[] str = searchQuery.getQuery().split(" ");
        for (int i = 0; i < str.length; i++) {
            capitalizeString.append(str[i].substring(0, 1).toUpperCase() + str[i].substring(1) + " ");
        }
        capitalizeString.deleteCharAt(capitalizeString.length() - 1);
        searchQuery.setQuery(capitalizeString.toString());

        extractNameUsingStanfordNLP(searchQuery.getQuery(), bookNameQuery, authorQuery);

*//*
        hqlQuery = "from Book b, author a, BookAuthor ba where a.author_id= ba.author.author_id and b.ISBN = ba.book.ISBN and b.title like '%"
                + bookNameQuery + "%' and a.name like '%" + authorQuery + "%'";
*//*

        // List<Book> books = bookRepository.findByTitleIgnoreCaseContaining(bookNameQuery.toString());

        prepareSearchResult(result, new ArrayList<>());

        return result;
    }
*/
    /**
     * Prepares the result
     *
     * @param result
     * @param list
     */
    private void prepareSearchResult(List<SearchResult> result, List<Book> list) {

        Map<String, SearchResult> isbn = new HashMap<>();
        for (Book b : list) {

            if (isbn.containsKey(b.getIsbn())) {

                SearchResult searchResult = isbn.get(b.getIsbn());
                searchResult.setAuthor(b.getAuthors());
                isbn.put(b.getIsbn(), searchResult);

            } else {
                SearchResult result2 = new SearchResult();
                result2.setCover(b.getCover());
                result2.setAvailable(b.isAvailable());
                result2.setISBN(b.getIsbn());
                result2.setTitle(b.getTitle());
                result2.setPublisher(b.getPublisher());
                result2.setPages(b.getPages());
                result2.setAuthor(b.getAuthors());
                isbn.put(b.getIsbn(), result2);
            }
        }

        for (Map.Entry<String, SearchResult> map : isbn.entrySet()) {
            result.add(map.getValue());
        }
    }


    @Override
    public List<Fine> getFineForCardId(SearchQuery searchQuery) {
        boolean isPaid = Boolean.parseBoolean(searchQuery.getPaid());
        Borrower borrower = borrowerRepository.findByCardId(Integer.parseInt(searchQuery.getQuery())).get();
        List<BookLoan> bookLoans = borrower.getBookLoans();

        List<Fine> fines = new ArrayList<>();
        for (BookLoan bookLoan : bookLoans) {
            if (bookLoan.getFine() != null) {
                if (isPaid && bookLoan.getFine().isPaid()) {
                    fines.add(bookLoan.getFine());
                }
                if (!isPaid && !bookLoan.getFine().isPaid()) {
                    fines.add(bookLoan.getFine());
                }
            }
        }

        return fines;
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


    /**
     * Map books to books DTO
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
