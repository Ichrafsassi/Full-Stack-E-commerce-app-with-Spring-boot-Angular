-- NERD'S TECH Database Setup Script for PostgreSQL
-- Run this script in your PostgreSQL client (pgAdmin, psql, etc.)

-- 1. Create the database
CREATE DATABASE nerdstech;

-- 2. Connect to the database
\c nerdstech;

-- Note: The tables will be created automatically by Spring Boot JPA
-- when you start the backend application.

-- Optional: If you want to verify the connection, you can run:
-- SELECT current_database();
