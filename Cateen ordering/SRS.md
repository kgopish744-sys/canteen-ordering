# Software Requirements Specification (SRS)
## College Canteen Ordering and Sales Analysis System
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode - 12  
**Department:** Information Technology  
**Academic Year:** 2026 - 2027 (ODD Semester) | **Class:** 25IT3A&B  
**Course Code & Title:** 25ITT33 - Software Engineering  
**Activity:** Project-Based Assessment | **Scenario 3**  
**Candidate Register Numbers:** 3, 33, 63, 93  

---

## 1. Introduction

### 1.1 Purpose
This Software Requirements Specification (SRS) document defines the operational, functional, and non-functional requirements for the **College Canteen Ordering and Sales Analysis System**. The primary objective is to eliminate manual error-prone paper registers by automating order processing, dynamic bill generation, real-time inventory tracking, transaction persistence, and automated sales data analysis.

### 1.2 Scope of the System
The system covers the following operational modules:
- **Digital Menu & Ordering:** Real-time stock display, multi-item cart management, out-of-stock validation.
- **Automated Billing Engine:** Dynamic calculation of subtotal, applicable taxes (5% GST), faculty/staff discounts, and net bill.
- **Inventory & Stock Management:** Instant stock decrement upon completed orders, low-stock threshold alerts, replenishment controls.
- **Sales Transaction Database:** Relational schema storing orders, itemized line items, customer details, and payment settlements.
- **Python Data Science & Visualization Pipeline:** Automated retrieval of transaction history, data preprocessing, descriptive statistics, and graphical visualization of daily revenue and item popularity.

### 1.3 Definitions, Acronyms, and Abbreviations
- **VCET:** Velalar College of Engineering and Technology
- **DBMS:** Database Management System (25ITT31)
- **OOP:** Object-Oriented Programming (25ITT32)
- **EDA:** Exploratory Data Analysis (25ITC31)
- **FR:** Functional Requirement
- **NFR:** Non-Functional Requirement
- **GST:** Goods and Services Tax
- **UPI:** Unified Payments Interface

---

## 2. Overall Description

### 2.1 Product Perspective
The system operates as a unified full-stack campus solution. It consists of:
1. **Frontend Presentation Layer:** Modern, responsive HTML5/CSS3/JavaScript web interface.
2. **Application & Business Logic Layer:** Java application implementing OOP principles, custom exceptions (`InsufficientStockException`), and RESTful HTTP microservices (`CanteenWebServer`).
3. **Data Persistence Layer:** Relational SQL database schema adhering to 3NF/BCNF normalization principles.
4. **Analytical Data Science Layer:** Python-powered preprocessing, exploratory analysis, and visualization pipeline.

### 2.2 User Classes and Characteristics
- **Students:** Browse digital menu, add items to cart, place orders via UPI/Cash/Wallet, receive instant digital receipts.
- **Faculty / Staff:** Place subsidized orders with institution discount applied automatically.
- **Canteen Operator / Cashier:** Monitor orders, view low-stock alerts, restock items, update prices.
- **Canteen Administrator / Manager:** Review daily sales summaries, popular food item trends, and export sales CSV records.

### 2.3 Operating Environment
- **Operating System:** Windows 10/11, Linux, or macOS.
- **Java Runtime:** JDK 8 or higher (No third-party frameworks required; uses built-in Java HTTP server).
- **Web Browser:** Modern browsers (Chrome, Edge, Firefox, Safari).
- **Python Environment:** Python 3.8+ with `pandas`, `numpy`, `matplotlib`, and `seaborn`.

---

## 3. Specific Functional Requirements (Scenario 3)

### FR 1: Maintain Food Item Names, Prices, and Stock Quantities
- The system shall store and display a comprehensive menu with item ID, item name, food category, price, and current stock level.
- Canteen operators shall have administrative capabilities to restock quantities, adjust unit prices, and add new items.

### FR 2: Accept Orders Containing One or More Food Items
- The system shall allow users to select multiple food items in a single transaction with distinct quantities.
- Users shall be able to modify item quantities or remove items dynamically prior to final checkout.

### FR 3: Calculate the Total Bill Automatically
- The system shall compute the line total for each selected item (`Unit Price * Quantity`).
- The system shall automatically compute the order subtotal, apply a 5% GST tax, apply role-based discounts (10% for faculty/staff), and display the final net payable amount.

### FR 4: Update Stock Quantities When Orders Are Completed
- Upon successful order confirmation, the system shall decrement the stock count of each ordered food item atomically.
- If an item's stock reaches zero, its availability state shall automatically switch to "Out of Stock".

### FR 5: Store Transaction Details and Generate Sales Reports
- Every confirmed transaction shall be stored with a unique Order ID, date, time, customer details, itemized breakdown, and payment mode.
- The system shall provide daily sales reports, item sales volume rankings, and export capabilities to CSV.

### FR 6: Prevent Orders That Exceed Available Stock
- The system shall validate requested quantities against available stock in real time.
- If requested quantity exceeds stock, the order shall be blocked immediately, throwing an `InsufficientStockException` and alerting the user.

### FR 7: Retrieve Sales Data from Database and Preprocess Using Python
- The Python pipeline shall retrieve transaction records from the database/CSV dataset.
- The script shall inspect and handle missing values, eliminate duplicates, cast date and numeric datatypes, and engineer temporal features (day of week, order hour).

### FR 8: Visualize Item-Wise Sales, Daily Revenue, and Popular Food Items
- The Python script shall generate high-resolution analytical plots:
  - Item-wise sales volume bar chart.
  - Daily revenue trend line chart.
  - Top 5 popular food items ranking chart.
  - Category-wise revenue contribution donut chart.

---

## 4. Non-Functional Requirements (NFR)

- **Performance:** Order calculation and receipt generation shall execute in under 100 milliseconds.
- **Reliability & Data Integrity:** Stock updates and order insertions must occur atomically to prevent discrepancies.
- **Usability:** Intuitive responsive design supporting mobile, tablet, and desktop viewports.
- **Maintainability:** Modular software design adhering to standard MVC and DAO design patterns.
- **Portability:** Zero external dependency deployment for core web server and interactive client.
