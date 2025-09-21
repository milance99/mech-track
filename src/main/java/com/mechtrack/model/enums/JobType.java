package com.mechtrack.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Job type enumeration for automotive workshop services")
public enum JobType {
    
    @Schema(description = "Oil and filter change service")
    OIL_CHANGE,
    
    @Schema(description = "Brake pads, discs, and fluid service")
    BRAKE_SERVICE,
    
    @Schema(description = "Tire mounting, balancing, and rotation")
    TIRE_SERVICE,
    
    @Schema(description = "Spark plugs, filters, and fluids maintenance")
    ENGINE_TUNE_UP,
    
    @Schema(description = "Battery testing, replacement, and charging")
    BATTERY_SERVICE,
    
    @Schema(description = "Transmission fluid change and repair")
    TRANSMISSION_SERVICE,
    
    @Schema(description = "Radiator, coolant, and hoses service")
    COOLING_SYSTEM,
    
    @Schema(description = "Wiring, lights, and alternator repair")
    ELECTRICAL_REPAIR,
    
    @Schema(description = "Shocks, struts, and springs repair")
    SUSPENSION_REPAIR,
    
    @Schema(description = "Muffler, pipes, and catalytic converter service")
    EXHAUST_REPAIR,
    
    @Schema(description = "Air conditioning repair and recharge")
    AIR_CONDITIONING,
    
    @Schema(description = "Computer diagnostics and troubleshooting")
    DIAGNOSTIC,
    
    @Schema(description = "Multi-point inspection and general service")
    GENERAL_MAINTENANCE,
    
    @Schema(description = "Dents, scratches, and painting work")
    BODYWORK,
    
    @Schema(description = "Other services not covered by specific categories")
    OTHER
}
