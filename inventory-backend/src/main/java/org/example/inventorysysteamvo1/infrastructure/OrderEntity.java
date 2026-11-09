package org.example.inventorysysteamvo1.infrastructure;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID", nullable = false, updatable = false)
    private Long orderId;

    @Column(name = "Date_Of_Order", nullable = false)
    private LocalDate dateOfOrder;

    @Column(name = "Order_Details", length = 1000)
    private String orderDetails;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Order_ID", nullable = false)
    @Builder.Default
    private List<OrderDetailEntity> items = new ArrayList<>();
}
