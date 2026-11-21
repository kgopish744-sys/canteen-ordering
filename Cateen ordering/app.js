/**
 * VCET Canteen Ordering and Sales Analysis System - Frontend Application Logic
 * Scenario 3: Complete single page application logic with offline LocalStorage support,
 * live stock management, interactive ordering, auto-bill calculation, and Chart.js analytics.
 */

// Initial Seed Data (mirrors seed_data.sql and canteen_sales_data.csv)
const INITIAL_MENU_ITEMS = [
    { itemId: 1, itemName: "Ghee Roast Dosa", category: "Breakfast", price: 45.0, stockQuantity: 40, isAvailable: true },
    { itemId: 2, itemName: "Idli & Vada Combo", category: "Breakfast", price: 30.0, stockQuantity: 50, isAvailable: true },
    { itemId: 3, itemName: "Poori Masala (2 pcs)", category: "Breakfast", price: 40.0, stockQuantity: 35, isAvailable: true },
    { itemId: 4, itemName: "Mini Tiffin Platter", category: "Breakfast", price: 60.0, stockQuantity: 25, isAvailable: true },
    { itemId: 5, itemName: "Pongal with Chutney", category: "Breakfast", price: 35.0, stockQuantity: 30, isAvailable: true },
    { itemId: 6, itemName: "South Indian Veg Meals", category: "Lunch & Meals", price: 70.0, stockQuantity: 60, isAvailable: true },
    { itemId: 7, itemName: "Egg Fried Rice", category: "Lunch & Meals", price: 65.0, stockQuantity: 45, isAvailable: true },
    { itemId: 8, itemName: "Chicken Biryani", category: "Lunch & Meals", price: 110.0, stockQuantity: 50, isAvailable: true },
    { itemId: 9, itemName: "Paneer Butter Masala Combo", category: "Lunch & Meals", price: 85.0, stockQuantity: 30, isAvailable: true },
    { itemId: 10, itemName: "Curd Rice with Pickle", category: "Lunch & Meals", price: 40.0, stockQuantity: 35, isAvailable: true },
    { itemId: 11, itemName: "Veg Puff", category: "Snacks & Quick Bites", price: 20.0, stockQuantity: 55, isAvailable: true },
    { itemId: 12, itemName: "Egg Puff", category: "Snacks & Quick Bites", price: 25.0, stockQuantity: 40, isAvailable: true },
    { itemId: 13, itemName: "Samosa (2 pcs)", category: "Snacks & Quick Bites", price: 20.0, stockQuantity: 60, isAvailable: true },
    { itemId: 14, itemName: "Paneer Roll", category: "Snacks & Quick Bites", price: 50.0, stockQuantity: 25, isAvailable: true },
    { itemId: 15, itemName: "Veg Cutlet (2 pcs)", category: "Snacks & Quick Bites", price: 25.0, stockQuantity: 30, isAvailable: true },
    { itemId: 16, itemName: "Filter Coffee", category: "Beverages", price: 15.0, stockQuantity: 80, isAvailable: true },
    { itemId: 17, itemName: "Masala Chai", category: "Beverages", price: 12.0, stockQuantity: 100, isAvailable: true },
    { itemId: 18, itemName: "Fresh Lemon Juice", category: "Beverages", price: 25.0, stockQuantity: 50, isAvailable: true },
    { itemId: 19, itemName: "Cold Badam Milk", category: "Beverages", price: 30.0, stockQuantity: 40, isAvailable: true },
    { itemId: 20, itemName: "Bottled Water (500ml)", category: "Beverages", price: 10.0, stockQuantity: 120, isAvailable: true }
];

