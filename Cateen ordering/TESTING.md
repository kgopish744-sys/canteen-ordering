# Software Testing Documentation
## College Canteen Ordering and Sales Analysis System
**Course:** 25ITT33 - Software Engineering (Unit 4: Software Testing)  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Scenario:** 3 (Register No.: 3, 33, 63, 93)  

---

## 1. Testing Strategy

The system underwent rigorous verification and validation encompassing:
- **Unit Testing:** Testing isolated classes, methods, bill calculations, and exception triggers (`InsufficientStockException`).
- **Integration Testing:** Verifying communication between UI components, Java backend services, and SQLite/CSV datasets.
- **System Testing:** End-to-end evaluation validating compliance with all 8 scenario functional requirements.
- **Boundary Value Analysis:** Testing stock decrement at boundary conditions (ordering exactly remaining stock vs. 1 above remaining stock).

---

## 2. Test Cases and Results Matrix

| Test ID | Test Scenario | Input Data | Expected Output | Actual Output | Status |
|---|---|---|---|---|---|
| **TC-01** | Display Menu with Stock Details (FR 1) | Application Startup / Menu Load | Full listing of 20 items with category, price, and stock levels. | Displayed all 20 items with real-time stock pills. | **PASS** |
| **TC-02** | Accept Multi-Item Order (FR 2) | Dosa (Qty: 2), Chai (Qty: 1) | Cart accepts both items and displays itemized table. | Both items added to cart with correct quantity counters. | **PASS** |
| **TC-03** | Auto-Bill Calculation (FR 3) | Dosa (Rs.45 x 2) = Rs.90.00 | Subtotal: Rs.90.00, GST (5%): Rs.4.50, Total: Rs.94.50 | Subtotal: Rs.90.00, GST: Rs.4.50, Grand Total: Rs.94.50 | **PASS** |
| **TC-04** | Role-based Discount Calculation (FR 3) | Faculty Order: Meals (Rs.70 x 2) = Rs.140 | 10% discount applied (-Rs.14.00), Tax: Rs.7.00, Net: Rs.133.00 | 10% discount applied correctly on bill summary. | **PASS** |
| **TC-05** | Stock Decrement on Order (FR 4) | Item #1 Initial Stock: 40, Order Qty: 2 | Order completed; New stock becomes 38. | Stock successfully reduced to 38 in memory and database. | **PASS** |
| **TC-06** | Out of Stock Prevention (FR 6) | Item #14 (Paneer Roll) Stock: 25; Order: 30 | System rejects order, throws `InsufficientStockException`. | Error banner: "Cannot complete order: Requested: 30, Available: 25". | **PASS** |
| **TC-07** | Boundary Stock Order (FR 4 & 6) | Stock: 10, Order Qty: 10 | Order accepted; Stock becomes 0; Item marked "Out of Stock". | Order placed; stock changed to 0; card shows "Out of Stock". | **PASS** |
| **TC-08** | Transaction Recording & Receipt (FR 5) | Place Order with UPI payment | Order stored in history; ASCII receipt rendered with unique invoice no. | Order stored with ID #136; printable modal receipt shown. | **PASS** |
| **TC-09** | Restock Inventory (FR 1 & 4) | Item #10: Restock +20 units | Stock increments from 35 to 55 units; status updated. | Stock incremented to 55; badge shows green "Sufficient". | **PASS** |
| **TC-10** | CSV Export of Transactions (FR 5) | Click "Export to CSV" | Downloads `.csv` file containing all transaction details. | File `canteen_sales_export_*.csv` generated and downloaded. | **PASS** |
| **TC-11** | Python Data Retrieval (FR 7) | Run `python data_analysis.py` | Successfully loads records from DB or `canteen_sales_data.csv`. | 45 rows loaded; data types verified. | **PASS** |
| **TC-12** | Python Preprocessing (FR 7) | Missing values & duplicate check | Missing values detected and cleaned; derived features added. | 0 nulls, derived `day_name` and `hour` features created. | **PASS** |
| **TC-13** | Python Visualizations (FR 8) | Generate analytical plots | 4 publication-quality PNG charts generated and saved. | All 4 charts created and saved in current folder. | **PASS** |
| **TC-14** | Interactive Chart.js Rendering (FR 8) | Switch to "Sales Analytics" tab | Canvas elements render daily revenue, popular items, category share. | Charts rendered smoothly with responsive animations. | **PASS** |

---

## 3. Test Summary and Conclusion
All 14 test cases executed with a **100% pass rate**. The system successfully satisfies all functional requirements specified in Scenario 3, with robust boundary handling, real-time stock integrity preservation, and comprehensive data visualization.
