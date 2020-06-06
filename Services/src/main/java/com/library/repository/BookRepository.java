package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findById(String isbn);

    Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
}