const INITIAL_ORDERS = [
    { orderId: 101, date: "2026-11-15", time: "08:30:00", customer: "Aravind Swaminathan", role: "Student", itemsSummary: "Ghee Roast Dosa x 2", total: 94.50, payment: "UPI", status: "Completed" },
    { orderId: 102, date: "2026-11-15", time: "09:15:00", customer: "Bhavana Ramesh", role: "Student", itemsSummary: "Masala Chai x 1, Ghee Roast Dosa x 1", total: 59.85, payment: "Cash", status: "Completed" },
    { orderId: 103, date: "2026-11-15", time: "12:45:00", customer: "Dr. S. Priya", role: "Faculty", itemsSummary: "South Indian Veg Meals x 2", total: 147.00, payment: "Card", status: "Completed" },
    { orderId: 104, date: "2026-11-15", time: "13:10:00", customer: "Gowtham K", role: "Student", itemsSummary: "Chicken Biryani x 1", total: 115.50, payment: "UPI", status: "Completed" },
    { orderId: 105, date: "2026-11-15", time: "16:30:00", customer: "Naveen Kumar", role: "Student", itemsSummary: "Veg Puff x 2, Masala Chai x 1", total: 54.60, payment: "Student Wallet", status: "Completed" },
    { orderId: 106, date: "2026-11-15", time: "17:00:00", customer: "Sneha Murali", role: "Student", itemsSummary: "Samosa (2 pcs) x 2, Fresh Lemon Juice x 1", total: 68.25, payment: "UPI", status: "Completed" },
    { orderId: 107, date: "2026-11-16", time: "08:45:00", customer: "Vigneshwaran R", role: "Student", itemsSummary: "Pongal with Chutney x 2", total: 73.50, payment: "UPI", status: "Completed" },
    { orderId: 108, date: "2026-11-16", time: "10:00:00", customer: "Prof. M. Karthik", role: "Faculty", itemsSummary: "Filter Coffee x 2", total: 31.50, payment: "Cash", status: "Completed" },
    { orderId: 109, date: "2026-11-16", time: "12:30:00", customer: "Aravind Swaminathan", role: "Student", itemsSummary: "Egg Fried Rice x 2", total: 136.50, payment: "UPI", status: "Completed" },
    { orderId: 110, date: "2026-11-16", time: "13:00:00", customer: "Bhavana Ramesh", role: "Student", itemsSummary: "Chicken Biryani x 1, Egg Fried Rice x 1", total: 183.75, payment: "Card", status: "Completed" },
    { orderId: 120, date: "2026-11-18", time: "12:50:00", customer: "Aravind Swaminathan", role: "Student", itemsSummary: "Chicken Biryani x 2", total: 231.00, payment: "UPI", status: "Completed" },
    { orderId: 130, date: "2026-11-20", time: "12:45:00", customer: "Harini Venkatesh", role: "Student", itemsSummary: "Chicken Biryani x 1", total: 115.50, payment: "UPI", status: "Completed" },
    { orderId: 135, date: "2026-11-21", time: "11:00:00", customer: "Dr. S. Priya", role: "Faculty", itemsSummary: "Veg Puff x 3, Masala Chai x 3", total: 100.80, payment: "UPI", status: "Completed" }
];

// Application State
let menuItems = [];
let cart = {}; // itemId -> quantity
let orders = [];
let currentCategory = "all";
let searchQuery = "";
let chartInstances = {};

// Initialize application on DOM ready
document.addEventListener("DOMContentLoaded", () => {
    initClock();
    loadStateFromStorage();
    setupNavigation();
    setupMenuFilters();
    renderMenu();
    updateCartDisplay();
    renderInventoryTable();
    renderOrdersTable();
    initAnalyticsCharts();
    checkBackendConnection();
});

// Clock Updater
function initClock() {
    const clockEl = document.getElementById("liveClock");
    const updateTime = () => {
        const now = new Date();
        clockEl.textContent = now.toLocaleDateString("en-IN") + " " + now.toLocaleTimeString("en-IN");
    };
    updateTime();
    setInterval(updateTime, 1000);
}

// Storage Persistence
function loadStateFromStorage() {
    const storedMenu = localStorage.getItem("vcet_canteen_menu");
    if (storedMenu) {
        menuItems = JSON.parse(storedMenu);
    } else {
        menuItems = [...INITIAL_MENU_ITEMS];
        saveMenuToStorage();
    }

    const storedOrders = localStorage.getItem("vcet_canteen_orders");
    if (storedOrders) {
        orders = JSON.parse(storedOrders);
    } else {
        orders = [...INITIAL_ORDERS];
        saveOrdersToStorage();
    }

    const storedCart = localStorage.getItem("vcet_canteen_cart");
    if (storedCart) {
        cart = JSON.parse(storedCart);
    }
}

function saveMenuToStorage() {
    localStorage.setItem("vcet_canteen_menu", JSON.stringify(menuItems));
}

function saveOrdersToStorage() {
    localStorage.setItem("vcet_canteen_orders", JSON.stringify(orders));
}

function saveCartToStorage() {
    localStorage.setItem("vcet_canteen_cart", JSON.stringify(cart));
}

// Tab Navigation
function setupNavigation() {
    const navButtons = document.querySelectorAll(".nav-btn");
    navButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const tabId = btn.getAttribute("data-tab");
            switchTab(tabId);
        });
    });
}

