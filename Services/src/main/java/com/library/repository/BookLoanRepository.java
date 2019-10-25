package com.library.repository;

import com.library.entity.BookBorrowerPrimaryKey;
import com.library.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookLoanRepository extends JpaRepository<BookLoan, BookBorrowerPrimaryKey> {

    List<BookLoan> findAllByDateInIsNull();

    List<BookLoan> findByBookBorrowerPrimaryKey_CardId(int cardId);

    Optional<BookLoan> findByBookBorrowerPrimaryKey(BookBorrowerPrimaryKey bookBorrowerPrimaryKey);
}
