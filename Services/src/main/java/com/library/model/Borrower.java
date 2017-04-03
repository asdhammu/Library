package com.library.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="borrower")
public class Borrower {

	@Id
	@Column(name="card_id")
	private int cardId;
	
	@Column(name="ssn")
	private String ssn;
	
	@Column(name="bname")
	private String bName;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone")
	private String phone;

	@OneToMany(mappedBy="borrower")
	@JsonBackReference
	private List<BookLoan> bookLoans;
	
	public Borrower() {
		
	}
	
	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<BookLoan> getBookLoans() {
		return bookLoans;
	}

	public void setBookLoans(List<BookLoan> bookLoans) {
		this.bookLoans = bookLoans;
	}
	
	
	
}
