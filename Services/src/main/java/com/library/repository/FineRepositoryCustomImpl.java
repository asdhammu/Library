package com.library.repository;

import com.library.dto.Fine;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class FineRepositoryCustomImpl implements FineRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Fine> findAllFinesWithSum() {

        Query query = entityManager.createNativeQuery("select b.card_id as cardId, sum(fine_amt) as totalFine from fine f ,book_loan b " +
                "where b.book_loan_id= f.loan_id and f.paid=0 group by b.card_id", "resultMapper");
        List<Fine> fines = query.getResultList();
        return fines;
    }




}



