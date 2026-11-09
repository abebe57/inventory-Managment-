package org.example.inventorysysteamvo1.service.dto.response.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.inventorysysteamvo1.service.ProductService;
import org.example.inventorysysteamvo1.service.dto.ProductRequestDTO;
import org.example.inventorysysteamvo1.service.dto.ProductUpdateRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StockAdjustmentRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.ProductResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.StockAdjustmentResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.StockReceiptResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product variants and stock management")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a product variant with its opening stock")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(requestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product variant and current stock")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{id}/stock-receipts")
    @Operation(summary = "Get the stock receipt history for a product variant")
    public ResponseEntity<List<StockReceiptResponseDTO>> getStockReceipts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getStockReceipts(id));
    }

    @GetMapping("/name/{productName}")
    @Operation(summary = "Find all product variants with the same product name")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByName(@PathVariable String productName) {
        return ResponseEntity.ok(productService.getProductsByName(productName));
    }

    @GetMapping("/name/{productName}/unit/{productUnit}")
    @Operation(summary = "Find one product variant by name and unit")
    public ResponseEntity<ProductResponseDTO> getProductByNameAndUnit(
            @PathVariable String productName,
            @PathVariable String productUnit) {
        return ResponseEntity.ok(productService.getProductByNameAndUnit(productName, productUnit));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "List product variants in a category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId));
    }

    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "List product variants from a supplier")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(productService.getProductsBySupplierId(supplierId));
    }

    @GetMapping
    @Operation(summary = "Get all product variants and current stock")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/{id}/stock")
    @Operation(summary = "Receive another stock batch for an existing product variant")
    public ResponseEntity<StockAdjustmentResponseDTO> addStock(
            @PathVariable Long id,
            @Valid @RequestBody StockAdjustmentRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addStock(id, requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit product master data only; stock is not changed")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a product variant and its stock receipt history")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
