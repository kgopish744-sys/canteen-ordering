# System Design and Architecture Documentation
## College Canteen Ordering and Sales Analysis System
**Course:** 25ITT33 - Software Engineering  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Scenario:** 3 (Register No.: 3, 33, 63, 93)  

---

## 1. Overall System Architecture

The system implements a modular 4-Tier Architecture separating presentation, business logic, persistence, and analytics:

```mermaid
flowchart TD
    subgraph Tier1["1. Presentation Tier (Frontend UI)"]
        UI1["Digital Menu & Live Stock"]
        UI2["Cart & Dynamic Bill Calculator"]
        UI3["Inventory & Restock Controls"]
        UI4["Transaction History & Receipt Modal"]
        UI5["Interactive Charts & Visualizations"]
    end

    subgraph Tier2["2. Business Logic Tier (Java Service)"]
        WS["CanteenWebServer (HTTP Microservice)"]
        CS["CanteenService (Business Engine)"]
        EX["InsufficientStockException"]
        BL["Bill & Tax Calculation Logic"]
    end

    subgraph Tier3["3. Persistence Tier (Relational Storage)"]
        SQL["Relational Database (SQLite / MySQL)"]
        CSV["Historical Sales Dataset (CSV)"]
    end

    subgraph Tier4["4. Analytics & Data Science Tier (Python)"]
        PY["data_analysis.py Pipeline"]
        PD["Data Cleaning & Preprocessing (Pandas)"]
        NP["Feature Engineering & Summary (NumPy)"]
        VIZ["Visualizations (Matplotlib & Seaborn)"]
    end

    Tier1 <-->|HTTP / REST JSON| Tier2
    Tier2 <-->|CRUD / SQL Queries| Tier3
    Tier3 -->|Data Extraction| Tier4
```

---

## 2. Use Case Diagram

```mermaid
flowchart LR
    Student((Student / Faculty))
    Staff((Canteen Cashier))
    Admin((Canteen Admin / Analyst))

    UC1(["Browse Menu & Check Stock"])
    UC2(["Add Items to Cart"])
    UC3(["Calculate Total Bill"])
    UC4(["Place Order & Select Payment"])
    UC5(["Receive Digital Invoice Receipt"])
    UC6(["Restock Items & Update Prices"])
    UC7(["Receive Low Stock Alerts"])
    UC8(["View Daily Sales Report"])
    UC9(["Execute Python Sales Analytics"])

    Student --> UC1
    Student --> UC2
    Student --> UC3
    Student --> UC4
    Student --> UC5

    Staff --> UC1
    Staff --> UC6
    Staff --> UC7
    Staff --> UC8

    Admin --> UC8
    Admin --> UC9
```

---

## 3. Class Diagram (Java OOP Structure)

```mermaid
classDiagram
    class FoodItem {
        -int itemId
        -String itemName
        -String category
        -double price
        -int stockQuantity
        -boolean isAvailable
        +getItemId() int
        +getItemName() String
        +getPrice() double
        +getStockQuantity() int
        +reduceStock(int quantity) void
        +addStock(int quantity) void
    }

    class OrderItem {
        -FoodItem foodItem
        -int quantity
        -double unitPrice
        -double lineTotal
        +getLineTotal() double
        +getQuantity() int
    }

    class Order {
        -int orderId
        -int customerId
        -String customerName
        -String customerRole
        -LocalDate orderDate
        -LocalTime orderTime
        -List~OrderItem~ items
        -double subtotal
        -double taxAmount
        -double discountAmount
        -double totalAmount
        -String orderStatus
        -String paymentMethod
        +addItem(FoodItem, int) void
        +calculateBill() void
        +getTotalAmount() double
    }

    class Bill {
        -Order order
        +generateReceipt() String
    }

    class InsufficientStockException {
        -int itemId
        -String itemName
        -int requestedQuantity
        -int availableStock
    }

    class CanteenService {
        -Map~Integer, FoodItem~ menuItems
        -List~Order~ orderHistory
        +processOrder(int, String, String, Map, String) Bill
        +addMenuItem(FoodItem) void
        +updateStock(int, int) boolean
        +getLowStockItems(int) List
        +getTotalRevenue() double
    }

    class CanteenWebServer {
        +main(String[] args) void
    }

    Order "1" *-- "1..*" OrderItem : contains
    OrderItem "1" o-- "1" FoodItem : references
    Bill "1" o-- "1" Order : formats
    CanteenService "1" o-- "*" FoodItem : manages
    CanteenService "1" o-- "*" Order : records
    CanteenService ..> InsufficientStockException : throws
    CanteenWebServer ..> CanteenService : uses
```

---

## 4. Sequence Diagram (Order Processing & Stock Check)

```mermaid
sequenceDiagram
    autonumber
    actor User as Customer / Student
    participant UI as Web Frontend (index.html)
    participant Server as CanteenWebServer
    participant Service as CanteenService
    participant Food as FoodItem
    participant BillObj as Bill

    User->>UI: Select items & click "Place Order"
    UI->>Server: POST /api/order (JSON payload)
    Server->>Service: processOrder(customer, items, payment)
    
    loop For each item requested
        Service->>Food: getStockQuantity()
        alt requested > available
            Food-->>Service: Insufficient Stock
            Service-->>Server: throw InsufficientStockException
            Server-->>UI: 400 Bad Request (Stock Error)
            UI-->>User: Display Stock Warning Toast
        end
    end

    loop For each item confirmed
        Service->>Food: reduceStock(quantity)
        Food-->>Service: Stock decremented
    end

    Service->>BillObj: new Bill(order)
    BillObj->>BillObj: generateReceipt()
    BillObj-->>Service: Formatted Invoice
    Service-->>Server: Return Bill
    Server-->>UI: 200 OK (Invoice JSON)
    UI-->>User: Display Digital Receipt Modal & Update UI Stock
```
