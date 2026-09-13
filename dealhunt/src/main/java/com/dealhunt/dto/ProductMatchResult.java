package com.dealhunt.dto;

public class ProductMatchResult {

    private boolean matched;
    private double score;
    private String reason;

    public ProductMatchResult() {
    }

    public ProductMatchResult(
            boolean matched,
            double score,
            String reason) {

        this.matched = matched;
        this.score = score;
        this.reason = reason;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}