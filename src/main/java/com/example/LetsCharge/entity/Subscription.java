package com.example.LetsCharge.entity;

import io.swagger.annotations.ApiModel;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Setter@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subscription_id", nullable = false)
    private String subscription_id;

/*    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)*/
    private LocalDateTime activated_at;
    private Integer quantity;
    @NonNull
    @Enumerated(EnumType.STRING)
    private com.example.LetsCharge.services.model.Subscription.StatusEnum status;
    private String first_name;
    private String last_name;
    private String email;
    private String product_name;
    private double product_price;
    private String plan_name;
    private boolean sucess;

    private String message;

    @NonNull
    @Enumerated(EnumType.STRING)
    private com.example.LetsCharge.services.model.Plan.PlanTypeEnum plan_type;
    private int plan_frequency;

    private org.threeten.bp.LocalDateTime next_order_date;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_customer_id")
    public Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_plan_id")
    public Plan plan;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_product_id")
    public Product product;

    public Subscription(boolean sucess, String message) {
        this.sucess = sucess;
        this.message = message;
    }

}
