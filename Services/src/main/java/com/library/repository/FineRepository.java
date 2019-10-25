package com.library.repository;

import com.library.entity.BookLoan;
import com.library.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, BookLoan> {
}
