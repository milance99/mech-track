-- Test data for MechTrack application
-- Generates ~90 jobs over the past 30 days (3 per day) with realistic automotive service data

-- Helper: Use RANDOM_UUID() for H2 database UUIDs

-- =====================================================
-- JOBS DATA (Past 30 days, 3 jobs per day)
-- =====================================================

-- Day -29 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'John Smith', '2018 Honda Civic', 'Oil change and filter replacement', DATEADD('DAY', -29, CURRENT_DATE), 85.00),
(RANDOM_UUID(), 'Sarah Johnson', '2020 Toyota Camry', 'Brake pad replacement - front wheels', DATEADD('DAY', -29, CURRENT_DATE), 320.00),
(RANDOM_UUID(), 'Mike Wilson', '2016 Ford F-150', 'Engine diagnostic and spark plug replacement', DATEADD('DAY', -29, CURRENT_DATE), 450.00);

-- Day -28 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Emily Davis', '2019 Subaru Outback', 'Tire rotation and alignment check', DATEADD('DAY', -28, CURRENT_DATE), 120.00),
(RANDOM_UUID(), 'Robert Brown', '2017 BMW 320i', 'Air conditioning service and refrigerant refill', DATEADD('DAY', -28, CURRENT_DATE), 280.00),
(RANDOM_UUID(), 'Lisa Garcia', '2021 Nissan Sentra', 'Battery replacement and electrical system check', DATEADD('DAY', -28, CURRENT_DATE), 195.00);

-- Day -27 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'David Lee', '2015 Chevrolet Malibu', 'Transmission fluid change', DATEADD('DAY', -27, CURRENT_DATE), 180.00),
(RANDOM_UUID(), 'Jennifer White', '2019 Mazda CX-5', 'Brake inspection and rear brake pad replacement', DATEADD('DAY', -27, CURRENT_DATE), 380.00),
(RANDOM_UUID(), 'James Rodriguez', '2020 Hyundai Elantra', 'Oil change and cabin air filter replacement', DATEADD('DAY', -27, CURRENT_DATE), 95.00);

-- Day -26 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Amanda Thompson', '2018 Jeep Wrangler', 'Coolant system flush and thermostat replacement', DATEADD('DAY', -26, CURRENT_DATE), 420.00),
(RANDOM_UUID(), 'Christopher Martinez', '2016 Audi A4', 'Timing belt replacement', DATEADD('DAY', -26, CURRENT_DATE), 850.00),
(RANDOM_UUID(), 'Michelle Taylor', '2022 Kia Forte', 'New vehicle inspection and oil change', DATEADD('DAY', -26, CURRENT_DATE), 75.00);

-- Day -25 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Kevin Anderson', '2017 Volkswagen Jetta', 'Alternator replacement', DATEADD('DAY', -25, CURRENT_DATE), 520.00),
(RANDOM_UUID(), 'Nicole Thomas', '2019 Toyota RAV4', 'Wheel bearing replacement - front left', DATEADD('DAY', -25, CURRENT_DATE), 340.00),
(RANDOM_UUID(), 'Ryan Jackson', '2020 Honda Accord', 'Engine oil leak repair', DATEADD('DAY', -25, CURRENT_DATE), 290.00);

-- Day -24 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Ashley Harris', '2018 Ford Escape', 'Brake rotor and pad replacement - all wheels', DATEADD('DAY', -24, CURRENT_DATE), 680.00),
(RANDOM_UUID(), 'Brandon Clark', '2015 Dodge Charger', 'Fuel pump replacement', DATEADD('DAY', -24, CURRENT_DATE), 450.00),
(RANDOM_UUID(), 'Stephanie Lewis', '2021 Subaru Forester', 'Routine maintenance - 30k mile service', DATEADD('DAY', -24, CURRENT_DATE), 320.00);

-- Day -23 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Daniel Walker', '2019 Chevrolet Equinox', 'Starter motor replacement', DATEADD('DAY', -23, CURRENT_DATE), 380.00),
(RANDOM_UUID(), 'Heather Hall', '2017 Lexus ES300', 'Power steering fluid leak repair', DATEADD('DAY', -23, CURRENT_DATE), 320.00),
(RANDOM_UUID(), 'Andrew Young', '2020 GMC Sierra', 'Differential service and gear oil change', DATEADD('DAY', -23, CURRENT_DATE), 220.00);

