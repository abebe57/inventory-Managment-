package org.example.inventorysysteamvo1.service;

import org.example.inventorysysteamvo1.service.dto.ProductRequestDTO;
import org.example.inventorysysteamvo1.service.dto.ProductUpdateRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StockAdjustmentRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.ProductResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.StockAdjustmentResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.StockReceiptResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
    ProductResponseDTO updateProduct(Long productId, ProductUpdateRequestDTO requestDTO);
    ProductResponseDTO getProductById(Long productId);
    List<ProductResponseDTO> getProductsByName(String productName);
    ProductResponseDTO getProductByNameAndUnit(String productName, String productUnit);
    List<ProductResponseDTO> getProductsByCategoryId(Long categoryId);
    List<ProductResponseDTO> getProductsBySupplierId(Long supplierId);
    List<ProductResponseDTO> getAllProducts();
    StockAdjustmentResponseDTO addStock(Long productId, StockAdjustmentRequestDTO requestDTO);
    List<StockReceiptResponseDTO> getStockReceipts(Long productId);
    void deleteProduct(Long productId);
}
