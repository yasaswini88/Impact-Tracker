
package com.Impact_Tracker.Impact_Tracker.DTO;

public class WeeklySentimentStatsDto {
    private double negativePercentage;
    private double positivePercentage;

    public WeeklySentimentStatsDto() {}

    public WeeklySentimentStatsDto(double negativePercentage, double positivePercentage) {
        this.negativePercentage = negativePercentage;
        this.positivePercentage = positivePercentage;
    }

    public double getNegativePercentage() {
        return negativePercentage;
    }

    public void setNegativePercentage(double negativePercentage) {
        this.negativePercentage = negativePercentage;
    }

    public double getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(double positivePercentage) {
        this.positivePercentage = positivePercentage;
    }
}