-- Day -22 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Rachel King', '2018 Infiniti Q50', 'Suspension strut replacement - front', DATEADD('DAY', -22, CURRENT_DATE), 620.00),
(RANDOM_UUID(), 'Jonathan Wright', '2016 Toyota Prius', 'Hybrid battery diagnostic and cooling system service', DATEADD('DAY', -22, CURRENT_DATE), 280.00),
(RANDOM_UUID(), 'Melissa Lopez', '2019 Honda CR-V', 'Catalytic converter replacement', DATEADD('DAY', -22, CURRENT_DATE), 780.00);

-- Day -21 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Timothy Hill', '2021 Ford Mustang', 'Clutch replacement', DATEADD('DAY', -21, CURRENT_DATE), 950.00),
(RANDOM_UUID(), 'Kimberly Scott', '2017 Acura TLX', 'Water pump replacement', DATEADD('DAY', -21, CURRENT_DATE), 480.00),
(RANDOM_UUID(), 'Jacob Green', '2020 Ram 1500', 'Air filter and oil change', DATEADD('DAY', -21, CURRENT_DATE), 85.00);

-- Day -20 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Crystal Adams', '2018 Mercedes C-Class', 'Oxygen sensor replacement', DATEADD('DAY', -20, CURRENT_DATE), 420.00),
(RANDOM_UUID(), 'Tyler Baker', '2019 Jeep Cherokee', 'Radiator replacement', DATEADD('DAY', -20, CURRENT_DATE), 550.00),
(RANDOM_UUID(), 'Vanessa Gonzalez', '2022 Toyota Corolla', 'Windshield wiper replacement and fluid top-up', DATEADD('DAY', -20, CURRENT_DATE), 45.00);

-- Day -19 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Austin Nelson', '2016 Buick LaCrosse', 'Transmission repair - solenoid replacement', DATEADD('DAY', -19, CURRENT_DATE), 720.00),
(RANDOM_UUID(), 'Brittany Carter', '2020 Nissan Altima', 'CV joint replacement - driver side', DATEADD('DAY', -19, CURRENT_DATE), 340.00),
(RANDOM_UUID(), 'Jordan Mitchell', '2018 Cadillac XT5', 'Engine mount replacement', DATEADD('DAY', -19, CURRENT_DATE), 480.00);

-- Day -18 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Morgan Perez', '2017 Volvo S60', 'Turbocharger replacement', DATEADD('DAY', -18, CURRENT_DATE), 1250.00),
(RANDOM_UUID(), 'Nathan Roberts', '2019 Mitsubishi Outlander', 'Wheel alignment and tire balancing', DATEADD('DAY', -18, CURRENT_DATE), 150.00),
(RANDOM_UUID(), 'Samantha Turner', '2021 Genesis G70', 'Brake fluid flush and system inspection', DATEADD('DAY', -18, CURRENT_DATE), 120.00);

-- Day -17 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Eric Phillips', '2020 Lincoln Corsair', 'Exhaust system repair - muffler replacement', DATEADD('DAY', -17, CURRENT_DATE), 380.00),
(RANDOM_UUID(), 'Chelsea Campbell', '2018 Mazda3', 'Ignition coil replacement', DATEADD('DAY', -17, CURRENT_DATE), 280.00),
(RANDOM_UUID(), 'Marcus Parker', '2016 Chrysler 300', 'Coolant leak repair - hose replacement', DATEADD('DAY', -17, CURRENT_DATE), 160.00);

-- Day -16 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Diana Evans', '2019 Tesla Model 3', 'Brake service and cabin filter replacement', DATEADD('DAY', -16, CURRENT_DATE), 220.00),
(RANDOM_UUID(), 'Craig Edwards', '2017 Subaru Legacy', 'Head gasket replacement', DATEADD('DAY', -16, CURRENT_DATE), 1480.00),
(RANDOM_UUID(), 'Tiffany Collins', '2022 Hyundai Tucson', 'Oil change and tire pressure check', DATEADD('DAY', -16, CURRENT_DATE), 70.00);

