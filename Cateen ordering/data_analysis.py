"""
====================================================================
VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (VCET), ERODE - 12
DEPARTMENT OF INFORMATION TECHNOLOGY | CLASS: 25IT3A&B
SCENARIO 3: COLLEGE CANTEEN ORDERING AND SALES ANALYSIS SYSTEM
Subject: 25ITC31 - Foundations of Data Science
====================================================================

Python Data Science Pipeline:
- FR 7: Retrieve sales data from database/CSV and preprocess it using Python.
- FR 8: Visualize item-wise sales, daily revenue, and popular food items.
====================================================================
"""

import os
import sqlite3
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

# Set publication style for scientific charts
plt.style.use('seaborn-v0_8-whitegrid' if 'seaborn-v0_8-whitegrid' in plt.style.available else 'default')
plt.rcParams['font.sans-serif'] = 'Arial'
plt.rcParams['font.size'] = 10
plt.rcParams['figure.autolayout'] = True

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
CSV_PATH = os.path.join(BASE_DIR, 'canteen_sales_data.csv')
DB_PATH = os.path.join(BASE_DIR, 'canteen_db.sqlite')


def step1_data_retrieval():
    """
    FR 7: Retrieve sales dataset from SQLite database or fallback CSV.
    """
    print("\n" + "="*60)
    print(" [STEP 1] RETRIEVING CANTEEN SALES DATASET (FR 7)")
    print("="*60)

    df = None
    if os.path.exists(DB_PATH):
        try:
            conn = sqlite3.connect(DB_PATH)
            query = """
                SELECT 
                    o.order_id,
                    o.order_date,
                    o.order_time,
                    c.customer_id,
                    c.customer_name,
                    c.role AS customer_role,
                    f.item_id,
                    f.item_name,
                    cat.category_name AS category,
                    oi.unit_price,
                    oi.quantity,
                    oi.line_total,
                    p.payment_method,
                    p.payment_status
                FROM orders o
                JOIN customers c ON o.customer_id = c.customer_id
                JOIN order_items oi ON o.order_id = oi.order_id
                JOIN food_items f ON oi.item_id = f.item_id
                JOIN categories cat ON f.category_id = cat.category_id
                LEFT JOIN payments p ON o.order_id = p.order_id
            """
            df = pd.read_sql_query(query, conn)
            conn.close()
            print(">> Successfully retrieved dataset from SQLite database.")
        except Exception as e:
            print(f">> Note: Database query fallback to CSV due to: {e}")

    if df is None:
        if os.path.exists(CSV_PATH):
            df = pd.read_csv(CSV_PATH)
            print(f">> Successfully loaded dataset from CSV: {CSV_PATH}")
        else:
            raise FileNotFoundError("Neither SQLite DB nor CSV file found!")

    print(f">> Total Records Loaded: {len(df)}")
    print(f">> Columns: {list(df.columns)}")
    return df


def step2_data_cleaning_and_preprocessing(df):
    """
    Course Syllabus Alignment: 25ITC31 Unit 4 (Data Cleaning & Transformation)
    - Handling missing values
    - Duplicate detection & removal
    - Data type conversions
    - Feature engineering (Total order value, Hour of day, Day of week)
    """
    print("\n" + "="*60)
    print(" [STEP 2] DATA CLEANING & PREPROCESSING (25ITC31 UNIT 4)")
    print("="*60)

    # 1. Inspect initial shape and data types
    print(f">> Initial Shape: {df.shape}")

    # 2. Check for duplicate records
    duplicate_count = df.duplicated().sum()
    print(f">> Duplicate rows identified: {duplicate_count}")
    if duplicate_count > 0:
        df = df.drop_duplicates()
        print(f">> Cleaned duplicates. New shape: {df.shape}")

    # 3. Check for missing / null values
    missing_summary = df.isnull().sum()
    print(">> Missing values per column:")
    for col, count in missing_summary.items():
        if count > 0:
            print(f"   - {col}: {count} missing")
    if missing_summary.sum() == 0:
        print("   - No missing values found (Dataset is complete and clean).")

    # 4. Data Type Conversions
    df['order_date'] = pd.to_datetime(df['order_date'])
    df['unit_price'] = df['unit_price'].astype(float)
    df['quantity'] = df['quantity'].astype(int)
    df['line_total'] = df['line_total'].astype(float)

    # 5. Feature Engineering
    df['day_name'] = df['order_date'].dt.day_name()
    df['hour'] = pd.to_datetime(df['order_time'], format='%H:%M:%S').dt.hour

    print(">> Preprocessing complete. Derived features: 'day_name', 'hour'.")
    return df


def step3_descriptive_statistics(df):
    """
    Course Syllabus Alignment: 25ITC31 Unit 1 & 5 (Descriptive Statistics & Summary)
    """
    print("\n" + "="*60)
    print(" [STEP 3] EXPLORATORY DATA ANALYSIS & SUMMARY STATISTICS")
    print("="*60)

    total_revenue = df['line_total'].sum()
    total_items_sold = df['quantity'].sum()
    unique_orders = df['order_id'].nunique()
    avg_order_value = total_revenue / unique_orders if unique_orders > 0 else 0

    print(f"1. Total Gross Sales Value : Rs. {total_revenue:,.2f}")
    print(f"2. Total Food Units Sold   : {total_items_sold} items")
    print(f"3. Unique Orders Processed : {unique_orders} orders")
    print(f"4. Average Order Value     : Rs. {avg_order_value:.2f}")

    print("\n--- Summary Statistics (Numerical Features) ---")
    print(df[['unit_price', 'quantity', 'line_total']].describe().round(2))

    print("\n--- Sales Distribution by Customer Role ---")
    role_summary = df.groupby('customer_role').agg(
        total_orders=('order_id', 'nunique'),
        units_purchased=('quantity', 'sum'),
        total_spent=('line_total', 'sum')
    ).round(2)
    print(role_summary)

    print("\n--- Top 5 Food Items by Units Sold ---")
    top_items = df.groupby('item_name')['quantity'].sum().sort_values(ascending=False).head(5)
    for idx, (name, qty) in enumerate(top_items.items(), 1):
        print(f"   {idx}. {name:<26} : {qty} units")

    return total_revenue, unique_orders