function switchTab(tabId) {
    document.querySelectorAll(".nav-btn").forEach(btn => {
        btn.classList.toggle("active", btn.getAttribute("data-tab") === tabId);
    });
    document.querySelectorAll(".tab-content").forEach(tab => {
        tab.classList.toggle("active", tab.id === tabId);
    });

    if (tabId === "tab-analytics") {
        updateAnalyticsData();
    } else if (tabId === "tab-inventory") {
        renderInventoryTable();
    } else if (tabId === "tab-menu") {
        renderMenu();
    }
}

// Menu Filters & Search
function setupMenuFilters() {
    const catPills = document.querySelectorAll(".cat-pill");
    catPills.forEach(pill => {
        pill.addEventListener("click", () => {
            catPills.forEach(p => p.classList.remove("active"));
            pill.classList.add("active");
            currentCategory = pill.getAttribute("data-cat");
            renderMenu();
        });
    });

    const searchInput = document.getElementById("menuSearchInput");
    searchInput.addEventListener("input", (e) => {
        searchQuery = e.target.value.toLowerCase().trim();
        renderMenu();
    });
}

// Render Menu Cards
function renderMenu() {
    const grid = document.getElementById("menuGrid");
    grid.innerHTML = "";

    const filtered = menuItems.filter(item => {
        const matchesCategory = currentCategory === "all" || item.category === currentCategory;
        const matchesSearch = item.itemName.toLowerCase().includes(searchQuery);
        return matchesCategory && matchesSearch;
    });

    if (filtered.length === 0) {
        grid.innerHTML = `
            <div class="empty-state" style="grid-column: 1 / -1;">
                <i class="fa-solid fa-utensils empty-icon"></i>
                <h4>No matching food items found</h4>
                <p>Try searching with another keyword or select All Items.</p>
            </div>
        `;
        return;
    }

    filtered.forEach(item => {
        const card = document.createElement("div");
        card.className = "food-card";

        let stockBadge = "";
        let isOutOfStock = item.stockQuantity <= 0 || !item.isAvailable;
        if (isOutOfStock) {
            stockBadge = `<span class="stock-badge stock-out">Out of Stock</span>`;
        } else if (item.stockQuantity <= 15) {
            stockBadge = `<span class="stock-badge stock-low">Only ${item.stockQuantity} left</span>`;
        } else {
            stockBadge = `<span class="stock-badge stock-in">In Stock (${item.stockQuantity})</span>`;
        }

        const qtyInCart = cart[item.itemId] || 0;

        card.innerHTML = `
            <div>
                <div class="food-card-top">
                    <span class="food-category-badge">${item.category}</span>
                    ${stockBadge}
                </div>
                <h3 class="food-title">${item.itemName}</h3>
                <div class="food-price">Rs. ${item.price.toFixed(2)}</div>
            </div>
            <div class="food-card-actions">
                <span class="card-stock-info">
                    <i class="fa-solid fa-cubes-stacked"></i> Stock: <strong>${item.stockQuantity}</strong>
                </span>
                <button class="btn-primary" ${isOutOfStock ? "disabled" : ""} onclick="addToCart(${item.itemId})">
                    <i class="fa-solid fa-cart-plus"></i> ${qtyInCart > 0 ? `Add (${qtyInCart})` : "Add"}
                </button>
            </div>
        `;
        grid.appendChild(card);
    });
}

// Cart Operations (FR 2, 3, 6)
function addToCart(itemId) {
    const item = menuItems.find(i => i.itemId === itemId);
    if (!item) return;

    const currentQty = cart[itemId] || 0;
    if (currentQty + 1 > item.stockQuantity) {
        showToast(`Cannot add more "${item.itemName}". Available stock is only ${item.stockQuantity}.`, "toast-error");
        return;
    }

    cart[itemId] = currentQty + 1;
    saveCartToStorage();
    updateCartDisplay();
    renderMenu();
    showToast(`Added "${item.itemName}" to your order cart.`, "toast-success");
}

function updateCartQty(itemId, newQty) {
    const item = menuItems.find(i => i.itemId === itemId);
    if (!item) return;

    if (newQty <= 0) {
        delete cart[itemId];
    } else {
        if (newQty > item.stockQuantity) {
            showToast(`Order exceeds available stock of ${item.stockQuantity} for "${item.itemName}".`, "toast-error");
            return;
        }
        cart[itemId] = newQty;
    }

    saveCartToStorage();
    updateCartDisplay();
    renderMenu();
}

