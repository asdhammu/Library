package com.library.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BookBorrowerPrimaryKey implements Serializable {

    private int cardId;

    private String isbn;

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}

