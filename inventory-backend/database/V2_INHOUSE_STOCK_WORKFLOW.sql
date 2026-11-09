-- Run this once against the existing `inventory` database after backing it up.
-- This removes sales/payment-only columns from the order tables.

USE inventory;

ALTER TABLE Orders
    DROP COLUMN Customer_ID;

ALTER TABLE Order_Detail
    DROP COLUMN Unit_Price,
    DROP COLUMN Size,
    DROP COLUMN Discount,
    DROP COLUMN Total,
    DROP COLUMN Bill_number;

-- Payment is no longer part of the in-house inventory workflow.
DROP TABLE IF EXISTS payment;
