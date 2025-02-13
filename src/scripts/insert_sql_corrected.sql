

-- Insert Users
INSERT INTO users (username, first_name, last_name, email, password, role, restaurant_id) VALUES
('jd123','John', 'Doe', 'john@example.com', 'hashed_password', 'Staff', -1),
('as123','Alice', 'Smith', 'alice@example.com', 'hashed_password', 'Customer', -1),
('staff123','Owner', 'Pizza Place', 'owner@pizza.com', 'hashed_password', 'Restaurant',1);

-- Insert Restaurants
INSERT INTO restaurants (name, cuisine) VALUES
('Pizza Palace', 'Pizza'),
('Burger House', 'Grill');

-- Insert Menus
INSERT INTO menus (restaurant_id, name, description) VALUES 
(1, 'Pizza Menu', 'A variety of classic and gourmet pizzas'),
(2, 'Burger Menu', 'Delicious burgers with fresh ingredients');

-- Insert Menu Items (Food with serving_size)
INSERT INTO menu_items (menu_id, name, price, type, description, image_url, serving_size, is_alcoholic) VALUES 
(1, 'Margherita Pizza', 10.99, 'Food', 'Tomato, mozzarella, and basil', 'images/margherita.jpg', 'Large', NULL),
(2, 'Cheeseburger', 8.99, 'Food', 'Juicy burger with melted cheese', 'images/cheeseburger.jpg', 'Medium', NULL);

-- Insert Menu Items (Drinks with is_alcoholic)
INSERT INTO menu_items (menu_id, name, price, type, description, image_url, serving_size, is_alcoholic) VALUES 
(1, 'Coca Cola', 2.50, 'Drink', 'Chilled soft drink', 'images/coke.jpg', NULL, FALSE),
(2, 'Red Wine', 12.99, 'Drink', 'Rich and full-bodied red wine', 'images/wine.jpg', NULL, TRUE);

