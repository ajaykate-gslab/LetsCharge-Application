package com.example.LetsCharge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Plan extends com.example.LetsCharge.services.model.Plan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "plan_id", nullable = false)
    private String plan_id;

   @Column(unique = true)
    private String plan_name;

    @NonNull
    @Enumerated(EnumType.STRING)
    private PlanTypeEnum planType;

    private Integer plan_frequency;

    @NonNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