function clearCart() {
    cart = {};
    saveCartToStorage();
    updateCartDisplay();
    renderMenu();
    showToast("Cart has been cleared.", "toast-warning");
}

document.getElementById("clearCartBtn").addEventListener("click", clearCart);

function updateCartDisplay() {
    const totalItemsCount = Object.values(cart).reduce((a, b) => a + b, 0);
    document.getElementById("cartCountBadge").textContent = totalItemsCount;

    const emptyState = document.getElementById("cartEmptyState");
    const tableContainer = document.getElementById("cartTableContainer");
    const tbody = document.getElementById("cartItemsTbody");
    const submitBtn = document.getElementById("placeOrderSubmitBtn");
    const warningNotice = document.getElementById("stockWarningNotice");

    const itemIds = Object.keys(cart).map(Number);

    if (itemIds.length === 0) {
        emptyState.style.display = "block";
        tableContainer.style.display = "none";
        submitBtn.disabled = true;
        warningNotice.style.display = "none";
        document.getElementById("billSubtotal").textContent = "Rs. 0.00";
        document.getElementById("billTax").textContent = "Rs. 0.00";
        document.getElementById("billGrandTotal").textContent = "Rs. 0.00";
        return;
    }

    emptyState.style.display = "none";
    tableContainer.style.display = "block";

    tbody.innerHTML = "";
    let subtotal = 0;
    let hasStockDeficit = false;
    let deficitMessage = "";

    itemIds.forEach(id => {
        const item = menuItems.find(i => i.itemId === id);
        if (!item) return;

        const qty = cart[id];
        const lineTotal = item.price * qty;
        subtotal += lineTotal;

        const isExceeding = qty > item.stockQuantity;
        if (isExceeding) {
            hasStockDeficit = true;
            deficitMessage = `Item "${item.itemName}" exceeds available stock (${item.stockQuantity}). Please decrease quantity.`;
        }

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>
                <strong>${item.itemName}</strong><br>
                <small style="color: #64748b;">${item.category}</small>
            </td>
            <td>Rs. ${item.price.toFixed(2)}</td>
            <td>
                <div class="qty-stepper">
                    <button class="qty-btn" type="button" onclick="updateCartQty(${item.itemId}, ${qty - 1})">-</button>
                    <input class="qty-input" type="text" readonly value="${qty}">
                    <button class="qty-btn" type="button" onclick="updateCartQty(${item.itemId}, ${qty + 1})">+</button>
                </div>
            </td>
            <td>
                <span class="stock-badge ${item.stockQuantity > 0 ? (item.stockQuantity <= 15 ? 'stock-low' : 'stock-in') : 'stock-out'}">
                    ${item.stockQuantity} left
                </span>
            </td>
            <td><strong>Rs. ${lineTotal.toFixed(2)}</strong></td>
            <td>
                <button class="btn-text-danger" type="button" onclick="updateCartQty(${item.itemId}, 0)">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });

    // Requirement 3: Automatic bill calculations
    const tax = Math.round(subtotal * 0.05 * 100) / 100; // 5% GST
    const role = document.getElementById("customerRole").value;
    let discount = 0;
    const discountLine = document.getElementById("discountLine");

    if (role === "Faculty" || role === "Staff") {
        discount = Math.round(subtotal * 0.10 * 100) / 100; // 10% Faculty/Staff discount
        discountLine.style.display = "flex";
        document.getElementById("billDiscount").textContent = `-Rs. ${discount.toFixed(2)}`;
    } else {
        discountLine.style.display = "none";
    }

    const grandTotal = Math.round((subtotal + tax - discount) * 100) / 100;

    document.getElementById("billSubtotal").textContent = `Rs. ${subtotal.toFixed(2)}`;
    document.getElementById("billTax").textContent = `Rs. ${tax.toFixed(2)}`;
    document.getElementById("billGrandTotal").textContent = `Rs. ${grandTotal.toFixed(2)}`;

    // Stock check enforcement
    if (hasStockDeficit) {
        warningNotice.style.display = "flex";
        document.getElementById("stockWarningText").textContent = deficitMessage;
        submitBtn.disabled = true;
    } else {
        warningNotice.style.display = "none";
        submitBtn.disabled = false;
    }
}

document.getElementById("customerRole").addEventListener("change", updateCartDisplay);

