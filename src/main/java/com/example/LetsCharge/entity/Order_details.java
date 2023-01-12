package com.example.LetsCharge.entity;

import com.example.LetsCharge.services.model.OrderDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_details")
public class Order_details {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_details_id", nullable = false)
    private String order_details_id;
    private String productName;
    private Double productPrice;
    private Integer quantity;


}
