package com.library.repository;

import com.library.dto.FineDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


import java.util.List;

public class FineRepositoryCustomImpl implements FineRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FineDTO> findAllFinesWithSum() {

        Query query = entityManager.createNativeQuery("select b.card_id as cardId, sum(fine_amt) as totalFine from fine f ,book_loan b " +
                "where b.book_loan_id= f.loan_id and f.paid=0 group by b.card_id", "resultMapper");
        List<FineDTO> fineDTOS = query.getResultList();
        return fineDTOS;
    }




}



