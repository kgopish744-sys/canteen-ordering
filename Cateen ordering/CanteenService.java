import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CanteenService.java
 * Core business logic service implementing all 8 functional requirements:
 * 1. Maintain food item names, prices, and stock quantities.
 * 2. Accept orders containing one or more food items.
 * 3. Calculate total bill automatically.
 * 4. Update stock quantities when orders are completed.
 * 5. Store transaction details and generate sales reports.
 * 6. Prevent orders that exceed available stock.
 * 
 * Course: 25ITT32 - Java Programming (Collections, Streams, Lambdas, Exception Handling)
 */
public class CanteenService {
    private final Map<Integer, FoodItem> menuItems;
    private final List<Order> orderHistory;
    private int nextOrderId = 136;
    private int nextItemId = 21;

    public CanteenService() {
        this.menuItems = new LinkedHashMap<>();
        this.orderHistory = new ArrayList<>();
        initializeDefaultMenu();
    }

    /**
     * Initialize standard campus canteen menu items.
     */
    private void initializeDefaultMenu() {
        addMenuItem(new FoodItem(1, "Ghee Roast Dosa", "Breakfast", 45.00, 40));
        addMenuItem(new FoodItem(2, "Idli & Vada Combo", "Breakfast", 30.00, 50));
        addMenuItem(new FoodItem(3, "Poori Masala (2 pcs)", "Breakfast", 40.00, 35));
        addMenuItem(new FoodItem(4, "Mini Tiffin Platter", "Breakfast", 60.00, 25));
        addMenuItem(new FoodItem(5, "Pongal with Chutney", "Breakfast", 35.00, 30));
        addMenuItem(new FoodItem(6, "South Indian Veg Meals", "Lunch & Meals", 70.00, 60));
        addMenuItem(new FoodItem(7, "Egg Fried Rice", "Lunch & Meals", 65.00, 45));
        addMenuItem(new FoodItem(8, "Chicken Biryani", "Lunch & Meals", 110.00, 50));
        addMenuItem(new FoodItem(9, "Paneer Butter Masala Combo", "Lunch & Meals", 85.00, 30));
        addMenuItem(new FoodItem(10, "Curd Rice with Pickle", "Lunch & Meals", 40.00, 35));
        addMenuItem(new FoodItem(11, "Veg Puff", "Snacks & Quick Bites", 20.00, 55));
        addMenuItem(new FoodItem(12, "Egg Puff", "Snacks & Quick Bites", 25.00, 40));
        addMenuItem(new FoodItem(13, "Samosa (2 pcs)", "Snacks & Quick Bites", 20.00, 60));
        addMenuItem(new FoodItem(14, "Paneer Roll", "Snacks & Quick Bites", 50.00, 25));
        addMenuItem(new FoodItem(15, "Veg Cutlet (2 pcs)", "Snacks & Quick Bites", 25.00, 30));
        addMenuItem(new FoodItem(16, "Filter Coffee", "Beverages", 15.00, 80));
        addMenuItem(new FoodItem(17, "Masala Chai", "Beverages", 12.00, 100));
        addMenuItem(new FoodItem(18, "Fresh Lemon Juice", "Beverages", 25.00, 50));
        addMenuItem(new FoodItem(19, "Cold Badam Milk", "Beverages", 30.00, 40));
        addMenuItem(new FoodItem(20, "Bottled Water (500ml)", "Beverages", 10.00, 120));
    }

    // FR 1: Maintain food item names, prices, and stock quantities
    public synchronized void addMenuItem(FoodItem item) {
        menuItems.put(item.getItemId(), item);
    }

    public synchronized FoodItem createMenuItem(String name, String category, double price, int stock) {
        FoodItem item = new FoodItem(nextItemId++, name, category, price, stock, stock > 0);
        menuItems.put(item.getItemId(), item);
        return item;
    }

