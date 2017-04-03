package com.library.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name="fine")
public class Fine{

	
	@Id
	@GeneratedValue
	@Column(name="fine_id")
	private int fineId;
	
	@OneToOne
	@JoinColumn(name="loan_id")
	@JsonManagedReference
	private BookLoan bookLoan;
	
	@Column(name="fine_amt")
	private String fineAmount;
	
	@Column(name="paid")
	private boolean paid;
	
	public Fine() {
	}
	
	
	public Fine(BookLoan bookLoan){
		this.bookLoan = bookLoan;
	}
	
	
	public BookLoan getBookLoan() {
		return bookLoan;
	}
	public void setBookLoan(BookLoan bookLoan) {
		this.bookLoan = bookLoan;
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


	public int getFineId() {
		return fineId;
	}


	public void setFineId(int fineId) {
		this.fineId = fineId;
	}
}
