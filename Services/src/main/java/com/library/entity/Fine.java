package com.library.entity;

import com.library.dto.FineDTO;
import jakarta.persistence.*;


@Entity
@Table(name = "fine")
@SqlResultSetMapping(
        name = "resultMapper",
        classes = {
                @ConstructorResult(targetClass = FineDTO.class,
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
    private float fineAmount;

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

    public float getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(float fineAmount) {
        this.fineAmount = fineAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}
