package com.example.LetsCharge.repository;

import com.example.LetsCharge.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Query(value = "SELECT * FROM subscription s WHERE s.subscription_id =:subscription_id", nativeQuery = true)
    Optional<Subscription> findAllById(@Param("subscription_id")String subscription_id);

    @Query(value = "SELECT customer_customer_id FROM subscription s WHERE s.customer_customer_id =:customer_id", nativeQuery = true)
    List<String> findCustomerIdById(String customer_id);


    /*@Query(value = "SELECT * FROM subscription s WHERE" +
            " s.customer_customer_id = :customer_id", nativeQuery = true)
    List<Subscription> findSubscriptionByCustomerId(String customer_id);*/

    @Query(value = "SELECT * FROM letscharge.subscription s where s.status = 'running'",nativeQuery = true)
    List<Subscription> findAllRunningSubscription();

    @Modifying
    @Transactional
    @Query(value = "UPDATE subscription s SET s.status=:status WHERE s.subscription_id=:subscription_id",nativeQuery = true)
    int updateSubscriptionStatus(@Param("status") String status, @Param("subscription_id") String subscription_id);
  //int updateSubscriptionStatus(com.example.LetsCharge.services.model.Subscription.StatusEnum status, String subscription_id);


    @Query(value = "SELECT subscription_id FROM subscription s WHERE s.subscription_id=:subscription_id", nativeQuery = true)
    List<String> findSubscriptionIdById(@Param("subscription_id") String subscription_id);

    @Query(value = "SELECT status FROM subscription s WHERE s.subscription_id=:subscription_id", nativeQuery = true)
    List<String> findStatusById(@Param("subscription_id") String subscription_id);

}





