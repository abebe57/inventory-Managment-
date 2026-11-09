-- V3: Stock receipt history and changing purchase prices.
-- Recommended for an existing inventory001 database.
-- Spring Boot with spring.jpa.hibernate.ddl-auto=update will also create this table automatically.

USE inventory001;

CREATE TABLE IF NOT EXISTS Stock_Receipt (
    Stock_Receipt_ID BIGINT NOT NULL AUTO_INCREMENT,
    Product_ID BIGINT NOT NULL,
    Supplier_ID BIGINT NOT NULL,
    Received_Date DATE NOT NULL,
    Quantity INT NOT NULL,
    Unit_Purchase_Price DECIMAL(12,2) NOT NULL,
    Total_Cost DECIMAL(14,2) NOT NULL,
    Notes VARCHAR(500),
    PRIMARY KEY (Stock_Receipt_ID),
    INDEX idx_stock_receipt_product (Product_ID),
    INDEX idx_stock_receipt_supplier (Supplier_ID),
    INDEX idx_stock_receipt_date (Received_Date)
);
