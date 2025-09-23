-- V3__fix_job_status_and_type_columns.sql
-- Fix status and type columns that may have been partially created

-- Drop existing columns if they exist (in case they were created without proper constraints)
ALTER TABLE job DROP COLUMN IF EXISTS status;
ALTER TABLE job DROP COLUMN IF EXISTS type;

-- Add status column (nullable first)
ALTER TABLE job ADD COLUMN status VARCHAR(20);

-- Add type column (nullable first)
ALTER TABLE job ADD COLUMN type VARCHAR(30);

-- Update existing records with default values
UPDATE job SET status = 'WAITING' WHERE status IS NULL;
UPDATE job SET type = 'GENERAL_MAINTENANCE' WHERE type IS NULL;

-- Now add NOT NULL constraints
ALTER TABLE job ALTER COLUMN status SET NOT NULL;
ALTER TABLE job ALTER COLUMN type SET NOT NULL;

-- Set default values for future inserts
ALTER TABLE job ALTER COLUMN status SET DEFAULT 'WAITING';
ALTER TABLE job ALTER COLUMN type SET DEFAULT 'GENERAL_MAINTENANCE';

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
CREATE INDEX IF NOT EXISTS idx_job_status ON job(status);
CREATE INDEX IF NOT EXISTS idx_job_type ON job(type);