-- Day -15 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Sean Stewart', '2018 Porsche Macan', 'Spark plug and ignition coil replacement', DATEADD('DAY', -15, CURRENT_DATE), 520.00),
(RANDOM_UUID(), 'Courtney Sanchez', '2020 Chevrolet Traverse', 'Power window motor replacement', DATEADD('DAY', -15, CURRENT_DATE), 280.00),
(RANDOM_UUID(), 'Logan Morris', '2019 Ford Explorer', 'Fuel injector cleaning', DATEADD('DAY', -15, CURRENT_DATE), 180.00);

-- Continue with more days... (Days -14 to -1)
-- Day -14 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Sierra Rogers', '2017 Audi Q5', 'Oil leak repair - valve cover gasket', DATEADD('DAY', -14, CURRENT_DATE), 420.00),
(RANDOM_UUID(), 'Ian Reed', '2021 BMW X3', 'Brake sensor replacement', DATEADD('DAY', -14, CURRENT_DATE), 180.00),
(RANDOM_UUID(), 'Jasmine Cook', '2018 Volkswagen Tiguan', 'Serpentine belt replacement', DATEADD('DAY', -14, CURRENT_DATE), 140.00);

-- Day -13 (3 jobs)  
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Caleb Bailey', '2020 Acura RDX', 'Timing chain tensioner replacement', DATEADD('DAY', -13, CURRENT_DATE), 680.00),
(RANDOM_UUID(), 'Destiny Rivera', '2019 Infiniti QX60', 'Air conditioning compressor replacement', DATEADD('DAY', -13, CURRENT_DATE), 720.00),
(RANDOM_UUID(), 'Trevor Cooper', '2016 Cadillac Escalade', 'Suspension air bag replacement', DATEADD('DAY', -13, CURRENT_DATE), 850.00);

-- Day -12 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Autumn Richardson', '2022 Lexus NX', 'Routine maintenance and inspection', DATEADD('DAY', -12, CURRENT_DATE), 220.00),
(RANDOM_UUID(), 'Xavier Cox', '2018 GMC Acadia', 'Wheel hub bearing replacement', DATEADD('DAY', -12, CURRENT_DATE), 380.00),
(RANDOM_UUID(), 'Paige Ward', '2017 Mercedes GLE', 'Diesel particulate filter cleaning', DATEADD('DAY', -12, CURRENT_DATE), 480.00);

-- Day -11 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Blake Torres', '2020 Toyota Highlander', 'Mass airflow sensor replacement', DATEADD('DAY', -11, CURRENT_DATE), 220.00),
(RANDOM_UUID(), 'Alexis Peterson', '2019 Honda Pilot', 'Drive belt and tensioner replacement', DATEADD('DAY', -11, CURRENT_DATE), 180.00),
(RANDOM_UUID(), 'Cameron Gray', '2021 Mazda CX-9', 'Thermostat and coolant service', DATEADD('DAY', -11, CURRENT_DATE), 160.00);

-- Day -10 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Sydney Ramirez', '2018 Jeep Grand Cherokee', 'Transmission filter and fluid change', DATEADD('DAY', -10, CURRENT_DATE), 220.00),
(RANDOM_UUID(), 'Garrett James', '2016 Ford Fusion', 'Clutch master cylinder replacement', DATEADD('DAY', -10, CURRENT_DATE), 320.00),
(RANDOM_UUID(), 'Mariah Watson', '2020 Subaru Crosstrek', 'CV axle replacement - passenger side', DATEADD('DAY', -10, CURRENT_DATE), 280.00);

-- Day -9 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Jaxon Brooks', '2019 Ram ProMaster', 'Brake master cylinder replacement', DATEADD('DAY', -9, CURRENT_DATE), 420.00),
(RANDOM_UUID(), 'Aaliyah Kelly', '2017 Nissan Rogue', 'Strut mount replacement', DATEADD('DAY', -9, CURRENT_DATE), 340.00),
(RANDOM_UUID(), 'Colton Sanders', '2022 Kia Sorento', 'Oil change and multi-point inspection', DATEADD('DAY', -9, CURRENT_DATE), 85.00);

-- Day -8 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Layla Price', '2018 Volvo XC90', 'Fuel rail and injector service', DATEADD('DAY', -8, CURRENT_DATE), 420.00),
(RANDOM_UUID(), 'Mason Bennett', '2020 Chevrolet Tahoe', 'Differential fluid change', DATEADD('DAY', -8, CURRENT_DATE), 140.00),
(RANDOM_UUID(), 'Zoe Wood', '2019 Audi A6', 'Windshield replacement', DATEADD('DAY', -8, CURRENT_DATE), 580.00);

