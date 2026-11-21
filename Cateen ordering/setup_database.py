"""
setup_database.py
Utility script to create and populate the SQLite database (canteen_db.sqlite)
directly from schema.sql and seed_data.sql using standard Python sqlite3 library.
"""
import os
import sqlite3

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DB_PATH = os.path.join(BASE_DIR, "canteen_db.sqlite")
SCHEMA_PATH = os.path.join(BASE_DIR, "schema.sql")
SEED_PATH = os.path.join(BASE_DIR, "seed_data.sql")

def init_db():
    print(f">> Initializing SQLite database at: {DB_PATH}")
    if os.path.exists(DB_PATH):
        try:
            os.remove(DB_PATH)
            print(">> Cleaned previous database file.")
        except Exception as e:
            print(f">> Note: Could not delete existing DB: {e}")

    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    # Read and execute schema.sql
    if os.path.exists(SCHEMA_PATH):
        with open(SCHEMA_PATH, "r", encoding="utf-8") as f:
            schema_sql = f.read()
        cursor.executescript(schema_sql)
        print(">> Successfully executed schema.sql (Tables, Indexes, Views created).")
    else:
        print("[ERROR] schema.sql not found!")

    # Read and execute seed_data.sql
    if os.path.exists(SEED_PATH):
        with open(SEED_PATH, "r", encoding="utf-8") as f:
            seed_sql = f.read()
        cursor.executescript(seed_sql)
        print(">> Successfully executed seed_data.sql (Seed data inserted).")
    else:
        print("[ERROR] seed_data.sql not found!")

    conn.commit()

    # Verify counts
    cursor.execute("SELECT COUNT(*) FROM food_items;")
    items_count = cursor.fetchone()[0]
    cursor.execute("SELECT COUNT(*) FROM orders;")
    orders_count = cursor.fetchone()[0]
    cursor.execute("SELECT COUNT(*) FROM customers;")
    customers_count = cursor.fetchone()[0]

    print(f"\n=== Database Verification Summary ===")
    print(f"Total Menu Items Loaded : {items_count}")
    print(f"Total Orders Seeded     : {orders_count}")
    print(f"Total Customers Seeded  : {customers_count}")
    print("=====================================")

    conn.close()
    print(">> Database setup complete! File saved as: canteen_db.sqlite\n")

if __name__ == "__main__":
    init_db()
