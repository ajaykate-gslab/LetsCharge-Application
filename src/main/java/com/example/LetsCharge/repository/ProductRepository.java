package com.example.LetsCharge.repository;

import com.example.LetsCharge.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {
}
