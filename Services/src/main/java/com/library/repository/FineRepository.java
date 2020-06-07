package com.library.repository;

import com.library.entity.BookLoan;
import com.library.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Integer> , FineRepositoryCustom{

    Optional<Fine> findByBookLoan(BookLoan bookLoan);
}
