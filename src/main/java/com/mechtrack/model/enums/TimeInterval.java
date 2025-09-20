package com.mechtrack.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum representing different time intervals for analytics
 */
@Schema(description = "Time interval for analytics data")
public enum TimeInterval {
    
    @Schema(description = "Last 7 days")
    SEVEN_DAYS("7d", 7),
    
    @Schema(description = "Last 1 month (30 days)")
    ONE_MONTH("1m", 30),
    
    @Schema(description = "Last 3 months (90 days)")
    THREE_MONTHS("3m", 90);
    
    private final String code;
    private final int days;
    
    TimeInterval(String code, int days) {
        this.code = code;
        this.days = days;
    }
    
    public String getCode() {
        return code;
    }
    
    public int getDays() {
        return days;
    }
    
    /**
     * Get TimeInterval from string code
     * @param code The string code (7d, 1m, 3m)
     * @return The corresponding TimeInterval
     * @throws IllegalArgumentException if code is not valid
     */
    public static TimeInterval fromCode(String code) {
        for (TimeInterval interval : values()) {
            if (interval.code.equalsIgnoreCase(code)) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Invalid time interval code: " + code + 
            ". Valid codes are: 7d, 1m, 3m");
    }
    
    @Override
    public String toString() {
        return code;
    }
}
