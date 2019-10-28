package com.library.repository;

import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    List<BookLoan> findAllByDateInIsNull();

    /*List<BookLoan> findByBookBorrowerPrimaryKey_CardId(int cardId);*/

    List<BookLoan> findByBorrower(Borrower borrower);

    List<BookLoan> findByBorrowerAndBook(Borrower borrower, Book book);

    /*Optional<BookLoan> findByBookBorrowerPrimaryKey(BookBorrowerPrimaryKey bookBorrowerPrimaryKey);*/
}
