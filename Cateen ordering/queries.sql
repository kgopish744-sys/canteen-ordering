-- ====================================================================
-- College Canteen Ordering and Sales Analysis System
-- Scenario 3: Analytical SQL Queries
-- Subject: 25ITT31 - Database Management Systems
-- ====================================================================

-- Query 1: Active Menu with Stock Levels and Category Names (JOIN)
SELECT 
    f.item_id,
    f.item_name,
    c.category_name,
    f.price,
    f.stock_quantity,
    CASE 
        WHEN f.stock_quantity = 0 THEN 'Out of Stock'
        WHEN f.stock_quantity < 15 THEN 'Low Stock'
        ELSE 'In Stock'
    END AS stock_status
FROM food_items f
JOIN categories c ON f.category_id = c.category_id
WHERE f.is_available = 1
ORDER BY c.category_name, f.price ASC;

-- Query 2: Daily Revenue and Order Count Report (Aggregation & Date Grouping)
SELECT 
    order_date,
    COUNT(order_id) AS total_orders,
    ROUND(SUM(subtotal), 2) AS gross_sales,
    ROUND(SUM(tax_amount), 2) AS tax_collected,
    ROUND(SUM(total_amount), 2) AS total_revenue
FROM orders
WHERE order_status = 'Completed'
GROUP BY order_date
ORDER BY order_date ASC;

-- Query 3: Top 5 Most Popular Food Items by Sales Volume (Multi-table JOIN & Aggregate)
SELECT 
    f.item_id,
    f.item_name,
    c.category_name,
    SUM(oi.quantity) AS total_units_sold,
    ROUND(SUM(oi.line_total), 2) AS total_sales_value
FROM order_items oi
JOIN food_items f ON oi.item_id = f.item_id
JOIN categories c ON f.category_id = c.category_id
JOIN orders o ON oi.order_id = o.order_id
WHERE o.order_status = 'Completed'
GROUP BY f.item_id, f.item_name, c.category_name
ORDER BY total_units_sold DESC
LIMIT 5;

-- Query 4: Category-wise Revenue Contribution and Percentage of Total Revenue
SELECT 
    c.category_name,
    COUNT(DISTINCT oi.order_id) AS orders_count,
    SUM(oi.quantity) AS total_items_sold,
    ROUND(SUM(oi.line_total), 2) AS category_revenue,
    ROUND((SUM(oi.line_total) * 100.0 / (SELECT SUM(line_total) FROM order_items)), 2) AS revenue_percentage
FROM order_items oi
JOIN food_items f ON oi.item_id = f.item_id
JOIN categories c ON f.category_id = c.category_id
GROUP BY c.category_id, c.category_name
ORDER BY category_revenue DESC;

-- Query 5: Customer Role Spending Analysis (Student vs Faculty vs Staff)
SELECT 
    c.role,
    COUNT(DISTINCT o.order_id) AS total_orders,
    ROUND(SUM(o.total_amount), 2) AS total_spent,
    ROUND(AVG(o.total_amount), 2) AS average_bill_amount
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
WHERE o.order_status = 'Completed'
GROUP BY c.role
ORDER BY total_spent DESC;

-- Query 6: Payment Method Breakdown
SELECT 
    p.payment_method,
    COUNT(p.payment_id) AS transaction_count,
    ROUND(SUM(p.amount_paid), 2) AS total_collected,
    ROUND((COUNT(p.payment_id) * 100.0 / (SELECT COUNT(*) FROM payments)), 2) AS percentage_share
FROM payments p
WHERE p.payment_status = 'Success'
GROUP BY p.payment_method
ORDER BY total_collected DESC;

-- Query 7: Identification of Low-Stock Items requiring replenishment (Stock Alert)
SELECT 
    item_id,
    item_name,
    price,
    stock_quantity
FROM food_items
WHERE stock_quantity <= 30 AND is_available = 1
ORDER BY stock_quantity ASC;

-- Query 8: High-Value Customer Orders (> Rs. 100) with Customer Details
SELECT 
    o.order_id,
    o.order_date,
    c.customer_name,
    c.role,
    o.total_amount,
    p.payment_method
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN payments p ON o.order_id = p.order_id
WHERE o.total_amount >= 100.00
ORDER BY o.total_amount DESC;
