package com.library.services;

import com.library.dto.*;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.error.*;
import com.library.nlp.*;
import com.library.repository.*;
import com.library.requests.BookLoanRequest;
import com.library.requests.CheckInBookRequest;
import com.library.requests.CreateBorrowerRequest;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibraryServicesImpl implements LibraryServices {

    private static final Logger LOGGER = LogManager.getLogger(LibraryServicesImpl.class);
    final
    BorrowerRepository borrowerRepository;

    final
    BookLoanRepository bookLoanRepository;

    final
    FineRepository fineRepository;

    final
    BookRepository bookRepository;

    final
    AuthorRepository authorRepository;

    NLPQuery nlpQuery;

    public LibraryServicesImpl(BorrowerRepository borrowerRepository,
                               BookLoanRepository bookLoanRepository,
                               FineRepository fineRepository,
                               BookRepository bookRepository,
                               AuthorRepository authorRepository) {
        nlpQuery = NLPFactory.getSource(NLPResource.STANFORD);
        this.borrowerRepository = borrowerRepository;
        this.bookLoanRepository = bookLoanRepository;
        this.fineRepository = fineRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }


    /**
     * Add Borrower
     */
    public BorrowerDTO addBorrower(CreateBorrowerRequest createBorrowerRequest) {

        if (borrowerRepository.findBySsn(createBorrowerRequest.getSsn()).isPresent()) {
            throw new BorrowerExistsException("Borrower exist with ssn number " + createBorrowerRequest.getSsn());
        }

        Borrower borrower = new Borrower();
        borrower.setbName(createBorrowerRequest.getName());
        borrower.setAddress(createBorrowerRequest.getAddress());
        borrower.setPhone(createBorrowerRequest.getPhone());
        borrower.setSsn(createBorrowerRequest.getSsn());

        Borrower persistedBorrower = borrowerRepository.save(borrower);
        BorrowerDTO borrowerDto = new BorrowerDTO();
        borrowerDto.setName(persistedBorrower.getbName());
        borrowerDto.setCardId(persistedBorrower.getCardId());
        borrowerDto.setAddress(persistedBorrower.getAddress());
        return borrowerDto;
    }

    public List<FineDTO> getAllFines() {
        return fineRepository.findAllFinesWithSum();
    }

    public ResponseDTO calculateFines() {
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
                long daysOverDue = Duration.between(bookLoan.getDueDate().toLocalDate().atStartOfDay(), date.toLocalDate().atStartOfDay()).toDays();
                fine.setFineAmount(daysOverDue * 0.25f);
                fineRepository.save(fine);
            }
        }

        return new ResponseDTO("All fines calculated");
    }

    @Override
    public ResponseDTO payFine(int cardId) {
        Optional<Borrower> borrowerOptional = borrowerRepository.findByCardId(cardId);
        if (borrowerOptional.isEmpty()) {
            throw new NoSuchBorrowerException("No such borrower exists");
        }

        List<BookLoan> bookLoans = borrowerOptional.get().getBookLoans();
        if (bookLoans.isEmpty()) {
            throw new NoSuchBookLoanException("No loans for this borrower");
        }

        for (BookLoan bookLoan : bookLoans) {
            Optional<Fine> fine = fineRepository.findByBookLoan(bookLoan);
            if (fine.isPresent()) {
                fine.get().setPaid(true);
                fineRepository.save(fine.get());
            }
        }
        return new ResponseDTO("Paid");
    }

    @Override
    public ResponseDTO checkInBook(CheckInBookRequest checkInBookRequest) {
        Optional<Book> bookOptional = bookRepository.findById(checkInBookRequest.getIsbn());
        if (bookOptional.isEmpty()) {
            throw new NoSuchBookException("book doesn't exists");
        }
        Optional<Borrower> borrowerOptional = borrowerRepository.findByCardId(checkInBookRequest.getCardId());
        if (borrowerOptional.isEmpty()) {
            throw new NoSuchBorrowerException("Borrower doesn't exists");
        }

        Book book = bookOptional.get();
        Borrower borrower = borrowerOptional.get();
        BookLoan bookLoan = bookLoanRepository.findByBorrowerAndBook(borrower, book).get(0);
        bookLoan.setDateIn(LocalDateTime.now());
        book.setAvailable(true);
        bookRepository.save(book);
        bookLoanRepository.save(bookLoan);
        return new ResponseDTO("Book Checked In");

    }

    @Override
    public ResponseDTO addBookLoan(BookLoanRequest bookLoanRequest) {

        String isbn = bookLoanRequest.getIsbn();
        int borrowerId = bookLoanRequest.getBorrowerId();

        Optional<Borrower> borrowerOptional = borrowerRepository.findByCardId(borrowerId);

        if (borrowerOptional.isEmpty()) {
            throw new NoSuchBorrowerException("No such borrower");
        }

        List<BookLoan> bookLoans = bookLoanRepository.findByBorrower(borrowerOptional.get()).stream().filter(x -> x.getDateIn() == null)
                .toList();
        if (bookLoans.size() > 2) {
            throw new BorrowerThresholdException("3 Book have been issued to the borrower");
        }

        Optional<Book> bookOptional = bookRepository.findById(isbn);
        if (bookOptional.isEmpty() || !bookOptional.get().isAvailable()) {
            throw new BookNotAvailableException("Book is not available. All books have been checked out");
        }

        Book book = bookOptional.get();
        Borrower borrower = borrowerOptional.get();
        Book book1 = bookOptional.get();
        BookLoan bookLoan = new BookLoan(borrower, book1);
        bookLoan.setDateOut(LocalDateTime.now());
        bookLoan.setDueDate(LocalDateTime.now().minusDays(-14));
        bookLoan.setBorrower(borrower);
        book.setAvailable(false);
        bookLoanRepository.save(bookLoan);
        bookRepository.save(book);
        return new ResponseDTO("Book checked out");
    }


    @Override
    public List<FineDTO> getFineForCardId(int cardId) {
        Optional<Borrower> borrowerOptional = borrowerRepository.findByCardId(cardId);

        if (borrowerOptional.isEmpty()) {
            return new ArrayList<>();
        }
        Borrower borrower = borrowerOptional.get();
        List<BookLoan> bookLoans = borrower.getBookLoans();
        return bookLoans.stream().map(x -> new FineDTO(x.getBorrower().getCardId(), x.getFine().getFineAmount())).collect(Collectors.toList());
    }

    @Override
    public BookPaginatedDTO searchBooks(String query, Pageable pageable) {

        if (query.matches("^(\\d{13})?$")) {
            Book book = bookRepository.findById(query).get();
            List<BookDTO> books = new ArrayList<>();
            books.add(mapBookToBookDTO(book, null));
            return new BookPaginatedDTO(new PaginatedDTO(1, pageable.getPageNumber(), pageable.getPageSize(), 1), books);
        }
        Feature feature = this.nlpQuery.getQuery(query);
        LOGGER.info("Feature is " + feature);
        if (feature.getFeatureType() == FeatureType.AUTHOR) {
            List<com.library.entity.Author> authors = authorRepository.findByNameIgnoreCaseContaining(feature.getQuery());
            Page<Book> bookList = bookRepository.findAllByAuthorsIn(authors, pageable);
            return mapBooksToBooksDto(bookList);
        } else {
            Page<Book> bookPage = bookRepository.findByTitleIgnoreCaseContaining(feature.getQuery(), pageable);
            return mapBooksToBooksDto(bookPage);
        }
    }

    @Override
    public BookPaginatedDTO searchBooksForBorrower(String borrowerName, int cardId, String isbn, Pageable pageable) {

        Optional<Borrower> borrower = borrowerRepository.findByCardId(cardId);
        if (borrower.isEmpty()) {
            throw new NoSuchBorrowerException("No borrower exists for this card Id");
        }

        // List<Book> bookList = borrower.get().getBookLoans().stream().filter(y -> y.getDateIn() == null).map(BookLoan::getBook).collect(Collectors.toList());

        Page<BookLoan> bookLoans = bookLoanRepository.findAllByBorrowerAndDateInIsNull(borrower.get(), pageable);
        return mapBookLoansToBookDTO(bookLoans);
    }


    /**
     * Map books to books DTO
     */
    private BookPaginatedDTO mapBooksToBooksDto(Page<Book> books) {
        List<BookDTO> bookDTOS = new ArrayList<>();
        books.getContent().forEach(bo -> {
            BookDTO bookDTO = mapBookToBookDTO(bo, null);
            bookDTOS.add(bookDTO);
        });

        PaginatedDTO paginatedDTO = new PaginatedDTO(books.getTotalPages(), books.getNumber(), books.getSize(), books.getTotalElements());
        return new BookPaginatedDTO(paginatedDTO, bookDTOS);
    }

    private BookPaginatedDTO mapBookLoansToBookDTO(Page<BookLoan> bookLoans) {

        List<BookDTO> books = new ArrayList<>();
        bookLoans.getContent().forEach(bookLoan -> {
            books.add(mapBookToBookDTO(bookLoan.getBook(), bookLoan.getBorrower()));
        });
        PaginatedDTO paginatedDTO = new PaginatedDTO(bookLoans.getTotalPages(), bookLoans.getNumber(), bookLoans.getSize(), bookLoans.getTotalElements());
        return new BookPaginatedDTO(paginatedDTO, books);

    }

    private static BorrowerDTO mapBorrowerToBorrowerDTO(Borrower borrower) {
        BorrowerDTO borrowerDTO = new BorrowerDTO();
        borrowerDTO.setName(borrower.getbName());
        borrowerDTO.setCardId(borrower.getCardId());
        return borrowerDTO;
    }

    private static BookDTO mapBookToBookDTO(Book book, Borrower borrower) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setISBN(book.getIsbn());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setCover(book.getCover());
        bookDTO.setPages(book.getPages());
        bookDTO.setAvailable(book.isAvailable());
        List<AuthorDTO> authorDTOS = new ArrayList<>();
        book.getAuthors().forEach(y -> {
            AuthorDTO authorDTO1 = new AuthorDTO();
            authorDTO1.setName(y.getName());
            authorDTOS.add(authorDTO1);
        });
        bookDTO.setAuthors(authorDTOS);

        if (borrower != null) {
            bookDTO.setBorrower(mapBorrowerToBorrowerDTO(borrower));
        }
        return bookDTO;
    }

}
