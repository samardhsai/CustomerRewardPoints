package com.rewards.CustomerReward.repository;

import com.rewards.CustomerReward.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("select transaction from Transaction transaction where transaction.customerId=:customerId")
    List<Transaction> findByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT t FROM Transaction t WHERE t.customerId = :customerId AND MONTH(t.transactionDate) = :month")
    List<Transaction> findByCustomerIdAndMonth(@Param("customerId") Integer customerId, @Param("month") int month);

    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
}
