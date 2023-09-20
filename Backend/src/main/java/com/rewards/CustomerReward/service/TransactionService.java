package com.rewards.CustomerReward.service;

import com.rewards.CustomerReward.model.Transaction;
import com.rewards.CustomerReward.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Validate and save the transaction
    public void recordTransaction(Transaction transaction) {
        if (transactionIsValid(transaction)) {
            transactionRepository.save(transaction);
        } else {
            throw new IllegalArgumentException("Invalid transaction data");
        }
    }

    private boolean transactionIsValid(Transaction transaction) {
        return transaction.getTransactionAmount() != null;
    }

    // Calculate reward points for the given customer ID
    public RewardPointsResponse calculateRewardPoints(Integer customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);

        BigDecimal totalRewardPoints = BigDecimal.valueOf(0);
        for (Transaction transaction : transactions) {
            totalRewardPoints = totalRewardPoints.add(calculateRewardPointsForTransaction(transaction));
        }

        return new RewardPointsResponse(customerId, totalRewardPoints);
    }

    // Calculate reward points for the given customer ID and month
    public RewardPointsResponse calculateRewardPointsForMonth(Integer customerId, int month) {
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndMonth(customerId, month);

        BigDecimal totalRewardPoints = BigDecimal.valueOf(0);
        for (Transaction transaction : transactions) {
            totalRewardPoints = totalRewardPoints.add(calculateRewardPointsForTransaction(transaction));
        }

        return new RewardPointsResponse(customerId, totalRewardPoints);
    }


    //Calculate Reward Points per Transaction
    private BigDecimal calculateRewardPointsForTransaction(Transaction transaction) {
        BigDecimal amountSpent = transaction.getTransactionAmount();
        BigDecimal rewardPoints = BigDecimal.valueOf(0);

        // Calculate reward points for amounts over $100
        if (amountSpent.compareTo(new BigDecimal("100.00")) > 0) {
            BigDecimal amountOver100 = amountSpent.subtract(new BigDecimal("100.00"));
            BigDecimal pointsOver100 = amountOver100.multiply(new BigDecimal("2.00"));
            rewardPoints = rewardPoints.add(pointsOver100);
            amountSpent = transaction.getTransactionAmount();
        }

        // Calculate reward points for amounts over $50
        if (amountSpent.compareTo(new BigDecimal("50.00")) > 0) {
            BigDecimal amountOver50 = amountSpent.subtract(new BigDecimal("50.00"));
            BigDecimal pointsOver50 = amountOver50;
            rewardPoints = rewardPoints.add(pointsOver50);
        }

        return rewardPoints;
    }

    //Calculate Reward Points for All customers based on given period
    public Map<Integer, Map<String, BigDecimal>> calculateRewardPointsForAllCustomers(int months) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusMonths(months).plusMonths(1).withDayOfMonth(1);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, currentDate);

        // Group transactions by customer ID and then by month
        Map<Integer, Map<String, List<Transaction>>> transactionsByCustomerIdAndMonth = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId,
                        Collectors.groupingBy(transaction -> {
                            int monthValue = transaction.getTransactionDate().getMonthValue();
                            return Month.of(monthValue).name();
                        })));

        // Calculate reward points for each customer and month
        Map<Integer, Map<String, BigDecimal>> rewardPointsByCustomerAndMonth = new HashMap<>();

        for (Map.Entry<Integer, Map<String, List<Transaction>>> entry : transactionsByCustomerIdAndMonth.entrySet()) {
            int customerId = entry.getKey();
            Map<String, List<Transaction>> transactionsByMonth = entry.getValue();

            Map<String, BigDecimal> rewardPointsByMonth = new TreeMap<>(new MonthComparator());

            for (Map.Entry<String, List<Transaction>> monthEntry : transactionsByMonth.entrySet()) {
                String monthName = monthEntry.getKey();
                List<Transaction> customerTransactions = monthEntry.getValue();
                BigDecimal totalRewardPoints = calculateTotalRewardPoints(customerTransactions);
                rewardPointsByMonth.put(monthName, totalRewardPoints);
            }

            rewardPointsByCustomerAndMonth.put(customerId, rewardPointsByMonth);
        }

        return rewardPointsByCustomerAndMonth;
    }

    //To order the months correctly
    class MonthComparator implements Comparator<String> {
        private final Map<String, Integer> monthOrder = new HashMap<>();

        public MonthComparator() {
            for (int i = 0; i < Month.values().length; i++) {
                monthOrder.put(Month.values()[i].name(), i);
            }
        }

        @Override
        public int compare(String month1, String month2) {
            return Integer.compare(monthOrder.get(month1), monthOrder.get(month2));
        }
    }

    private BigDecimal calculateTotalRewardPoints(List<Transaction> transactions) {
        BigDecimal totalRewardPoints = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            BigDecimal transactionRewardPoints = calculateRewardPointsForTransaction(transaction);
            totalRewardPoints = totalRewardPoints.add(transactionRewardPoints);
        }

        return totalRewardPoints;
    }

}
