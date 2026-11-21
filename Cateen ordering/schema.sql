-- ====================================================================
-- College Canteen Ordering and Sales Analysis System
-- Scenario 3: Database Schema (DDL)
-- Subject: 25ITT31 - Database Management Systems
-- Institution: Velalar College of Engineering and Technology
-- ====================================================================

-- Enable Foreign Key constraints in SQLite
PRAGMA foreign_keys = ON;

-- 1. Categories Table
CREATE TABLE IF NOT EXISTS categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- 2. Food Items Table (Menu & Real-time Stock Management)
CREATE TABLE IF NOT EXISTS food_items (
    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_name VARCHAR(100) NOT NULL UNIQUE,
    category_id INTEGER NOT NULL,
    price DECIMAL(8, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    is_available BOOLEAN NOT NULL DEFAULT 1 CHECK (is_available IN (0, 1)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT
);

-- 3. Customers Table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    register_number VARCHAR(20) UNIQUE,
    role VARCHAR(20) DEFAULT 'Student' CHECK (role IN ('Student', 'Faculty', 'Staff', 'Guest')),
    contact_number VARCHAR(15)
);

-- 4. Orders Table (Transaction Header)
CREATE TABLE IF NOT EXISTS orders (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    order_date DATE NOT NULL,
    order_time TIME NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.0 CHECK (tax_amount >= 0),
    discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.0 CHECK (discount_amount >= 0),
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    order_status VARCHAR(20) NOT NULL DEFAULT 'Completed' CHECK (order_status IN ('Pending', 'Completed', 'Cancelled')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT
);

-- 5. Order Items Table (Transaction Line Items - Resolves M:N Relationship)
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    unit_price DECIMAL(8, 2) NOT NULL CHECK (unit_price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    line_total DECIMAL(10, 2) NOT NULL CHECK (line_total >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES food_items(item_id) ON DELETE RESTRICT
);

-- 6. Payments Table (Transaction Settlement)
CREATE TABLE IF NOT EXISTS payments (
    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL UNIQUE,
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('Cash', 'UPI', 'Card', 'Student Wallet')),
    amount_paid DECIMAL(10, 2) NOT NULL CHECK (amount_paid >= 0),
    payment_status VARCHAR(20) NOT NULL DEFAULT 'Success' CHECK (payment_status IN ('Pending', 'Success', 'Failed', 'Refunded')),
    payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_food_category ON food_items(category_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_item ON order_items(item_id);

-- Analytics Views
CREATE VIEW IF NOT EXISTS v_daily_sales_summary AS
SELECT 
    order_date,
    COUNT(DISTINCT order_id) AS total_orders,
    SUM(total_amount) AS daily_revenue,
    AVG(total_amount) AS avg_order_value
FROM orders
WHERE order_status = 'Completed'
GROUP BY order_date;

CREATE VIEW IF NOT EXISTS v_item_popularity AS
SELECT 
    f.item_id,
    f.item_name,
    c.category_name,
    f.price,
    SUM(oi.quantity) AS total_quantity_sold,
    SUM(oi.line_total) AS total_revenue_generated
FROM order_items oi
JOIN food_items f ON oi.item_id = f.item_id
JOIN categories c ON f.category_id = c.category_id
JOIN orders o ON oi.order_id = o.order_id
WHERE o.order_status = 'Completed'
GROUP BY f.item_id, f.item_name, c.category_name, f.price;
