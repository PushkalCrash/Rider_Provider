create database RP;
USE RP;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    aadhar_number VARCHAR(20) UNIQUE NOT NULL,
    role ENUM('Tenant', 'Owner') NOT NULL,
    profile_picture LONGBLOB
);

CREATE TABLE vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,          -- Unique identifier for the vehicle
    location VARCHAR(255) NOT NULL,             -- Vehicle location
    `condition` VARCHAR(50),                      -- Vehicle condition (Excellent, Good, Fair, Poor)
    insurance_available BOOLEAN,                -- Whether insurance is available
    model_year INT,                             -- Vehicle model year
    seats INT,                                  -- Number of seats
    vehicle_type VARCHAR(50),                   -- Type of vehicle (Car, Bike, SUV, Van)
    registration_number VARCHAR(50),            -- Vehicle registration number
    price_per_day DECIMAL(10, 2),               -- Price per day
    image_url VARCHAR(255),                     -- Path to the uploaded image (for preview)
    scheduled_visit DATE                        -- Scheduled visit date
);


CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    pickup_date DATE NOT NULL,
    return_date DATE NOT NULL,
    drivers_license VARCHAR(50) NOT NULL,
    card_number VARCHAR(20) NOT NULL,
    card_expiry VARCHAR(7) NOT NULL, -- Format: MM/YY
    booking_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);