import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * CanteenApp.java
 * Standalone runnable console application for testing and demonstration.
 * 
 * Course: 25ITT32 - Java Programming
 * Scenario 3: College Canteen Ordering and Sales Analysis System
 */
public class CanteenApp {
    private static final CanteenService service = new CanteenService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (VCET), ERODE  ");
        System.out.println("         CANTEEN ORDERING AND SALES ANALYSIS SYSTEM            ");
        System.out.println("     Scenario 3 | DBMS (25ITT31) & Java Programming (25ITT32)  ");
        System.out.println("===============================================================");

        // Preload some simulated transaction data for instant testing
        seedSimulatedOrders();

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter your choice (1-7): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayMenu();
                    break;
                case "2":
                    placeOrderInteractive();
                    break;
                case "3":
                    inventoryManagement();
                    break;
                case "4":
                    viewDailySalesReport();
                    break;
                case "5":
                    viewPopularItems();
                    break;
                case "6":
                    viewLowStockAlerts();
                    break;
                case "7":
                    System.out.println("\nExiting Canteen Ordering System. Have a nice day!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection! Please enter a number between 1 and 7.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n---------------- MAIN OPERATION MENU ----------------");
        System.out.println("1. View Current Canteen Menu (Names, Prices, Real-time Stock)");
        System.out.println("2. Place New Customer Order (Multi-item, Auto-Bill & Stock Check)");
        System.out.println("3. Manage Inventory & Menu (Restock / Price Update / Add Item)");
        System.out.println("4. Generate Daily Sales & Revenue Report");
        System.out.println("5. View Popular Food Items & Sales Rankings");
        System.out.println("6. Check Low Stock Alerts");
        System.out.println("7. Exit Application");
        System.out.println("-----------------------------------------------------");
    }

    private static void displayMenu() {
        System.out.println("\n================================== CANTEEN FOOD MENU ==================================");
        System.out.printf("%-4s %-28s %-20s %-10s %-8s %-12s\n", "ID", "Item Name", "Category", "Price", "Stock", "Status");
        System.out.println("---------------------------------------------------------------------------------------");
        for (FoodItem item : service.getAllMenuItems()) {
            System.out.printf("%-4d %-28s %-20s Rs.%-7.2f %-8d %-12s\n",
                    item.getItemId(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getStockQuantity(),
                    item.isAvailable() ? "AVAILABLE" : "OUT OF STOCK");
        }
        System.out.println("=======================================================================================");
    }

    private static void placeOrderInteractive() {
        System.out.println("\n--- PLACE NEW ORDER ---");
        System.out.print("Enter Customer Name (e.g. Aravind / Dr. Priya): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Walk-in Student";

        System.out.print("Enter Customer Role (Student / Faculty / Staff / Guest) [Student]: ");
        String role = scanner.nextLine().trim();
        if (role.isEmpty()) role = "Student";

        System.out.print("Enter Payment Method (UPI / Cash / Card / Student Wallet) [UPI]: ");
        String payment = scanner.nextLine().trim();
        if (payment.isEmpty()) payment = "UPI";

        Map<Integer, Integer> requestedItems = new HashMap<>();
        boolean addingItems = true;

        while (addingItems) {
            System.out.print("\nEnter Food Item ID (or '0' to finish order): ");
            String idStr = scanner.nextLine().trim();
            if (idStr.equals("0")) {
                break;
            }

            try {
                int itemId = Integer.parseInt(idStr);
                FoodItem item = service.getMenuItem(itemId);
                if (item == null) {
                    System.out.println("Item ID not found! Please check menu.");
                    continue;
                }

                System.out.printf("Selected: %s (Available Stock: %d, Price: Rs.%.2f)\n", 
                        item.getItemName(), item.getStockQuantity(), item.getPrice());
                System.out.print("Enter Quantity to order: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());

                if (qty <= 0) {
                    System.out.println("Quantity must be greater than zero.");
                    continue;
                }

                // Check stock beforehand
                if (qty > item.getStockQuantity()) {
                    System.out.printf("[ERROR] Insufficient stock! Requested: %d, Available: %d\n", 
                            qty, item.getStockQuantity());
                    continue;
                }

                requestedItems.put(itemId, requestedItems.getOrDefault(itemId, 0) + qty);
                System.out.printf("Added %d x %s to cart. Total items in cart: %d\n", 
                        qty, item.getItemName(), requestedItems.size());

                System.out.print("Add another item? (y/n) [n]: ");
                String more = scanner.nextLine().trim();
                if (!more.equalsIgnoreCase("y")) {
                    addingItems = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input!");
            }
        }

        if (requestedItems.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return;
        }

        try {
            Bill bill = service.processOrder(101, name, role, requestedItems, payment);
            System.out.println("\n*** ORDER PROCESSED SUCCESSFULLY! ***\n");
            System.out.println(bill.generateReceipt());
        } catch (InsufficientStockException e) {
            System.out.println("\n[TRANSACTION FAILED] Stock constraint violated!");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[TRANSACTION FAILED] Error: " + e.getMessage());
        }
    }

    private static void inventoryManagement() {
        System.out.println("\n--- INVENTORY & MENU MANAGEMENT ---");
        System.out.println("1. Restock Existing Item");
        System.out.println("2. Update Item Price");
        System.out.println("3. Add New Item to Menu");
        System.out.print("Enter option (1-3): ");
        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1":
                System.out.print("Enter Food Item ID to restock: ");
                int id = Integer.parseInt(scanner.nextLine().trim());
                FoodItem item = service.getMenuItem(id);
                if (item != null) {
                    System.out.printf("Current Stock of '%s': %d\n", item.getItemName(), item.getStockQuantity());
                    System.out.print("Enter quantity to add: ");
                    int addQty = Integer.parseInt(scanner.nextLine().trim());
                    service.updateStock(id, addQty);
                    System.out.printf("Success! New stock for '%s' is %d\n", item.getItemName(), item.getStockQuantity());
                } else {
                    System.out.println("Item not found!");
                }
                break;
            case "2":
                System.out.print("Enter Food Item ID to update price: ");
                int priceId = Integer.parseInt(scanner.nextLine().trim());
                FoodItem pItem = service.getMenuItem(priceId);
                if (pItem != null) {
                    System.out.printf("Current Price of '%s': Rs.%.2f\n", pItem.getItemName(), pItem.getPrice());
                    System.out.print("Enter new price (Rs.): ");
                    double newPrice = Double.parseDouble(scanner.nextLine().trim());
                    service.updateItemPrice(priceId, newPrice);
                    System.out.printf("Success! Price updated to Rs.%.2f\n", newPrice);
                } else {
                    System.out.println("Item not found!");
                }
                break;
            case "3":
                System.out.print("Enter New Food Item Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Enter Category (Breakfast / Lunch & Meals / Snacks & Quick Bites / Beverages): ");
                String cat = scanner.nextLine().trim();
                System.out.print("Enter Unit Price (Rs.): ");
                double price = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Enter Initial Stock Quantity: ");
                int stock = Integer.parseInt(scanner.nextLine().trim());
                FoodItem created = service.createMenuItem(name, cat, price, stock);
                System.out.printf("Success! Created Item: [%d] %s with stock %d\n", created.getItemId(), created.getItemName(), stock);
                break;
            default:
                System.out.println("Invalid inventory option.");
        }
    }

    private static void viewDailySalesReport() {
        System.out.println("\n=========================== SALES & REVENUE REPORT ===========================");
        System.out.printf("Total Completed Orders : %d\n", service.getOrderHistory().size());
        System.out.printf("Total Gross Revenue    : Rs. %.2f\n", service.getTotalRevenue());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Recent Orders:");
        System.out.printf("%-8s %-12s %-20s %-15s %-10s %-10s\n", 
                "Order#", "Date", "Customer", "Role", "Amount", "Payment");
        System.out.println("------------------------------------------------------------------------------");
        for (Order o : service.getOrderHistory()) {
            System.out.printf("%-8d %-12s %-20s %-15s Rs.%-8.2f %-10s\n",
                    o.getOrderId(),
                    o.getOrderDate(),
                    o.getCustomerName(),
                    o.getCustomerRole(),
                    o.getTotalAmount(),
                    o.getPaymentMethod());
        }
        System.out.println("==============================================================================");
    }

    private static void viewPopularItems() {
        System.out.println("\n======================== TOP SELLING FOOD ITEMS ========================");
        System.out.printf("%-4s %-28s %-15s\n", "Rank", "Food Item Name", "Units Sold");
        System.out.println("-----------------------------------------------------------------------");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : service.getItemSalesCount().entrySet()) {
            System.out.printf("%-4d %-28s %-15d\n", rank++, entry.getKey(), entry.getValue());
            if (rank > 10) break;
        }
        System.out.println("=======================================================================");
    }

    private static void viewLowStockAlerts() {
        System.out.println("\n======================= LOW STOCK INVENTORY ALERTS (<= 35 units) =======================");
        List<FoodItem> low = service.getLowStockItems(35);
        if (low.isEmpty()) {
            System.out.println("All food items have sufficient inventory stock levels.");
        } else {
            System.out.printf("%-4s %-28s %-20s %-10s %-10s\n", "ID", "Item Name", "Category", "Price", "Stock Left");
            System.out.println("---------------------------------------------------------------------------------------");
            for (FoodItem f : low) {
                System.out.printf("%-4d %-28s %-20s Rs.%-8.2f %-10d (REPLENISH)\n",
                        f.getItemId(), f.getItemName(), f.getCategory(), f.getPrice(), f.getStockQuantity());
            }
        }
        System.out.println("=======================================================================================");
    }

    private static void seedSimulatedOrders() {
        try {
            Map<Integer, Integer> o1 = new HashMap<>();
            o1.put(1, 2); // Ghee Roast Dosa
            o1.put(16, 2); // Filter Coffee
            service.processOrder(1, "Aravind Swaminathan", "Student", o1, "UPI");

            Map<Integer, Integer> o2 = new HashMap<>();
            o2.put(8, 2); // Chicken Biryani
            o2.put(18, 2); // Lemon Juice
            service.processOrder(5, "Dr. S. Priya", "Faculty", o2, "Card");

            Map<Integer, Integer> o3 = new HashMap<>();
            o3.put(13, 3); // Samosa
            o3.put(17, 3); // Masala Chai
            service.processOrder(3, "Gowtham K", "Student", o3, "Student Wallet");
        } catch (Exception ignored) {
        }
    }
}
