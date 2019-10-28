package com.library.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_loan")
public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "book_loan_id")
    private Long bookLoanId;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Borrower borrower;

    @Column(name = "date_out")
    private LocalDateTime dateOut;

    @Column(name = "date_in")
    private LocalDateTime dateIn;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @OneToOne(mappedBy = "bookLoan")
    private Fine fine;

    public BookLoan() {
        // TODO Auto-generated constructor stub
    }

    public BookLoan(Borrower borrower, Book book) {
        this.borrower = borrower;
        this.book = book;
    }

    public Long getBookLoanId() {
        return bookLoanId;
    }

    public void setBookLoanId(Long bookLoanId) {
        this.bookLoanId = bookLoanId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
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

