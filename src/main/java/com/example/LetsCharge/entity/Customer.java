package com.example.LetsCharge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends com.example.LetsCharge.services.model.Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id", nullable = false)
    private String customer_id;
    @NotEmpty(message = "should not empty")
    private String first_name;

    @NotEmpty(message = "should not empty")
    private String last_name;

    @Column(unique = true)
    @Email(message = "Enter valid email Id")
    private String email;

    @NonNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

}