    public synchronized boolean updateItemPrice(int itemId, double newPrice) {
        FoodItem item = menuItems.get(itemId);
        if (item != null && newPrice >= 0) {
            item.setPrice(newPrice);
            return true;
        }
        return false;
    }

    public synchronized boolean updateStock(int itemId, int additionalStock) {
        FoodItem item = menuItems.get(itemId);
        if (item != null && additionalStock > 0) {
            item.addStock(additionalStock);
            return true;
        }
        return false;
    }

    public List<FoodItem> getAllMenuItems() {
        return new ArrayList<>(menuItems.values());
    }

    public FoodItem getMenuItem(int itemId) {
        return menuItems.get(itemId);
    }

    public List<FoodItem> getLowStockItems(int threshold) {
        return menuItems.values().stream()
                .filter(item -> item.getStockQuantity() <= threshold)
                .collect(Collectors.toList());
    }

    /**
     * FR 2, FR 3, FR 4, FR 6:
     * Accepts order, validates available stock, prevents order exceeding stock,
     * calculates bill, deducts stock atomically, and logs transaction.
     */
    public synchronized Bill processOrder(int customerId, String customerName, String customerRole,
                                         Map<Integer, Integer> requestedItems, String paymentMethod) 
                                         throws InsufficientStockException {
        
        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one food item.");
        }

        // STEP 1: FR 6 - Pre-validate all items for sufficient stock before making any updates
        for (Map.Entry<Integer, Integer> entry : requestedItems.entrySet()) {
            int itemId = entry.getKey();
            int requestedQty = entry.getValue();

            FoodItem item = menuItems.get(itemId);
            if (item == null) {
                throw new IllegalArgumentException("Food item ID " + itemId + " does not exist in the menu.");
            }
            if (requestedQty <= 0) {
                throw new IllegalArgumentException("Quantity for '" + item.getItemName() + "' must be greater than 0.");
            }
            if (requestedQty > item.getStockQuantity()) {
                // Prevent order exceeding available stock!
                throw new InsufficientStockException(itemId, item.getItemName(), requestedQty, item.getStockQuantity());
            }
        }

        // STEP 2: All items valid, instantiate Order object
        Order order = new Order(nextOrderId++, customerId, customerName, customerRole);
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "UPI");

        // STEP 3: FR 4 - Deduct stock quantities and add items to order
        for (Map.Entry<Integer, Integer> entry : requestedItems.entrySet()) {
            int itemId = entry.getKey();
            int qty = entry.getValue();
            FoodItem item = menuItems.get(itemId);

            item.reduceStock(qty); // reduces stock quantity
            order.addItem(item, qty); // calculates bill lines
        }

        order.setOrderStatus("Completed");

        // STEP 4: FR 5 - Store transaction details
        orderHistory.add(order);

        // Return generated Bill with automatic calculations
        return new Bill(order);
    }

    // FR 5: Sales Reporting and Analytics
    public List<Order> getOrderHistory() {
        return Collections.unmodifiableList(orderHistory);
    }

    public double getTotalRevenue() {
        return orderHistory.stream()
                .filter(o -> "Completed".equalsIgnoreCase(o.getOrderStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public Map<LocalDate, Double> getDailyRevenue() {
        return orderHistory.stream()
                .filter(o -> "Completed".equalsIgnoreCase(o.getOrderStatus()))
                .collect(Collectors.groupingBy(
                        Order::getOrderDate,
                        TreeMap::new,
                        Collectors.summingDouble(Order::getTotalAmount)
                ));
    }

    public Map<String, Integer> getItemSalesCount() {
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (Order o : orderHistory) {
            if ("Completed".equalsIgnoreCase(o.getOrderStatus())) {
                for (OrderItem item : o.getItems()) {
                    String name = item.getFoodItem().getItemName();
                    countMap.put(name, countMap.getOrDefault(name, 0) + item.getQuantity());
                }
            }
        }
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
