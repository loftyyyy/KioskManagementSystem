package org.example.softfun_funsoft.model;

import java.sql.Timestamp;

public class CardPayments {
    private int cardPaymentId;
    private int paymentId;
    private String cardType;
    private String cardHolderName;
    private String transactionId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and Setters
    public int getCardPaymentId() {
        return cardPaymentId;
    }

    public void setCardPaymentId(int cardPaymentId) {
        this.cardPaymentId = cardPaymentId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}