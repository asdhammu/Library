package com.library.services;

import com.library.dto.BorrowerDTO;
import com.library.dto.ResponseDTO;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.exception.*;
import com.library.repository.*;
import com.library.requests.BookLoanRequest;
import com.library.requests.CheckInBookRequest;
import com.library.requests.CreateBorrowerRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorrowerServiceImpl implements BorrowerService {

    final BorrowerRepository borrowerRepository;
    final BookLoanRepository bookLoanRepository;
    final FineRepository fineRepository;
    final BookRepository bookRepository;
    final AuthorRepository authorRepository;

    public BorrowerServiceImpl(BorrowerRepository borrowerRepository,
                               BookLoanRepository bookLoanRepository,
                               FineRepository fineRepository,
                               BookRepository bookRepository,
                               AuthorRepository authorRepository) {
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
}
