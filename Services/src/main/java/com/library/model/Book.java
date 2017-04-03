package com.library.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="book")
public class Book {

	@Id
	@Column(name="isbn")
	private String ISBN;
	
	@Column(name="title")
	private String title;
	
	@Column(name="cover")
	private String cover;
	
	@Column(name="publisher")
	private String publisher;
	
	@Column(name="pages")
	private String pages;
	
	@Column(name="available")
	private boolean available;
	
	@OneToMany(mappedBy="book", cascade = CascadeType.ALL)
	@JsonBackReference
	private List<BookAuthor> author;
	
	@OneToMany(mappedBy="book", cascade = CascadeType.ALL)
	@JsonBackReference
	private List<BookLoan> bookLoan;
	
	public List<BookAuthor> getAuthor() {
		return author;
	}
	public void setAuthor(List<BookAuthor> author) {
		this.author = author;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public List<BookLoan> getBookLoan() {
		return bookLoan;
	}
	public void setBookLoan(List<BookLoan> bookLoan) {
		this.bookLoan = bookLoan;
	}
	
}
