-- V1__init_schema.sql
-- Initial schema creation for mechtrack application

-- Create job table
CREATE TABLE job (
    id UUID PRIMARY KEY,
    customer_name VARCHAR(255),
    car_model VARCHAR(255),
    description TEXT,
    date DATE,
    income NUMERIC(10, 2)
);

-- Create part table
CREATE TABLE part (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    cost NUMERIC(10, 2),
    invoice_image_url VARCHAR(255),
    purchase_date DATE,
    job_id UUID REFERENCES job(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_job_customer_name ON job(customer_name);
CREATE INDEX idx_job_date ON job(date);
CREATE INDEX idx_job_car_model ON job(car_model);
CREATE INDEX idx_part_job_id ON part(job_id);
CREATE INDEX idx_part_name ON part(name);
CREATE INDEX idx_part_purchase_date ON part(purchase_date); 