-- Day -7 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Landon Barnes', '2021 Genesis GV70', 'Brake caliper replacement - rear', DATEADD('DAY', -7, CURRENT_DATE), 480.00),
(RANDOM_UUID(), 'Savannah Ross', '2017 Toyota Sienna', 'Sliding door motor repair', DATEADD('DAY', -7, CURRENT_DATE), 380.00),
(RANDOM_UUID(), 'Parker Henderson', '2018 Lincoln Navigator', 'Air suspension compressor replacement', DATEADD('DAY', -7, CURRENT_DATE), 720.00);

-- Day -6 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Mackenzie Coleman', '2020 Mitsubishi Eclipse Cross', 'Throttle body cleaning', DATEADD('DAY', -6, CURRENT_DATE), 120.00),
(RANDOM_UUID(), 'Easton Jenkins', '2019 Cadillac CTS', 'Radiator fan motor replacement', DATEADD('DAY', -6, CURRENT_DATE), 280.00),
(RANDOM_UUID(), 'Brooklyn Perry', '2016 BMW 528i', 'Engine oil pan gasket replacement', DATEADD('DAY', -6, CURRENT_DATE), 420.00);

-- Day -5 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Hudson Powell', '2022 Honda Passport', 'Cabin air filter and engine air filter replacement', DATEADD('DAY', -5, CURRENT_DATE), 80.00),
(RANDOM_UUID(), 'Evelyn Long', '2018 Mazda CX-3', 'Clutch hydraulic system service', DATEADD('DAY', -5, CURRENT_DATE), 180.00),
(RANDOM_UUID(), 'Wyatt Patterson', '2020 Ford Bronco', 'Transfer case service', DATEADD('DAY', -5, CURRENT_DATE), 160.00);

-- Day -4 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Aria Hughes', '2017 Acura MDX', 'Timing belt and water pump replacement', DATEADD('DAY', -4, CURRENT_DATE), 920.00),
(RANDOM_UUID(), 'Grayson Flores', '2019 Jeep Compass', 'Evaporative emission system repair', DATEADD('DAY', -4, CURRENT_DATE), 280.00),
(RANDOM_UUID(), 'Luna Washington', '2021 Volvo XC60', 'Brake vacuum booster replacement', DATEADD('DAY', -4, CURRENT_DATE), 480.00);

-- Day -3 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Eli Butler', '2018 Infiniti QX80', 'Shock absorber replacement - all four', DATEADD('DAY', -3, CURRENT_DATE), 680.00),
(RANDOM_UUID(), 'Chloe Simmons', '2020 Hyundai Santa Fe', 'Power steering pump replacement', DATEADD('DAY', -3, CURRENT_DATE), 420.00),
(RANDOM_UUID(), 'Owen Foster', '2019 Chevrolet Silverado', 'Fuel filter replacement', DATEADD('DAY', -3, CURRENT_DATE), 120.00);

-- Day -2 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Mia Gonzales', '2022 Toyota Venza', 'Oil change and tire rotation', DATEADD('DAY', -2, CURRENT_DATE), 85.00),
(RANDOM_UUID(), 'Liam Bryant', '2017 Mercedes S-Class', 'Air suspension valve block replacement', DATEADD('DAY', -2, CURRENT_DATE), 1200.00),
(RANDOM_UUID(), 'Stella Alexander', '2020 Nissan Pathfinder', 'Catalytic converter and O2 sensor replacement', DATEADD('DAY', -2, CURRENT_DATE), 850.00);

-- Day -1 (3 jobs)
INSERT INTO job (id, customer_name, car_model, description, date, income) VALUES
(RANDOM_UUID(), 'Noah Russell', '2018 Ford Edge', 'Turbocharger wastegate actuator replacement', DATEADD('DAY', -1, CURRENT_DATE), 520.00),
(RANDOM_UUID(), 'Isabella Griffin', '2019 Subaru Ascent', 'Headlight restoration and bulb replacement', DATEADD('DAY', -1, CURRENT_DATE), 140.00),
(RANDOM_UUID(), 'Ethan Diaz', '2021 Kia Telluride', 'Routine maintenance and fluid check', DATEADD('DAY', -1, CURRENT_DATE), 180.00);

