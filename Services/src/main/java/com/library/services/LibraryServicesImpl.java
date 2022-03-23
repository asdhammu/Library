package com.library.services;

import com.library.dto.Author;
import com.library.dto.Response;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.error.*;
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

import javax.swing.text.html.Option;
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
    @Override
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

    @Override
    public List<com.library.dto.Fine> getAllFines() {
        return fineRepository.findAllFinesWithSum();
    }

    @Override
    public Response calculateFines() {
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
                fine.setFineAmount(Double.toString(daysOverDue * 0.25));
                fineRepository.save(fine);
            }
        }

        return new Response("All fines calculated");
    }

    @Override
    public Response payFine(int cardId) {
        Borrower borrower = borrowerRepository.findByCardId(cardId).get();
        if (borrower == null) {
            throw new NoSuchBorrowerException("No such borrower exists");
        }

        List<BookLoan> bookLoans = borrower.getBookLoans();
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
        return new Response("Paid");
    }

    @Override
    public Response checkInBook(CheckInBook book) {
        bookRepository.findById(book.getIsbn()).get();
        BookLoan bookLoan = bookLoanRepository.findByBorrowerAndBook(borrowerRepository.findByCardId(book.getCardId()).get(),
                bookRepository.findById(book.getIsbn()).get()).get(0);
        bookLoan.setDateIn(LocalDateTime.now());
        Book book1 = bookRepository.findById(book.getIsbn()).get();
        book1.setAvailable(true);
        bookRepository.save(book1);
        bookLoanRepository.save(bookLoan);
        return new Response("Book Checked In");

    }

    @Override
    public Response addBookLoan(BookLoanRequest bookLoanRequest) {

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

        return new Response("Book checked out");
    }


    @Override
    public List<Fine> getFineForCardId(int cardId) {
        Borrower borrower = borrowerRepository.findByCardId(cardId).get();
        List<BookLoan> bookLoans = borrower.getBookLoans();
        return bookLoans.stream().map(x -> x.getFine()).collect(Collectors.toList());
    }

    

   

    

}
