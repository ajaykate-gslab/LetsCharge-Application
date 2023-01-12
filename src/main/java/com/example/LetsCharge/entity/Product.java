package com.example.LetsCharge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", nullable = false)
    private String product_id;

    @Column(unique = true)
    private String product_name;
    @Column(unique = true)
    @NotEmpty(message = "Should not empty")
    private String product_code;

    private Double productPrice;


    @NonNull
    @Enumerated(EnumType.STRING)
    private com.example.LetsCharge.services.model.Product.StatusEnum status;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
