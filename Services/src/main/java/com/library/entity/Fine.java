package com.library.entity;

import javax.persistence.*;


@Entity
@Table(name = "fine")
@SqlResultSetMapping(
        name = "resultMapper",
        classes = {
                @ConstructorResult(targetClass = com.library.dto.Fine.class,
                        columns = {
                                @ColumnResult(name = "cardId", type = Integer.class),
                                @ColumnResult(name = "totalFine", type = Long.class)
                        })
        })
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "fine_id")
    private int fineId;

    @OneToOne
    @JoinColumn(name = "loan_id")
    private BookLoan bookLoan;

    @Column(name = "fine_amt")
    private String fineAmount;

    @Column(name = "paid")
    private boolean paid;

    public Fine() {
    }

    public Fine(BookLoan bookLoan) {
        this.bookLoan = bookLoan;
    }

    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public String getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(String fineAmount) {
        this.fineAmount = fineAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}
