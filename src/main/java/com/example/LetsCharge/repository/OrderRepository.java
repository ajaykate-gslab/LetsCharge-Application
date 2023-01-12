package com.example.LetsCharge.repository;

import com.example.LetsCharge.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {
}
