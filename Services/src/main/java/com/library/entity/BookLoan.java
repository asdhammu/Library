package com.library.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_loan")
public class BookLoan {

    @EmbeddedId
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "cardId", insertable = false, updatable = false),
            @JoinColumn(name = "isbn", insertable = false, updatable = false)
    })
    private BookBorrowerPrimaryKey bookBorrowerPrimaryKey;

    @Column(name = "date_out")
    private LocalDateTime dateOut;

    @Column(name = "date_in")
    private LocalDateTime dateIn;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @OneToOne(mappedBy="bookLoan")
    private Fine fine;

    public BookLoan() {
        // TODO Auto-generated constructor stub
    }

    public BookLoan(BookBorrowerPrimaryKey bookBorrowerPrimaryKey){
        this.bookBorrowerPrimaryKey = bookBorrowerPrimaryKey;
    }

    public BookBorrowerPrimaryKey getBookBorrowerPrimaryKey() {
        return bookBorrowerPrimaryKey;
    }

    public void setBookBorrowerPrimaryKey(BookBorrowerPrimaryKey bookBorrowerPrimaryKey) {
        this.bookBorrowerPrimaryKey = bookBorrowerPrimaryKey;
    }

    public LocalDateTime getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDateTime dateOut) {
        this.dateOut = dateOut;
    }

    public LocalDateTime getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDateTime dateIn) {
        this.dateIn = dateIn;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Fine getFine() {
        return fine;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
    }

}

