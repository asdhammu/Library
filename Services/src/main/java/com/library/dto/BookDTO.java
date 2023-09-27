package com.library.dto;

import java.util.List;

public class BookDTO {

    private String ISBN;

    private String title;

    private String cover;

    private String publisher;

    private String pages;

    private boolean available;

    private List<AuthorDTO> authorDTOS;

    private BorrowerDTO borrowerDTO;

    public BorrowerDTO getBorrower() {
        return borrowerDTO;
    }

    public void setBorrower(BorrowerDTO borrowerDTO) {
        this.borrowerDTO = borrowerDTO;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
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

    public List<AuthorDTO> getAuthors() {
        return authorDTOS;
    }

    public void setAuthors(List<AuthorDTO> authorDTOS) {
        this.authorDTOS = authorDTOS;
    }
}
