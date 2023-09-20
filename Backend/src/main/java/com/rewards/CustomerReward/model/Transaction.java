package com.rewards.CustomerReward.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @Column(name ="transaction_id")
    private int transactionId;
    @Column(name ="customer_id")
    private Integer customerId;
    @Column(name ="transaction_date")
    private LocalDate transactionDate;
    @Column(name ="transaction_amount")
    private BigDecimal transactionAmount;

    public Transaction() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", customerId=" + customerId +
                ", transactionDate=" + transactionDate +
                ", transactionAmount=" + transactionAmount +
                '}';
    }
}
