package com.library.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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

	/*@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "borrower_book", joinColumns = {@JoinColumn(name = "card_id")}, inverseJoinColumns = {@JoinColumn(name = "isbn")})
	private List<Book> books = new ArrayList<>();*/

	@OneToMany()
	private List<BookLoan> bookLoans = new ArrayList<>();

	public Borrower() {
		
	}


	public List<BookLoan> getBookLoans() {
		return bookLoans;
	}

	public void setBookLoans(List<BookLoan> bookLoans) {
		this.bookLoans = bookLoans;
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
	
}