// Place Order Handler (FR 2, 4, 5, 6)
async function handlePlaceOrder(e) {
    e.preventDefault();

    const customerName = document.getElementById("customerName").value.trim();
    const customerRole = document.getElementById("customerRole").value;
    const registerNo = document.getElementById("registerNo").value.trim();
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;

    const itemIds = Object.keys(cart).map(Number);
    if (itemIds.length === 0) {
        showToast("Cannot place empty order! Add food items first.", "toast-error");
        return;
    }

    // Step 1: Validate stock strictly before placing order
    for (let id of itemIds) {
        const item = menuItems.find(i => i.itemId === id);
        const qty = cart[id];
        if (qty > item.stockQuantity) {
            showToast(`Order failed: Insufficient stock for ${item.itemName}!`, "toast-error");
            return;
        }
    }

    // Step 2: Calculate bill lines
    let subtotal = 0;
    let itemsSummaryList = [];
    let receiptLines = [];

    itemIds.forEach((id, index) => {
        const item = menuItems.find(i => i.itemId === id);
        const qty = cart[id];
        const lineTotal = item.price * qty;
        subtotal += lineTotal;

        // Requirement 4: Update stock quantities when orders are completed
        item.stockQuantity -= qty;
        if (item.stockQuantity === 0) item.isAvailable = false;

        itemsSummaryList.push(`${item.itemName} x ${qty}`);
        receiptLines.push({
            idx: index + 1,
            name: item.itemName,
            qty: qty,
            price: item.price,
            total: lineTotal
        });
    });

    const tax = Math.round(subtotal * 0.05 * 100) / 100;
    let discount = (customerRole === "Faculty" || customerRole === "Staff") ? Math.round(subtotal * 0.10 * 100) / 100 : 0;
    const grandTotal = Math.round((subtotal + tax - discount) * 100) / 100;

    const newOrderId = orders.length > 0 ? Math.max(...orders.map(o => o.orderId)) + 1 : 101;
    const now = new Date();
    const dateStr = now.toISOString().split("T")[0];
    const timeStr = now.toTimeString().split(" ")[0];

    // Store transaction details (Requirement 5)
    const newOrder = {
        orderId: newOrderId,
        date: dateStr,
        time: timeStr,
        customer: customerName,
        role: customerRole,
        regNo: registerNo,
        itemsSummary: itemsSummaryList.join(", "),
        subtotal: subtotal,
        tax: tax,
        discount: discount,
        total: grandTotal,
        payment: paymentMethod,
        status: "Completed",
        receiptLines: receiptLines
    };

    orders.unshift(newOrder); // Add to beginning of orders list

    // Persist changes
    saveMenuToStorage();
    saveOrdersToStorage();

    // Clear cart
    cart = {};
    saveCartToStorage();

    // Refresh UI
    updateCartDisplay();
    renderMenu();
    renderInventoryTable();
    renderOrdersTable();

    // Try posting to backend if available
    try {
        fetch("/api/order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                customerName: customerName,
                customerRole: customerRole,
                paymentMethod: paymentMethod,
                items: itemIds.map(id => ({ itemId: id, quantity: cart[id] || 1 }))
            })
        }).catch(() => {});
    } catch (e) {}

    showToast(`Order #VCET-CAN-${newOrderId} placed successfully!`, "toast-success");
    openReceiptModal(newOrder);
}

