# College Canteen Ordering and Sales Analysis System
### Scenario 3: Complete Project Implementation
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode - 638 012  
**Department:** Department of Information Technology  
**Academic Year:** 2026 - 2027 (ODD Semester) | **Class:** 25IT3A&B  
**Activity:** Project-Based Assessment  
**Candidate Register Numbers:** 3, 33, 63, 93  

---

## 1. Problem Statement & Objectives
A college canteen manually records food orders, bills, inventory, and daily sales. This makes it difficult to maintain accurate stock levels, identify popular food items, and understand variations in demand.

This project delivers an end-to-end automated **Canteen Ordering and Sales Analysis System** that:
1. Maintains food item names, prices, and stock quantities.
2. Accepts orders containing one or more food items.
3. Automatically calculates the total bill (subtotal, taxes, role-based discounts).
4. Updates stock quantities automatically upon order completion.
5. Stores transaction details and generates sales reports.
6. Strictly prevents orders that exceed available stock.
7. Retrieves sales data from the database and preprocesses it using Python.
8. Visualizes item-wise sales, daily revenue trends, and popular food items.

---

## 2. Integrated Curriculum Mapping

| Course Code | Course Title | Project Deliverables & Modules |
|---|---|---|
| **25ITT31** | Database Management Systems | Relational Schema (`schema.sql`), Seed Dataset (`seed_data.sql`), Analytical Queries (`queries.sql`), ER Diagram & Normalization (1NF to BCNF) (`DATABASE_DESIGN.md`). |
| **25ITT32** | Java Programming | OOP Architecture (`FoodItem.java`, `OrderItem.java`, `Order.java`, `Bill.java`), Custom Exception (`InsufficientStockException.java`), Service Layer (`CanteenService.java`), Runnable Console (`CanteenApp.java`), Microservice (`CanteenWebServer.java`). |
| **25ITT33** | Software Engineering | SRS Document (`SRS.md`), Design Diagrams (Use Case, Class, Sequence, Architecture in `SYSTEM_DESIGN.md`), Software Testing Plan & Test Cases Matrix (`TESTING.md`). |
| **25ITC31** | Foundations of Data Science | Python Analysis Pipeline (`data_analysis.py`), Dataset Preprocessing, Missing Value Handling, Feature Engineering, Matplotlib/Seaborn Visualizations (`chart_*.png`). |

---

## 3. Project Directory Structure
All project files are saved directly inside the `Canteen ordering` folder:

```text
Canteen ordering/
├── index.html                   # Modern responsive Web Portal
├── styles.css                   # Custom CSS styling & printable invoice format
├── app.js                       # Interactive frontend logic, Cart, Chart.js analytics
├── canteen_sales_data.csv       # Pre-exported canteen transactions dataset
├── data_analysis.py             # Python data science & visualization pipeline
├── schema.sql                   # Database DDL (Tables, Indexes, Views)
├── seed_data.sql                 # Sample dataset (Categories, Menu, Orders, Payments)
├── queries.sql                  # Analytical SQL queries (Joins, Aggregates, Group By)
├── FoodItem.java                # Java Model: Food item & stock management
├── OrderItem.java               # Java Model: Order line item composition
├── Order.java                   # Java Model: Order transaction header & auto-bill
├── Bill.java                    # Java Utility: Formatted ASCII tax invoice receipt
├── InsufficientStockException.java # Java Custom Exception for out-of-stock prevention
├── CanteenService.java          # Java Business Service: Menu, Cart, Stock validation
├── CanteenApp.java              # Standalone runnable Java console application
├── CanteenWebServer.java        # Built-in lightweight Java HTTP web server
├── SRS.md                       # IEEE Software Requirements Specification
├── DATABASE_DESIGN.md           # ER Model, Relational Schema, Normalization proof
├── SYSTEM_DESIGN.md             # Architecture, Use Case, Class, Sequence Diagrams
├── TESTING.md                   # Test strategy and 14 executed test cases
└── README.md                    # Project documentation & execution guide
```

---

## 4. How to Run the Project

### Option A: Launch Web Interface Directly (Instant)
Simply double-click `index.html` or open it with any web browser (Chrome, Edge, Firefox).  
- Browse the 20 delicious menu items with real-time stock badges.
- Add multiple food items to cart and test stock validation.
- Checkout with your name, roll number, and payment method (UPI / Cash / Card / Wallet).
- View and print the formatted college receipt.
- Explore the **Inventory & Stock** tab to restock or edit prices.
- Explore the **Sales Analytics** tab to see live dynamic Chart.js visualizations.

### Option B: Run Standalone Java Console Application
Open terminal or PowerShell in this folder and run:
```powershell
javac *.java
java CanteenApp
```
You can interactively navigate the 7 menu options to display the menu, place orders, restock inventory, and view sales summaries.

### Option C: Run Built-in Java Web Server
To run the full-stack system with Java HTTP microservice:
```powershell
javac *.java
java CanteenWebServer
```
Then open your browser at:
```
http://localhost:8080
```

### Option D: Run Python Data Science Analysis & Visualizations
To execute the data cleaning, preprocessing, descriptive statistics, and chart generation:
```powershell
python data_analysis.py
```
This will display the statistical report and generate four PNG charts in this folder:
1. `chart_item_wise_sales.png`
2. `chart_daily_revenue.png`
3. `chart_popular_food_items.png`
4. `chart_category_revenue.png`

### Option E: Run Database SQL Scripts
To create and query the database using SQLite:
```powershell
sqlite3 canteen_db.sqlite < schema.sql
sqlite3 canteen_db.sqlite < seed_data.sql
sqlite3 canteen_db.sqlite < queries.sql
```

---

## 5. Verification & Key Features Summary
- **FR 1 (Menu & Stock):** 20 preloaded campus dishes across Breakfast, Lunch, Snacks, and Beverages.
- **FR 2 (Multi-item Order):** Interactive cart supporting multi-dish ordering with dynamic stepper.
- **FR 3 (Auto Bill):** Automatic computation of line totals, subtotal, 5% GST, and faculty discount.
- **FR 4 (Stock Decrement):** Real-time stock decrement upon checkout.
- **FR 5 (Sales & Reports):** Complete transaction table, CSV export, and printable tax receipts.
- **FR 6 (Stock Prevention):** Robust check preventing orders exceeding available stock with custom exception.
- **FR 7 & 8 (Python Data Science):** End-to-end data pipeline for retrieval, preprocessing, and 4 graphical charts.