def step4_generate_visualizations(df):
    """
    FR 8: Visualize item-wise sales, daily revenue, and popular food items.
    Generates 4 high-resolution academic charts.
    """
    print("\n" + "="*60)
    print(" [STEP 4] GENERATING DATA VISUALIZATIONS (FR 8)")
    print("="*60)

    # Figure 1: Item-wise Sales Quantity
    plt.figure(figsize=(10, 6))
    item_sales = df.groupby('item_name')['quantity'].sum().sort_values(ascending=True)
    colors = sns.color_palette("viridis", len(item_sales))
    bars = plt.barh(item_sales.index, item_sales.values, color=colors)
    plt.title('Canteen Item-wise Total Sales Volume (Units Sold)', fontsize=14, fontweight='bold', pad=15)
    plt.xlabel('Quantity Sold', fontsize=12)
    plt.ylabel('Food Item Name', fontsize=12)
    for bar in bars:
        width = bar.get_width()
        plt.text(width + 0.3, bar.get_y() + bar.get_height()/2, f'{int(width)}', va='center', fontsize=9)
    plt.tight_layout()
    chart1_path = os.path.join(BASE_DIR, 'chart_item_wise_sales.png')
    plt.savefig(chart1_path, dpi=200)
    plt.close()
    print(f">> Saved Chart 1: {chart1_path}")

    # Figure 2: Daily Revenue Trend
    plt.figure(figsize=(9, 5))
    daily_rev = df.groupby(df['order_date'].dt.strftime('%Y-%m-%d'))['line_total'].sum()
    plt.plot(daily_rev.index, daily_rev.values, marker='o', color='#0284c7', linewidth=2.5, markersize=7)
    plt.title('Daily Revenue Trend (Rs.) - Campus Canteen', fontsize=14, fontweight='bold', pad=15)
    plt.xlabel('Order Date', fontsize=12)
    plt.ylabel('Total Revenue (Rs.)', fontsize=12)
    plt.xticks(rotation=30)
    for x, y in zip(daily_rev.index, daily_rev.values):
        plt.annotate(f'Rs.{int(y)}', (x, y), textcoords="offset points", xytext=(0, 8), ha='center', fontweight='bold', fontsize=9)
    plt.tight_layout()
    chart2_path = os.path.join(BASE_DIR, 'chart_daily_revenue.png')
    plt.savefig(chart2_path, dpi=200)
    plt.close()
    print(f">> Saved Chart 2: {chart2_path}")

    # Figure 3: Popular Food Items (Top 5 Bar Chart)
    plt.figure(figsize=(8, 5))
    top5 = df.groupby('item_name')['quantity'].sum().sort_values(ascending=False).head(5)
    sns.barplot(x=top5.values, y=top5.index, palette="mako")
    plt.title('Top 5 Most Popular Food Items', fontsize=14, fontweight='bold', pad=15)
    plt.xlabel('Total Units Sold', fontsize=12)
    plt.ylabel('Food Item', fontsize=12)
    for idx, val in enumerate(top5.values):
        plt.text(val + 0.2, idx, f'{val} units', va='center', fontweight='bold', fontsize=10)
    plt.tight_layout()
    chart3_path = os.path.join(BASE_DIR, 'chart_popular_food_items.png')
    plt.savefig(chart3_path, dpi=200)
    plt.close()
    print(f">> Saved Chart 3: {chart3_path}")

    # Figure 4: Category-wise Revenue Contribution (Pie Chart)
    plt.figure(figsize=(7, 6))
    cat_revenue = df.groupby('category')['line_total'].sum()
    colors = ['#38bdf8', '#34d399', '#fbbf24', '#a78bfa']
    plt.pie(cat_revenue, labels=cat_revenue.index, autopct='%1.1f%%', startangle=140, colors=colors,
            wedgeprops={'edgecolor': 'white', 'linewidth': 2})
    plt.title('Revenue Contribution by Food Category', fontsize=14, fontweight='bold', pad=15)
    plt.tight_layout()
    chart4_path = os.path.join(BASE_DIR, 'chart_category_revenue.png')
    plt.savefig(chart4_path, dpi=200)
    plt.close()
    print(f">> Saved Chart 4: {chart4_path}")

    print(">> All visual charts successfully generated and saved to current folder!")


def main():
    print("==================================================================")
    print(" VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (VCET), ERODE      ")
    print(" CANTEEN ORDERING AND SALES ANALYSIS SYSTEM - PYTHON DATA SCIENCE ")
    print(" Scenario 3 | Course: 25ITC31 - Foundations of Data Science       ")
    print("==================================================================")

    # FR 7: Retrieve dataset
    df = step1_data_retrieval()

    # Preprocess & Clean
    cleaned_df = step2_data_cleaning_and_preprocessing(df)

    # Descriptive Statistics & Insights
    step3_descriptive_statistics(cleaned_df)

    # FR 8: Visualizations
    try:
        step4_generate_visualizations(cleaned_df)
    except Exception as e:
        print(f">> Note on chart generation: {e}")

    print("\n[SUCCESS] Python Data Science Pipeline completed successfully.\n")


if __name__ == '__main__':
    main()
