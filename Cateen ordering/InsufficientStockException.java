/**
 * InsufficientStockException.java
 * Custom Exception thrown when an order quantity exceeds available stock.
 * 
 * Course: 25ITT32 - Java Programming (Unit 3: Exception Handling)
 * Academic Year: 2026-27 | Scenario 3: College Canteen Ordering System
 */
public class InsufficientStockException extends Exception {
    private final int itemId;
    private final String itemName;
    private final int requestedQuantity;
    private final int availableStock;

    public InsufficientStockException(int itemId, String itemName, int requestedQuantity, int availableStock) {
        super(String.format("Cannot complete order for '%s' (Item ID: %d). Requested: %d, Available Stock: %d",
                itemName, itemId, requestedQuantity, availableStock));
        this.itemId = itemId;
        this.itemName = itemName;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
