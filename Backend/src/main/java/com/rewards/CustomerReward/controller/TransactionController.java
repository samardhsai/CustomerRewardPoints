package com.rewards.CustomerReward.controller;

import com.rewards.CustomerReward.model.Transaction;
import com.rewards.CustomerReward.service.RewardPointsResponse;
import com.rewards.CustomerReward.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Endpoint to record a new transaction
    @PostMapping
    public ResponseEntity<String> recordTransaction(@RequestBody Transaction transaction) {
        try {
            // Validate and save the transaction
            transactionService.recordTransaction(transaction);
            return new ResponseEntity<>("Transaction recorded successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error recording transaction: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint to calculate reward points based on customerId
    @GetMapping("/rewardPoints/{customerId}")
    public ResponseEntity<RewardPointsResponse> calculateRewardPoints(@PathVariable Integer customerId) {
        try {
            // Calculate reward points for the  customer ID
            RewardPointsResponse rewardPoints = transactionService.calculateRewardPoints(customerId);
            return new ResponseEntity<>(rewardPoints, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //EndPoint to calculate reward Points based on month per customer
    @GetMapping("/rewardPoints/{customerId}/{month}")
    public ResponseEntity<RewardPointsResponse> calculateRewardPointsForMonth(@PathVariable Integer customerId,
                                                                              @PathVariable int month) {
        try {
            // Calculate reward points for the given customer ID and month
            RewardPointsResponse rewardPoints = transactionService.calculateRewardPointsForMonth(customerId, month);
            return new ResponseEntity<>(rewardPoints, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //EndPoint to Calculate Reward Points based on given Period
    @GetMapping("/rewardPointsByDuration/{months}")
    public ResponseEntity<Map<Integer, Map<String, BigDecimal>>> calculateRewardPointsForAllCustomers(
            @PathVariable int months
    ) {
        try {
            // Calculate reward points for all customers for the given period
            Map<Integer, Map<String, BigDecimal>> rewardPointsList = transactionService
                    .calculateRewardPointsForAllCustomers(months);
            return new ResponseEntity<>(rewardPointsList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
