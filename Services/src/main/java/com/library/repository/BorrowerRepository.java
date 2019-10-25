package com.library.repository;

import com.library.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {

    Optional<Borrower> findBySsn(String ssn);

    Optional<Borrower> findByCardId(int cardId);

    List<Borrower> findByCardIdOrBNameIgnoreCaseContaining(int cardId, String name);

    List<Borrower> findByBNameIgnoreCaseContaining(String name);

}
