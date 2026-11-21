/**
 * OrderItem.java
 * Represents an individual food item and quantity inside an order.
 * 
 * Course: 25ITT32 - Java Programming (Unit 1 & 2: Object Composition)
 * Scenario 3: College Canteen Ordering System
 */
public class OrderItem {
    private FoodItem foodItem;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public OrderItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.unitPrice = foodItem.getPrice();
        this.lineTotal = this.unitPrice * quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
        this.unitPrice = foodItem.getPrice();
        this.lineTotal = this.unitPrice * this.quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lineTotal = this.unitPrice * quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    @Override
    public String toString() {
        return String.format("%-25s x %2d @ Rs.%6.2f = Rs.%8.2f",
                foodItem.getItemName(), quantity, unitPrice, lineTotal);
    }
}
