import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * CanteenWebServer.java
 * High-performance built-in Java HTTP server serving the Web UI and JSON REST endpoints.
 * Requires ZERO external dependencies - uses standard java com.sun.net.httpserver.
 * 
 * Course: 25ITT32 - Java Programming & Web Integration
 * Scenario 3: College Canteen Ordering System
 */
public class CanteenWebServer {
    private static final int PORT = 8080;
    private static final CanteenService service = new CanteenService();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Static file handler (serves HTML, CSS, JS, CSV directly from current folder)
        server.createContext("/", new StaticFileHandler());
        
        // REST API Endpoints
        server.createContext("/api/menu", new MenuHandler());
        server.createContext("/api/order", new OrderHandler());
        server.createContext("/api/sales", new SalesHandler());
        server.createContext("/api/stock", new StockHandler());

        server.setExecutor(null); // default executor
        System.out.println("==================================================================");
        System.out.println("  VELALAR COLLEGE OF ENGINEERING & TECHNOLOGY (VCET), ERODE      ");
        System.out.println("  CANTEEN ORDERING SYSTEM - JAVA WEB SERVER RUNNING              ");
        System.out.println("==================================================================");
        System.out.println(">> Web Application URL: http://localhost:" + PORT);
        System.out.println(">> REST API Endpoints:");
        System.out.println("   - GET  /api/menu   (Returns menu items with live stock)");
        System.out.println("   - POST /api/order  (Processes order with stock verification)");
        System.out.println("   - GET  /api/sales  (Returns sales and analytics data)");
        System.out.println("   - POST /api/stock  (Restocks inventory)");
        System.out.println("Press Ctrl+C to stop the server.");
        System.out.println("==================================================================");
        server.start();
    }

    /**
     * Serves static frontend files: index.html, styles.css, app.js, canteen_sales_data.csv.
     */
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            File file = new File("." + path);
            if (!file.exists() || file.isDirectory()) {
                file = new File(path.substring(1));
            }

            if (!file.exists()) {
                String response = "404 (Not Found)\nFile not found: " + path;
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes(StandardCharsets.UTF_8));
                os.close();
                return;
            }

            String contentType = "text/plain";
            if (path.endsWith(".html")) contentType = "text/html; charset=UTF-8";
            else if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".csv")) contentType = "text/csv; charset=UTF-8";
            else if (path.endsWith(".png")) contentType = "image/png";

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    /**
     * GET /api/menu : returns JSON array of menu items with stock.
     */
    static class MenuHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }

            List<FoodItem> items = service.getAllMenuItems();
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < items.size(); i++) {
                FoodItem item = items.get(i);
                json.append("{")
                    .append("\"itemId\":").append(item.getItemId()).append(",")
                    .append("\"itemName\":\"").append(escapeJson(item.getItemName())).append("\",")
                    .append("\"category\":\"").append(escapeJson(item.getCategory())).append("\",")
                    .append("\"price\":").append(item.getPrice()).append(",")
                    .append("\"stockQuantity\":").append(item.getStockQuantity()).append(",")
                    .append("\"isAvailable\":").append(item.isAvailable())
                    .append("}");
                if (i < items.size() - 1) json.append(",");
            }
            json.append("]");

            byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    /**
     * POST /api/order : Processes order, performs stock check, updates stock.
     */
    static class OrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                body.append(line);
            }

            // Simple parser for lightweight JSON payload
            String payload = body.toString();
            try {
                String customerName = extractString(payload, "customerName", "Student Customer");
                String customerRole = extractString(payload, "customerRole", "Student");
                String paymentMethod = extractString(payload, "paymentMethod", "UPI");

                Map<Integer, Integer> items = extractItems(payload);

                Bill bill = service.processOrder(101, customerName, customerRole, items, paymentMethod);

                String jsonResponse = "{\"success\":true,\"orderId\":" + bill.getOrder().getOrderId() +
                        ",\"totalAmount\":" + bill.getOrder().getTotalAmount() +
                        ",\"receipt\":\"" + escapeJson(bill.generateReceipt()) + "\"}";

                byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } catch (InsufficientStockException e) {
                String error = "{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) +
                        "\",\"itemId\":" + e.getItemId() + ",\"availableStock\":" + e.getAvailableStock() + "}";
                byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(400, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } catch (Exception e) {
                String error = "{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}";
                byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(500, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        }
    }

    /**
     * GET /api/sales : returns sales statistics.
     */
    static class SalesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }

            StringBuilder json = new StringBuilder("{");
            json.append("\"totalRevenue\":").append(service.getTotalRevenue()).append(",");
            json.append("\"orderCount\":").append(service.getOrderHistory().size()).append(",");
            json.append("\"topItems\":[");
            int i = 0;
            for (Map.Entry<String, Integer> e : service.getItemSalesCount().entrySet()) {
                json.append("{\"item\":\"").append(escapeJson(e.getKey())).append("\",\"count\":").append(e.getValue()).append("}");
                if (++i < service.getItemSalesCount().size()) json.append(",");
            }
            json.append("]}");

            byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    /**
     * POST /api/stock : Restocks inventory.
     */
    static class StockHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }

            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) body.append(line);

            String payload = body.toString();
            int itemId = extractInt(payload, "itemId", 0);
            int quantity = extractInt(payload, "quantity", 0);

            boolean updated = service.updateStock(itemId, quantity);
            String resp = "{\"success\":" + updated + "}";
            byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    private static void sendCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private static String extractString(String json, String key, String defaultVal) {
        String pattern = "\"" + key + "\":\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return defaultVal;
        int start = idx + pattern.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return defaultVal;
        return json.substring(start, end);
    }

    private static int extractInt(String json, String key, int defaultVal) {
        String pattern = "\"" + key + "\":";
        int idx = json.indexOf(pattern);
        if (idx == -1) return defaultVal;
        int start = idx + pattern.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
            end++;
        }
        try {
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private static Map<Integer, Integer> extractItems(String json) {
        Map<Integer, Integer> map = new HashMap<>();
        int itemsIdx = json.indexOf("\"items\":[");
        if (itemsIdx == -1) return map;
        int start = itemsIdx + "\"items\":[".length();
        int end = json.indexOf("]", start);
        if (end == -1) return map;
        String arrayContent = json.substring(start, end);
        String[] objects = arrayContent.split("},\\{");
        for (String obj : objects) {
            int itemId = extractInt(obj, "itemId", 0);
            int qty = extractInt(obj, "quantity", 0);
            if (itemId > 0 && qty > 0) {
                map.put(itemId, qty);
            }
        }
        return map;
    }
}
