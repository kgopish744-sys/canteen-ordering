# Sample Inputs, Outputs, and Execution Evidence
## College Canteen Ordering and Sales Analysis System
**Course:** 25ITT32 - Java Programming & 25ITT31 - DBMS  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Scenario:** 3 (Register No.: 3, 33, 63, 93)  

---

## 1. Java Console Application Execution Sample (`CanteenApp.java`)

### 1.1 Sample Input: Placing a Multi-Item Order (FR 2, FR 3, FR 4)
```text
Enter your choice (1-7): 2

--- PLACE NEW ORDER ---
Enter Customer Name (e.g. Aravind / Dr. Priya): Aravind Swaminathan
Enter Customer Role (Student / Faculty / Staff / Guest) [Student]: Student
Enter Payment Method (UPI / Cash / Card / Student Wallet) [UPI]: UPI

Enter Food Item ID (or '0' to finish order): 1
Selected: Ghee Roast Dosa (Available Stock: 40, Price: Rs.45.00)
Enter Quantity to order: 2
Added 2 x Ghee Roast Dosa to cart. Total items in cart: 1
Add another item? (y/n) [n]: y

Enter Food Item ID (or '0' to finish order): 16
Selected: Filter Coffee (Available Stock: 80, Price: Rs.15.00)
Enter Quantity to order: 2
Added 2 x Filter Coffee to cart. Total items in cart: 2
Add another item? (y/n) [n]: n
```

### 1.2 Actual Generated Output (ASCII Invoice Receipt)
```text
*** ORDER PROCESSED SUCCESSFULLY! ***

========================================================
      VELALAR COLLEGE OF ENGINEERING & TECHNOLOGY       
                 CAMPUS CANTEEN INVOICE                 
               Thindal, Erode - 638 012                 
========================================================
Invoice No : VCET-CAN-136      Date: 2026-11-21
Customer   : Aravind Swaminathan  Role: Student
Payment Via: UPI                  Status: Completed
--------------------------------------------------------
#    Item Description          Qty   Price     Amount
--------------------------------------------------------
1    Ghee Roast Dosa             2   45.00      90.00
2    Filter Coffee               2   15.00      30.00
--------------------------------------------------------
Subtotal:                               Rs.     120.00
GST (5%):                               Rs.       6.00
========================================================
NET AMOUNT PAYABLE:                     Rs.     126.00
========================================================
           Thank you! Enjoy your fresh meal!            
         * Please collect your token at counter *       
========================================================
```

---

## 2. Stock Violation and Exception Trigger Sample (FR 6)

### 2.1 Sample Input: Ordering Beyond Available Stock
```text
Enter your choice (1-7): 2

--- PLACE NEW ORDER ---
Enter Customer Name: Naveen Kumar
Enter Customer Role: Student
Enter Payment Method: UPI

Enter Food Item ID (or '0' to finish order): 14
Selected: Paneer Roll (Available Stock: 25, Price: Rs.50.00)
Enter Quantity to order: 30

[ERROR] Insufficient stock! Requested: 30, Available: 25
```

### 2.2 Exception Handled:
```java
InsufficientStockException: Cannot complete order for 'Paneer Roll' (Item ID: 14). Requested: 30, Available Stock: 25
```
**Result:** The transaction is rejected, no stock is deducted, and data integrity is maintained.

---

## 3. Analytical SQL Query Execution Sample (`queries.sql`)

### 3.1 Query 3: Top 5 Most Popular Food Items
```sql
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
```

**Output Result Set:**
| item_id | item_name | category_name | total_units_sold | total_sales_value (Rs.) |
|---|---|---|---|---|
| 17 | Masala Chai | Beverages | 12 | 144.00 |
| 13 | Samosa (2 pcs) | Snacks & Quick Bites | 9 | 180.00 |
| 8 | Chicken Biryani | Lunch & Meals | 8 | 880.00 |
| 16 | Filter Coffee | Beverages | 8 | 120.00 |
| 1 | Ghee Roast Dosa | Breakfast | 7 | 315.00 |

---

## 4. REST API Endpoint Sample (`CanteenWebServer.java`)

### 4.1 Sample Request: `POST /api/order`
```json
{
  "customerName": "Dr. S. Priya",
  "customerRole": "Faculty",
  "paymentMethod": "Card",
  "items": [
    { "itemId": 6, "quantity": 2 },
    { "itemId": 18, "quantity": 2 }
  ]
}
```

### 4.2 Sample Response (`200 OK`):
```json
{
  "success": true,
  "orderId": 137,
  "totalAmount": 190.00,
  "receipt": "========================================================\n      VELALAR COLLEGE OF ENGINEERING & TECHNOLOGY       \n                 CAMPUS CANTEEN INVOICE                 \n..."
}
```
