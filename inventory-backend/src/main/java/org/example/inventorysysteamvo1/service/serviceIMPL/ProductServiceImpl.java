package org.example.inventorysysteamvo1.service.serviceIMPL;

import jakarta.transaction.Transactional;
import org.example.inventorysysteamvo1.domain.Product;
import org.example.inventorysysteamvo1.domain.Repo.CategoryRepository;
import org.example.inventorysysteamvo1.domain.Repo.ProductRepository;
import org.example.inventorysysteamvo1.domain.Repo.StockReceiptRepository;
import org.example.inventorysysteamvo1.domain.Repo.SupplierRepository;
import org.example.inventorysysteamvo1.domain.StockReceipt;
import org.example.inventorysysteamvo1.service.ProductService;
import org.example.inventorysysteamvo1.service.dto.ProductRequestDTO;
import org.example.inventorysysteamvo1.service.dto.ProductUpdateRequestDTO;
import org.example.inventorysysteamvo1.service.dto.StockAdjustmentRequestDTO;
import org.example.inventorysysteamvo1.service.dto.response.ProductResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.StockAdjustmentResponseDTO;
import org.example.inventorysysteamvo1.service.dto.response.StockReceiptResponseDTO;
import org.example.inventorysysteamvo1.service.mapper.ProductDtoMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final StockReceiptRepository stockReceiptRepository;
    private final ProductDtoMapper dtoMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            StockReceiptRepository stockReceiptRepository,
            ProductDtoMapper dtoMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.stockReceiptRepository = stockReceiptRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        if (!categoryRepository.existsById(requestDTO.getCategoryId())) {
            throw new IllegalArgumentException("Category with ID " + requestDTO.getCategoryId() + " does not exist.");
        }
        if (!supplierRepository.existsById(requestDTO.getSupplierId())) {
            throw new IllegalArgumentException("Supplier with ID " + requestDTO.getSupplierId() + " does not exist.");
        }

        if (productRepository.existsByProductNameAndProductUnit(
                requestDTO.getProductName(), requestDTO.getProductUnit())) {
            throw new IllegalArgumentException(
                    "Product '" + requestDTO.getProductName() + "' with unit '" +
                            requestDTO.getProductUnit() + "' already exists. Use Receive Stock to add more quantity to it.");
        }

        Product saved = productRepository.save(dtoMapper.toDomain(requestDTO));

        StockReceipt initialReceipt = new StockReceipt(
                null,
                saved.getProductId(),
                saved.getSupplierId(),
                requestDTO.getReceivedDate(),
                requestDTO.getProductQuantity(),
                requestDTO.getProductPrice(),
                "Initial stock"
        );
        stockReceiptRepository.save(initialReceipt);

        return dtoMapper.toResponse(saved);
    }

    @Override
    public ProductResponseDTO updateProduct(Long productId, ProductUpdateRequestDTO requestDTO) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found."));

        boolean identityChanged =
                !existing.getProductName().equalsIgnoreCase(requestDTO.getProductName()) ||
                !existing.getProductUnit().equalsIgnoreCase(requestDTO.getProductUnit());

        if (identityChanged && productRepository.existsByProductNameAndProductUnit(
                requestDTO.getProductName(), requestDTO.getProductUnit())) {
            throw new IllegalArgumentException(
                    "Product '" + requestDTO.getProductName() + "' with unit '" +
                            requestDTO.getProductUnit() + "' already exists.");
        }

        if (!categoryRepository.existsById(requestDTO.getCategoryId())) {
            throw new IllegalArgumentException("Category with ID " + requestDTO.getCategoryId() + " does not exist.");
        }
        if (!supplierRepository.existsById(requestDTO.getSupplierId())) {
            throw new IllegalArgumentException("Supplier with ID " + requestDTO.getSupplierId() + " does not exist.");
        }

        existing.updateProductDetails(
                requestDTO.getProductName(),
                requestDTO.getProductDescription(),
                requestDTO.getProductUnit(),
                requestDTO.getOtherDetails(),
                requestDTO.getSupplierId(),
                requestDTO.getCategoryId()
        );

        return dtoMapper.toResponse(productRepository.save(existing));
    }

    @Override
    public ProductResponseDTO getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(dtoMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found."));
    }

    @Override
    public List<ProductResponseDTO> getProductsByName(String productName) {
        List<ProductResponseDTO> products = productRepository.findByProductName(productName)
                .stream().map(dtoMapper::toResponse).toList();
        if (products.isEmpty()) {
            throw new IllegalArgumentException("No product variants found for name " + productName + ".");
        }
        return products;
    }

    @Override
    public ProductResponseDTO getProductByNameAndUnit(String productName, String productUnit) {
        return productRepository.findByProductNameAndProductUnit(productName, productUnit)
                .map(dtoMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product '" + productName + "' with unit '" + productUnit + "' not found."));
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream().map(dtoMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsBySupplierId(Long supplierId) {
        return productRepository.findBySupplierId(supplierId)
                .stream().map(dtoMapper::toResponse).toList();
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream().map(dtoMapper::toResponse).toList();
    }

    @Override
    public StockAdjustmentResponseDTO addStock(Long productId, StockAdjustmentRequestDTO requestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found."));

        int previous = product.getProductQuantity();
        BigDecimal previousAverageCost = product.getProductPrice();

        product.receiveStock(requestDTO.getQuantity(), requestDTO.getUnitPurchasePrice());
        Product saved = productRepository.save(product);

        StockReceipt receipt = new StockReceipt(
                null,
                saved.getProductId(),
                saved.getSupplierId(),
                requestDTO.getReceivedDate(),
                requestDTO.getQuantity(),
                requestDTO.getUnitPurchasePrice(),
                requestDTO.getNotes()
        );
        StockReceipt savedReceipt = stockReceiptRepository.save(receipt);

        return new StockAdjustmentResponseDTO(
                savedReceipt.getStockReceiptId(),
                saved.getProductId(),
                saved.getProductName(),
                saved.getProductUnit(),
                savedReceipt.getReceivedDate(),
                previous,
                requestDTO.getQuantity(),
                saved.getProductQuantity(),
                savedReceipt.getUnitPurchasePrice(),
                savedReceipt.getTotalCost(),
                saved.getProductPrice()
        );
    }

    @Override
    public List<StockReceiptResponseDTO> getStockReceipts(Long productId) {
        // Fail clearly when the product does not exist.
        getProductById(productId);

        return stockReceiptRepository.findByProductId(productId)
                .stream()
                .map(receipt -> {
                    StockReceiptResponseDTO dto = new StockReceiptResponseDTO();
                    dto.setStockReceiptId(receipt.getStockReceiptId());
                    dto.setProductId(receipt.getProductId());
                    dto.setSupplierId(receipt.getSupplierId());
                    dto.setReceivedDate(receipt.getReceivedDate());
                    dto.setQuantity(receipt.getQuantity());
                    dto.setUnitPurchasePrice(receipt.getUnitPurchasePrice());
                    dto.setTotalCost(receipt.getTotalCost());
                    dto.setNotes(receipt.getNotes());
                    return dto;
                })
                .toList();
    }

    @Override
    public void deleteProduct(Long productId) {
        getProductById(productId);
        stockReceiptRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
    }
}
