/**
 * FoodItem.java
 * Model class representing a food item in the canteen menu.
 * 
 * Course: 25ITT32 - Java Programming (Unit 1 & 2: Classes, Objects, Encapsulation)
 * Scenario 3: College Canteen Ordering System
 */
public class FoodItem {
    private int itemId;
    private String itemName;
    private String category;
    private double price;
    private int stockQuantity;
    private boolean isAvailable;

    public FoodItem(int itemId, String itemName, String category, double price, int stockQuantity, boolean isAvailable) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isAvailable = isAvailable;
    }

    public FoodItem(int itemId, String itemName, String category, double price, int stockQuantity) {
        this(itemId, itemName, category, price, stockQuantity, stockQuantity > 0);
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        this.isAvailable = stockQuantity > 0;
    }

    public boolean isAvailable() {
        return isAvailable && stockQuantity > 0;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Deducts stock upon successful order placement.
     * Throws InsufficientStockException if quantity exceeds available stock.
     */
    public synchronized void reduceStock(int quantity) throws InsufficientStockException {
        if (quantity > this.stockQuantity) {
            throw new InsufficientStockException(this.itemId, this.itemName, quantity, this.stockQuantity);
        }
        this.stockQuantity -= quantity;
        if (this.stockQuantity == 0) {
            this.isAvailable = false;
        }
    }

    /**
     * Adds stock replenishment.
     */
    public synchronized void addStock(int quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
            this.isAvailable = true;
        }
    }

    @Override
    public String toString() {
        return String.format("[%d] %-25s | %-15s | Rs. %6.2f | Stock: %3d | %s",
                itemId, itemName, category, price, stockQuantity, isAvailable() ? "AVAILABLE" : "OUT OF STOCK");
    }
}
