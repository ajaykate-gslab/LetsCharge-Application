package com.example.LetsCharge.repository;

import com.example.LetsCharge.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,String> {
}
