package org.example.inventorysysteamvo1.infrastructure;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Stock_Receipt")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockReceiptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Stock_Receipt_ID", nullable = false, updatable = false)
    private Long stockReceiptId;

    @Column(name = "Product_ID", nullable = false)
    private Long productId;

    @Column(name = "Supplier_ID", nullable = false)
    private Long supplierId;

    @Column(name = "Received_Date", nullable = false)
    private LocalDate receivedDate;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "Unit_Purchase_Price", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPurchasePrice;

    @Column(name = "Total_Cost", precision = 14, scale = 2, nullable = false)
    private BigDecimal totalCost;

    @Column(name = "Notes", length = 500)
    private String notes;
}
