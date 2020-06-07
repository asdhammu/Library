package com.library.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "cover")
    private String cover;

    @Column(name = "publisher")
    private String publisher;

    @NotNull
    @Column(name = "pages")
    private String pages;

    @Column(name = "available")
    private boolean available;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "books")
    List<Author> authors = new ArrayList<>();

    /*@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "borrowers")
    List<Borrower> borrowers = new ArrayList<>();*/

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookLoan> bookLoan = new ArrayList<>();

    public Book(){

    }

    public List<BookLoan> getBookLoan() {
        return bookLoan;
    }

    public void setBookLoan(List<BookLoan> bookLoan) {
        this.bookLoan = bookLoan;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /*public List<Borrower> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(List<Borrower> borrowers) {
        this.borrowers = borrowers;
    }*/

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String iSBN) {
        isbn = iSBN;
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

    @Override
    public String toString() {
        return "Book{" +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return available == book.available &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(title, book.title) &&
                Objects.equals(cover, book.cover) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(pages, book.pages) &&
                Objects.equals(authors, book.authors) &&
                Objects.equals(bookLoan, book.bookLoan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, cover, publisher, pages, available, authors, bookLoan);
    }
}
