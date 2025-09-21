-- V2__add_job_status_and_type.sql
-- Add status and type columns to job table

-- Add status column with default value WAITING
ALTER TABLE job ADD COLUMN status VARCHAR(20) DEFAULT 'WAITING' NOT NULL;

-- Add type column with default value GENERAL_MAINTENANCE  
ALTER TABLE job ADD COLUMN type VARCHAR(30) DEFAULT 'GENERAL_MAINTENANCE' NOT NULL;

-- Add constraints to ensure valid enum values
ALTER TABLE job ADD CONSTRAINT chk_job_status 
    CHECK (status IN ('WAITING', 'IN_PROGRESS', 'DONE'));

ALTER TABLE job ADD CONSTRAINT chk_job_type 
    CHECK (type IN ('OIL_CHANGE', 'BRAKE_SERVICE', 'TIRE_SERVICE', 
                    'ENGINE_TUNE_UP', 'BATTERY_SERVICE', 'TRANSMISSION_SERVICE',
                    'COOLING_SYSTEM', 'ELECTRICAL_REPAIR', 'SUSPENSION_REPAIR',
                    'EXHAUST_REPAIR', 'AIR_CONDITIONING', 'DIAGNOSTIC',
                    'GENERAL_MAINTENANCE', 'BODYWORK', 'OTHER'));

-- Create indexes for better query performance
CREATE INDEX idx_job_status ON job(status);
CREATE INDEX idx_job_type ON job(type);

-- Update existing jobs to have default values (already handled by DEFAULT clause)
-- All existing jobs will automatically get WAITING status and GENERAL_MAINTENANCE type
