/**
 * Bill.java
 * Utility class to generate structured tax invoices and bill receipts.
 * 
 * Course: 25ITT32 - Java Programming (Unit 1, 4: Strings, String Formatting)
 * Functional Requirement 3: Calculate the total bill automatically
 */
public class Bill {
    private Order order;

    public Bill(Order order) {
        this.order = order;
        this.order.calculateBill();
    }

    public Order getOrder() {
        return order;
    }

    /**
     * Generates a formatted ASCII invoice receipt.
     */
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        String line = "========================================================\n";
        String dashed = "--------------------------------------------------------\n";

        sb.append(line);
        sb.append("      VELALAR COLLEGE OF ENGINEERING & TECHNOLOGY       \n");
        sb.append("                 CAMPUS CANTEEN INVOICE                 \n");
        sb.append("               Thindal, Erode - 638 012                 \n");
        sb.append(line);
        sb.append(String.format("Invoice No : VCET-CAN-%-8d Date: %s\n", 
                order.getOrderId(), order.getOrderDate()));
        sb.append(String.format("Customer   : %-20s Role: %s\n", 
                order.getCustomerName(), order.getCustomerRole()));
        sb.append(String.format("Payment Via: %-20s Status: %s\n", 
                order.getPaymentMethod(), order.getOrderStatus()));
        sb.append(dashed);
        sb.append(String.format("%-4s %-24s %5s %7s %10s\n", "#", "Item Description", "Qty", "Price", "Amount"));
        sb.append(dashed);

        int count = 1;
        for (OrderItem item : order.getItems()) {
            sb.append(String.format("%-4d %-24s %5d %7.2f %10.2f\n",
                    count++,
                    truncate(item.getFoodItem().getItemName(), 24),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal()));
        }

        sb.append(dashed);
        sb.append(String.format("%-38s Rs. %10.2f\n", "Subtotal:", order.getSubtotal()));
        sb.append(String.format("%-38s Rs. %10.2f\n", "GST (5%):", order.getTaxAmount()));
        if (order.getDiscountAmount() > 0) {
            sb.append(String.format("%-38s-Rs. %10.2f\n", "Discount:", order.getDiscountAmount()));
        }
        sb.append(line);
        sb.append(String.format("%-38s Rs. %10.2f\n", "NET AMOUNT PAYABLE:", order.getTotalAmount()));
        sb.append(line);
        sb.append("           Thank you! Enjoy your fresh meal!            \n");
        sb.append("         * Please collect your token at counter *       \n");
        sb.append(line);

        return sb.toString();
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    @Override
    public String toString() {
        return generateReceipt();
    }
}
