package com.example.LetsCharge.repository;

import com.example.LetsCharge.entity.Subscription;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,String> {
/*
    @Query(value = "SELECT * FROM subscription s WHERE" +
            " s.customer_customer_id = :customer_id", nativeQuery = true)
    Optional<Subscription> searchCustomer(String customer_id);

    @Override
    @Query(value = "SELECT * FROM subscription s WHERE" +
            " s.customer_customer_id = :customer_id", nativeQuery = true)
    Optional<Subscription> findById(String customer_id);
*/

    @Query(value = "SELECT * FROM subscription s WHERE s.customer_customer_id='customer_id'", nativeQuery = true)
    List<Subscription> findAllById(String customer_id);

    /*@Query(value = "SELECT * FROM subscription s WHERE" +
            " s.customer_customer_id = :customer_id", nativeQuery = true)
    List<Subscription> findSubscriptionByCustomerId(String customer_id);*/

    @Query(value = "SELECT * FROM letscharge.subscription s where s.status = 'running'",nativeQuery = true)
    List<Subscription> findAllRunningSubscription();

    @Modifying
    @Transactional
    @Query(value = "UPDATE subscription SET status='status' WHERE subscription_id='subscription_id'",nativeQuery = true)
    void updateSubscriptionStatus(String status, String subscription_id);

}





