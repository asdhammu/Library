package com.library.services;

import com.library.dto.FineDTO;
import com.library.dto.ResponseDTO;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.exception.NoSuchBookLoanException;
import com.library.exception.NoSuchBorrowerException;
import com.library.repository.BookLoanRepository;
import com.library.repository.BorrowerRepository;
import com.library.repository.FineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FineServiceImpl implements FineService {

    private final BorrowerRepository borrowerRepository;
    private final FineRepository fineRepository;
    private final BookLoanRepository bookLoanRepository;

    public FineServiceImpl(BorrowerRepository borrowerRepository, FineRepository fineRepository, BookLoanRepository bookLoanRepository) {
        this.borrowerRepository = borrowerRepository;
        this.fineRepository = fineRepository;
        this.bookLoanRepository = bookLoanRepository;
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
    public List<FineDTO> getFineForCardId(int cardId) {
        Optional<Borrower> borrowerOptional = borrowerRepository.findByCardId(cardId);

        if (borrowerOptional.isEmpty()) {
            return new ArrayList<>();
        }
        Borrower borrower = borrowerOptional.get();
        List<BookLoan> bookLoans = borrower.getBookLoans();
        return bookLoans.stream().map(x -> new FineDTO(x.getBorrower().getCardId(), x.getFine().getFineAmount())).collect(Collectors.toList());
    }
}