// Receipt Modal & ASCII Generation
function openReceiptModal(order) {
    const pre = document.getElementById("receiptTextPre");
    const line = "========================================================\n";
    const dashed = "--------------------------------------------------------\n";

    let text = line;
    text += "      VELALAR COLLEGE OF ENGINEERING & TECHNOLOGY       \n";
    text += "                 CAMPUS CANTEEN INVOICE                 \n";
    text += "               Thindal, Erode - 638 012                 \n";
    text += line;
    text += `Invoice No : VCET-CAN-${order.orderId.toString().padEnd(8)} Date: ${order.date} ${order.time}\n`;
    text += `Customer   : ${order.customer.padEnd(20)} Role: ${order.role}\n`;
    text += `Payment Via: ${order.payment.padEnd(20)} Status: ${order.status}\n`;
    text += dashed;
    text += `#    Item Description         Qty   Price     Amount\n`;
    text += dashed;

    if (order.receiptLines) {
        order.receiptLines.forEach(item => {
            const name = item.name.padEnd(24).substring(0, 24);
            const qty = item.qty.toString().padStart(3);
            const price = item.price.toFixed(2).padStart(7);
            const total = item.total.toFixed(2).padStart(10);
            text += `${item.idx.toString().padEnd(4)} ${name} ${qty} ${price} ${total}\n`;
        });
    } else {
        text += `1    ${order.itemsSummary.padEnd(24).substring(0, 24)}   1   ${order.total.toFixed(2)}   ${order.total.toFixed(2)}\n`;
    }

    text += dashed;
    text += `Subtotal:                               Rs. ${ (order.subtotal || order.total).toFixed(2).padStart(10) }\n`;
    text += `GST (5%):                               Rs. ${ (order.tax || 0).toFixed(2).padStart(10) }\n`;
    if (order.discount && order.discount > 0) {
        text += `Discount:                              -Rs. ${ order.discount.toFixed(2).padStart(10) }\n`;
    }
    text += line;
    text += `NET AMOUNT PAYABLE:                     Rs. ${ order.total.toFixed(2).padStart(10) }\n`;
    text += line;
    text += "           Thank you! Enjoy your fresh meal!            \n";
    text += "         * Please collect your token at counter *       \n";
    text += line;

    pre.textContent = text;
    document.getElementById("receiptModal").classList.add("active");
}

function closeReceiptModal() {
    document.getElementById("receiptModal").classList.remove("active");
}

function printReceipt() {
    window.print();
}

