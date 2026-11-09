package org.example.inventorysysteamvo1.infrastructure;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Order_Detail")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_Detail_ID", nullable = false, updatable = false)
    private Long orderDetailId;

    @Column(name = "Product_ID", nullable = false)
    private Long productId;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "Date")
    private LocalDateTime date;
}
