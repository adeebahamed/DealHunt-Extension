package com.dealhunt.dto;

public class AiMatchResult {

    private boolean matched;
    private double confidence;
    private String reason;

    public AiMatchResult() {
    }

    public AiMatchResult(
            boolean matched,
            double confidence,
            String reason) {

        this.matched = matched;
        this.confidence = confidence;
        this.reason = reason;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}