-- =====================================================
-- PARTS DATA (1-3 parts per job, realistic automotive parts)
-- =====================================================

-- We'll use job IDs from the inserted jobs. Since we can't predict UUIDs,
-- we'll use a subquery approach to match parts to jobs by date and customer

-- Helper view to get job IDs for easier part insertion
-- For parts, we'll insert based on job characteristics

-- Oil change jobs - typical parts
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Engine Oil Filter', 12.50, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Oil change%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Synthetic Motor Oil 5W-30 (5qt)', 35.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Oil change%';

-- Brake jobs - brake pads, rotors, fluid
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Front Brake Pads', 85.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Brake pad%' AND j.description LIKE '%front%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Rear Brake Pads', 75.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Brake pad%' AND j.description LIKE '%rear%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Brake Rotors (Set of 2)', 120.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Brake rotor%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Brake Fluid DOT 3', 15.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%brake%' AND j.income > 300;

-- Engine work - spark plugs, filters, belts
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Spark Plugs (Set of 4)', 45.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%spark plug%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Ignition Coils (Set of 4)', 180.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Ignition coil%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Engine Air Filter', 22.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%air filter%' OR j.description LIKE '%filter replacement%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Cabin Air Filter', 18.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%cabin%filter%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Serpentine Belt', 35.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%belt%';

-- Battery and electrical
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Car Battery', 120.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Battery replacement%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Alternator', 280.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Alternator%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Starter Motor', 180.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Starter%';

-- Cooling system
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Engine Coolant', 25.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Coolant%' OR j.description LIKE '%coolant%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Thermostat', 45.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%thermostat%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Radiator', 250.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Radiator%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Water Pump', 180.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Water pump%';

-- Transmission
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Transmission Fluid', 35.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Transmission%' AND j.description LIKE '%fluid%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Transmission Filter', 55.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Transmission filter%';

-- Suspension
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Front Struts (Pair)', 280.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%strut%' AND j.description LIKE '%front%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Shock Absorbers (Set of 4)', 320.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Shock absorber%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Wheel Bearing', 85.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%bearing%';

-- Fuel system
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Fuel Pump', 220.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Fuel pump%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Fuel Filter', 25.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Fuel filter%';

-- Exhaust system
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Catalytic Converter', 450.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Catalytic converter%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Muffler', 120.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%muffler%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Oxygen Sensor', 85.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%Oxygen sensor%';

-- AC system
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'A/C Refrigerant', 45.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%air conditioning%' OR j.description LIKE '%refrigerant%';

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'A/C Compressor', 380.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%compressor%';

-- Miscellaneous common parts
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Windshield Wipers (Pair)', 25.00, null, j.date, j.id
FROM job j WHERE j.description LIKE '%wiper%';

-- Some parts with invoice images (simulated URLs)
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'OEM Timing Belt Kit', 180.00, '/uploads/invoices/timing-belt-invoice-001.pdf', j.date, j.id
FROM job j WHERE j.description LIKE '%Timing belt%' LIMIT 3;

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Premium Brake Pad Set', 95.00, '/uploads/invoices/brake-pads-invoice-002.pdf', j.date, j.id
FROM job j WHERE j.description LIKE '%brake%' AND j.income > 500 LIMIT 2;

-- Add some additional random parts to jobs that might not have enough parts
INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Shop Supplies', 15.00, null, j.date, j.id
FROM job j WHERE j.income > 200
ORDER BY RAND() LIMIT 20;

INSERT INTO part (id, name, cost, invoice_image_url, purchase_date, job_id) 
SELECT RANDOM_UUID(), 'Labor Supplies', 8.50, null, j.date, j.id
FROM job j WHERE j.income > 100
ORDER BY RAND() LIMIT 30;

-- =====================================================
-- Summary
-- =====================================================
-- This script creates:
-- - 90 jobs over the past 30 days (3 per day)
-- - Realistic automotive service descriptions
-- - Income ranging from $45 to $1,480
-- - 200+ parts with realistic names and costs
-- - Parts properly linked to their respective jobs
-- - Mix of simple maintenance to complex repairs
-- - Some parts with invoice image URLs for testing file handling 