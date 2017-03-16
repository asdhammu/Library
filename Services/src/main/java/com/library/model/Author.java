package com.library.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name="author")
public class Author {

	
	
	@Id
	@GeneratedValue
	@Column(name="author_id")
	private int author_id;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	@JsonBackReference
	private List<BookAuthor> bookAuthor;
	
	@Column(name="name")
	private String name;
	
	public int getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BookAuthor> getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(List<BookAuthor> bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	
	
	
}
