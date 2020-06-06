package com.library.repository;

import com.library.entity.Fine;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class FineRepositoryCustomImpl implements FineRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Fine> findAllFinesWithSum() {

        // select b.card_id , sum(fine_amt) from fine f ,book_loan b where b.loan_id= f.loan_id and f.paid=0 group by b.card_id
//        List list = entityManager.createQuery("select b.borrower.cardId, sum(fineAmount) from Fine f, BookLoan b " +
//                "where b.fine.fineId = f.fineId and f.paid =0 group by b.borrower.cardId").getResultList();

        List list1 = entityManager.createNativeQuery("select b.card_id , sum(fine_amt) from fine f ,book_loan b " +
                "where b.book_loan_id= f.loan_id and f.paid=0 group by b.card_id").getResultList();

        System.out.println(list1);

        return null;
    }
}
