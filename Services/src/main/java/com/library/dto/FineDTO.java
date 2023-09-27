package com.library.dto;

public class FineDTO {

    int cardId;
    float totalFine;

    public FineDTO(int cardId, float totalFine){
        this.cardId = cardId;
        this.totalFine = totalFine;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public float getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(float totalFine) {
        this.totalFine = totalFine;
    }
}
