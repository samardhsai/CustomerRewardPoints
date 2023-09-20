package com.rewards.CustomerReward.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RewardPointsResponse {

    private Integer customerId;
    private BigDecimal totalRewardPoints;

    public RewardPointsResponse() {
    }

    public RewardPointsResponse(Integer customerId, BigDecimal totalRewardPoints) {
        this.customerId = customerId;
        this.totalRewardPoints = totalRewardPoints;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalRewardPoints() {
        return totalRewardPoints;
    }

    public void setTotalRewardPoints(BigDecimal totalRewardPoints) {
        this.totalRewardPoints = totalRewardPoints;
    }

    @Override
    public String toString() {
        return "RewardPointsResponse{" +
                "customerId=" + customerId +
                ", totalRewardPoints=" + totalRewardPoints +
                '}';
    }
}
