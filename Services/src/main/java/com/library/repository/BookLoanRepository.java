package com.library.repository;

import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    List<BookLoan> findAllByDateInIsNull();

    List<BookLoan> findByBorrower(Borrower borrower);

    List<BookLoan> findByBorrowerAndBook(Borrower borrower, Book book);

    Page<BookLoan> findAllByBorrowerAndDateInIsNull(Borrower borrower, Pageable pageable);

}
