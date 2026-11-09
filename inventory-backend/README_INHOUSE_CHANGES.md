# Backend changes

- Product unit is required.
- Product uniqueness is now `productName + productUnit` so Water/500ml, Water/1L and Water/2L can coexist.
- Added product variant lookup by name + unit.
- Added stock receiving endpoint `POST /api/products/{id}/stock`.
- Orders are internal stock-out records.
- Orders contain date, notes and product/quantity lines only.
- Unit price is no longer entered in an order.
- Bill number, discount, payment and customer are removed from the order workflow.
- Creating an order reduces product stock.
- Increasing/decreasing an existing order quantity adjusts stock accordingly.
- Removing an order item returns its quantity to stock.
- Deleting an order returns all its quantities to stock.
- Added CORS for IntelliJ/Brave frontend origins on ports 63342 and 63343.
