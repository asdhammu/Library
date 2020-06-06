package com.library.repository;

import com.library.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Integer> {

    Optional<Borrower> findBySsn(String ssn);

    Optional<Borrower> findByCardId(int cardId);

    List<Borrower> findByCardIdOrBNameIgnoreCaseContaining(int cardId, String name);

    List<Borrower> findByBNameIgnoreCaseContaining(String name);

}
