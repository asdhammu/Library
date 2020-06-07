package com.library.dto;

public class Fine {

    int cardId;
    long totalFine;

    public Fine(int cardId, long totalFine){
        this.cardId = cardId;
        this.totalFine = totalFine;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public long getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(long totalFine) {
        this.totalFine = totalFine;
    }
}
