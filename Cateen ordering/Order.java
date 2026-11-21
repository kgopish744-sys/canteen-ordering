import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order.java
 * Model class representing an order transaction.
 * 
 * Course: 25ITT32 - Java Programming (Unit 1, 2, 5: Collections & Encapsulation)
 * Scenario 3: College Canteen Ordering System
 */
public class Order {
    private int orderId;
    private int customerId;
    private String customerName;
    private String customerRole; // Student, Faculty, Staff, Guest
    private LocalDate orderDate;
    private LocalTime orderTime;
    private List<OrderItem> items;
    private double subtotal;
    private double taxRate = 0.05; // 5% GST
    private double taxAmount;
    private double discountAmount;
    private double totalAmount;
    private String orderStatus; // Completed, Pending, Cancelled
    private String paymentMethod; // UPI, Cash, Card, Student Wallet

    public Order(int orderId, int customerId, String customerName, String customerRole) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerRole = customerRole;
        this.orderDate = LocalDate.now();
        this.orderTime = LocalTime.now();
        this.items = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxAmount = 0.0;
        this.discountAmount = 0.0;
        this.totalAmount = 0.0;
        this.orderStatus = "Pending";
        this.paymentMethod = "Cash";
    }

    public Order(int orderId, int customerId, String customerName, String customerRole, LocalDate orderDate, LocalTime orderTime) {
        this(orderId, customerId, customerName, customerRole);
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public void addItem(FoodItem foodItem, int quantity) {
        OrderItem item = new OrderItem(foodItem, quantity);
        this.items.add(item);
        calculateBill();
    }

    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
        calculateBill();
    }

    /**
     * Requirement 3: Calculate the total bill automatically.
     */
    public void calculateBill() {
        this.subtotal = 0.0;
        for (OrderItem item : items) {
            this.subtotal += item.getLineTotal();
        }
        this.taxAmount = Math.round(this.subtotal * this.taxRate * 100.0) / 100.0;
        this.totalAmount = Math.round((this.subtotal + this.taxAmount - this.discountAmount) * 100.0) / 100.0;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerRole() { return customerRole; }
    public void setCustomerRole(String customerRole) { this.customerRole = customerRole; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalTime orderTime) { this.orderTime = orderTime; }

    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    public double getSubtotal() { return subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { 
        this.discountAmount = discountAmount;
        calculateBill();
    }
    public double getTotalAmount() { return totalAmount; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getFormattedDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return orderDate.format(dateFormatter) + " " + orderTime.format(timeFormatter);
    }
}
