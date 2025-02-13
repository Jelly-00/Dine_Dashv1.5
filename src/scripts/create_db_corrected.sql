-- Drop tables in correct order
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS menus;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;

-- Create Users Table
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       email VARCHAR(100) UNIQUE,
                       password VARCHAR(255),
                       role VARCHAR(20) DEFAULT 'Customer',
                       restaurant_id INT DEFAULT NULL
);

-- Create Restaurants Table (No Foreign Keys)
CREATE TABLE restaurants (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             cuisine VARCHAR(255) NOT NULL,
                             image_url VARCHAR(255) DEFAULT 'images/default.jpg'
);

-- Create Menus Table (No Foreign Keys)
CREATE TABLE menus (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       restaurant_id INT NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       description TEXT
);

-- Create MenuItems Table (No Foreign Keys)
CREATE TABLE menu_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            menu_id INT NOT NULL,
                            name VARCHAR(100) NOT NULL,
                            price DECIMAL(5,2) NOT NULL,
                            type VARCHAR(10) NOT NULL,
                            description TEXT,
                            image_url VARCHAR(255),
                            serving_size VARCHAR(50) DEFAULT NULL,
                            is_alcoholic BOOLEAN DEFAULT NULL
);

-- Show all tables after creation
SHOW TABLES;
