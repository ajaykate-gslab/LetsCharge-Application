package com.example.LetsCharge.entity;

import com.example.LetsCharge.services.model.Plan;
import jakarta.persistence.*;
import lombok.*;
import org.threeten.bp.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false)
    private String order_id;
    private String subscription_id;
   /* @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)*/
    private String order_generation_date;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Plan.PlanTypeEnum planType;
    private double order_total;
    private LocalDateTime next_order_date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_details_od_id")
    public Order_details order_details ;

}
