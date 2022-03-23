package com.library.dto;

public class Borrower {
    private String borrower_Name;
    private int cardId;
    private String borrower_Address;

    public String getName() {
        return borrower_Name;
    }

    public void setName(String name) {
        this.borrower_Name = name;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getAddress() {
        return borrower_Address;
    }

    public void setAddress(String address) {
        this.borrower_Address = address;
    }
}