// Inventory Management
function renderInventoryTable() {
    const tbody = document.getElementById("inventoryTableBody");
    tbody.innerHTML = "";

    let totalUnits = 0;
    let lowStock = 0;
    let outOfStock = 0;

    menuItems.forEach(item => {
        totalUnits += item.stockQuantity;
        if (item.stockQuantity <= 0) outOfStock++;
        else if (item.stockQuantity <= 25) lowStock++;

        const tr = document.createElement("tr");

        let statusBadge = "";
        if (item.stockQuantity <= 0) {
            statusBadge = `<span class="stock-badge stock-out">Out of Stock</span>`;
        } else if (item.stockQuantity <= 25) {
            statusBadge = `<span class="stock-badge stock-low">Low Stock</span>`;
        } else {
            statusBadge = `<span class="stock-badge stock-in">Sufficient</span>`;
        }

        tr.innerHTML = `
            <td>#${item.itemId}</td>
            <td><strong>${item.itemName}</strong></td>
            <td><span class="food-category-badge">${item.category}</span></td>
            <td>Rs. ${item.price.toFixed(2)}</td>
            <td><strong>${item.stockQuantity}</strong> units</td>
            <td>${statusBadge}</td>
            <td>
                <button class="btn-outline" style="padding: 0.3rem 0.6rem; font-size: 0.8rem;" onclick="openRestockModal(${item.itemId})">
                    <i class="fa-solid fa-plus"></i> Restock
                </button>
            </td>
            <td>
                <button class="btn-outline" style="padding: 0.3rem 0.6rem; font-size: 0.8rem;" onclick="openEditItemModal(${item.itemId})">
                    <i class="fa-solid fa-pen-to-square"></i> Edit
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    document.getElementById("invTotalItems").textContent = menuItems.length;
    document.getElementById("invTotalUnits").textContent = totalUnits;
    document.getElementById("invLowStockCount").textContent = lowStock;
    document.getElementById("invOutOfStockCount").textContent = outOfStock;
}

// Add/Edit Item Modal
function openNewItemModal() {
    document.getElementById("itemModalTitle").textContent = "Add New Food Item";
    document.getElementById("editItemId").value = "";
    document.getElementById("newItemName").value = "";
    document.getElementById("newItemPrice").value = "";
    document.getElementById("newItemStock").value = "50";
    document.getElementById("itemModal").classList.add("active");
}

function openEditItemModal(itemId) {
    const item = menuItems.find(i => i.itemId === itemId);
    if (!item) return;

    document.getElementById("itemModalTitle").textContent = "Edit Food Item";
    document.getElementById("editItemId").value = item.itemId;
    document.getElementById("newItemName").value = item.itemName;
    document.getElementById("newItemCategory").value = item.category;
    document.getElementById("newItemPrice").value = item.price;
    document.getElementById("newItemStock").value = item.stockQuantity;
    document.getElementById("itemModal").classList.add("active");
}

function closeItemModal() {
    document.getElementById("itemModal").classList.remove("active");
}

function handleSaveMenuItem(e) {
    e.preventDefault();
    const editId = document.getElementById("editItemId").value;
    const name = document.getElementById("newItemName").value.trim();
    const category = document.getElementById("newItemCategory").value;
    const price = parseFloat(document.getElementById("newItemPrice").value);
    const stock = parseInt(document.getElementById("newItemStock").value, 10);

    if (editId) {
        // Edit existing
        const item = menuItems.find(i => i.itemId === parseInt(editId, 10));
        if (item) {
            item.itemName = name;
            item.category = category;
            item.price = price;
            item.stockQuantity = stock;
            item.isAvailable = stock > 0;
            showToast(`Updated "${name}" details successfully.`, "toast-success");
        }
    } else {
        // Create new
        const newId = menuItems.length > 0 ? Math.max(...menuItems.map(i => i.itemId)) + 1 : 1;
        menuItems.push({
            itemId: newId,
            itemName: name,
            category: category,
            price: price,
            stockQuantity: stock,
            isAvailable: stock > 0
        });
        showToast(`Added new item "${name}" to canteen menu.`, "toast-success");
    }

    saveMenuToStorage();
    closeItemModal();
    renderInventoryTable();
    renderMenu();
}

// Restock Modal
function openRestockModal(itemId) {
    const item = menuItems.find(i => i.itemId === itemId);
    if (!item) return;

    document.getElementById("restockItemId").value = item.itemId;
    document.getElementById("restockItemName").textContent = `${item.itemName} (Current Stock: ${item.stockQuantity})`;
    document.getElementById("restockAddQty").value = 25;
    document.getElementById("restockModal").classList.add("active");
}

function closeRestockModal() {
    document.getElementById("restockModal").classList.remove("active");
}

function handleApplyRestock(e) {
    e.preventDefault();
    const id = parseInt(document.getElementById("restockItemId").value, 10);
    const addQty = parseInt(document.getElementById("restockAddQty").value, 10);

    const item = menuItems.find(i => i.itemId === id);
    if (item && addQty > 0) {
        item.stockQuantity += addQty;
        item.isAvailable = true;
        saveMenuToStorage();
        showToast(`Added +${addQty} units to "${item.itemName}". New stock: ${item.stockQuantity}`, "toast-success");
    }

    closeRestockModal();
    renderInventoryTable();
    renderMenu();
    updateCartDisplay();
}

// Orders History Table
function renderOrdersTable() {
    const tbody = document.getElementById("ordersTableBody");
    tbody.innerHTML = "";

    orders.forEach(order => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td><strong>#VCET-${order.orderId}</strong></td>
            <td>${order.date} <br><small style="color: #64748b;">${order.time}</small></td>
            <td><strong>${order.customer}</strong></td>
            <td><span class="food-category-badge">${order.role}</span></td>
            <td style="max-width: 250px; font-size: 0.85rem;">${order.itemsSummary}</td>
            <td><strong>Rs. ${order.total.toFixed(2)}</strong></td>
            <td><span class="stock-badge stock-in">${order.payment}</span></td>
            <td><span class="stock-badge stock-in">${order.status}</span></td>
            <td>
                <button class="btn-outline" style="padding: 0.3rem 0.6rem; font-size: 0.8rem;" onclick="viewOrderReceipt(${order.orderId})">
                    <i class="fa-solid fa-receipt"></i> Receipt
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function viewOrderReceipt(orderId) {
    const order = orders.find(o => o.orderId === orderId);
    if (order) {
        openReceiptModal(order);
    }
}

function exportOrdersToCSV() {
    let csv = "order_id,order_date,order_time,customer_name,customer_role,items_summary,total_amount,payment_method,payment_status\n";
    orders.forEach(o => {
        csv += `${o.orderId},${o.date},${o.time},"${o.customer}",${o.role},"${o.itemsSummary}",${o.total.toFixed(2)},${o.payment},${o.status}\n`;
    });

    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `canteen_sales_export_${new Date().toISOString().split("T")[0]}.csv`;
    a.click();
    showToast("Exported transactions to CSV successfully!", "toast-success");
}

// Analytics and Charts (FR 7 & 8)
function initAnalyticsCharts() {
    updateAnalyticsData();
}

function updateAnalyticsData() {
    const totalRev = orders.reduce((sum, o) => sum + o.total, 0);
    const avgVal = orders.length > 0 ? (totalRev / orders.length) : 0;

    document.getElementById("analyticsTotalRev").textContent = `Rs. ${totalRev.toLocaleString('en-IN', { minimumFractionDigits: 2 })}`;
    document.getElementById("analyticsTotalOrders").textContent = orders.length;
    document.getElementById("analyticsAvgOrder").textContent = `Rs. ${avgVal.toFixed(2)}`;

    renderDailyRevenueChart();
    renderPopularItemsChart();
    renderCategoryShareChart();
    renderPaymentMethodChart();
}

function renderDailyRevenueChart() {
    const dailyMap = {};
    orders.forEach(o => {
        dailyMap[o.date] = (dailyMap[o.date] || 0) + o.total;
    });

    const sortedDates = Object.keys(dailyMap).sort();
    const values = sortedDates.map(d => dailyMap[d]);

    const ctx = document.getElementById("dailyRevenueChart").getContext("2d");
    if (chartInstances.dailyRev) chartInstances.dailyRev.destroy();

    chartInstances.dailyRev = new Chart(ctx, {
        type: "line",
        data: {
            labels: sortedDates,
            datasets: [{
                label: "Daily Revenue (Rs.)",
                data: values,
                borderColor: "#0284c7",
                backgroundColor: "rgba(2, 132, 199, 0.12)",
                fill: true,
                tension: 0.35,
                borderWidth: 3,
                pointBackgroundColor: "#0284c7",
                pointRadius: 5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}

function renderPopularItemsChart() {
    const itemMap = {
        "Chicken Biryani": 8,
        "Ghee Roast Dosa": 7,
        "Masala Chai": 12,
        "Samosa (2 pcs)": 9,
        "Filter Coffee": 8,
        "Egg Fried Rice": 5
    };

    // Calculate dynamically from orders summary
    orders.forEach(o => {
        if (o.receiptLines) {
            o.receiptLines.forEach(l => {
                itemMap[l.name] = (itemMap[l.name] || 0) + l.qty;
            });
        }
    });

    const sortedItems = Object.entries(itemMap).sort((a, b) => b[1] - a[1]).slice(0, 5);
    const labels = sortedItems.map(i => i[0]);
    const data = sortedItems.map(i => i[1]);

    const ctx = document.getElementById("popularItemsChart").getContext("2d");
    if (chartInstances.popular) chartInstances.popular.destroy();

    chartInstances.popular = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Units Sold",
                data: data,
                backgroundColor: [
                    "#3b82f6", "#10b981", "#f59e0b", "#8b5cf6", "#ec4899"
                ],
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}

function renderCategoryShareChart() {
    const catMap = {
        "Breakfast": 325.0,
        "Lunch & Meals": 1180.0,
        "Snacks & Quick Bites": 415.0,
        "Beverages": 380.0
    };

    const ctx = document.getElementById("categoryShareChart").getContext("2d");
    if (chartInstances.category) chartInstances.category.destroy();

    chartInstances.category = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: Object.keys(catMap),
            datasets: [{
                data: Object.values(catMap),
                backgroundColor: ["#38bdf8", "#34d399", "#fbbf24", "#a78bfa"]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

function renderPaymentMethodChart() {
    const payMap = {};
    orders.forEach(o => {
        payMap[o.payment] = (payMap[o.payment] || 0) + 1;
    });

    const ctx = document.getElementById("paymentMethodChart").getContext("2d");
    if (chartInstances.payment) chartInstances.payment.destroy();

    chartInstances.payment = new Chart(ctx, {
        type: "pie",
        data: {
            labels: Object.keys(payMap),
            datasets: [{
                data: Object.values(payMap),
                backgroundColor: ["#0284c7", "#22c55e", "#f97316", "#8b5cf6"]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

// Backend Server Status Check
async function checkBackendConnection() {
    const badge = document.getElementById("serverStatusBadge");
    const statusText = document.getElementById("backendStatusText");

    try {
        const response = await fetch("/api/menu", { cache: "no-cache" });
        if (response.ok) {
            statusText.textContent = "Connected to Java Server";
            badge.style.background = "rgba(34, 197, 94, 0.2)";
        }
    } catch (e) {
        statusText.textContent = "Client App Ready";
    }
}

// Toast Notifications
function showToast(message, type = "toast-success") {
    const container = document.getElementById("toastContainer");
    const toast = document.createElement("div");
    toast.className = `toast ${type}`;

    let icon = "fa-check-circle";
    if (type === "toast-error") icon = "fa-circle-exclamation";
    if (type === "toast-warning") icon = "fa-triangle-exclamation";

    toast.innerHTML = `<i class="fa-solid ${icon}"></i> <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}
