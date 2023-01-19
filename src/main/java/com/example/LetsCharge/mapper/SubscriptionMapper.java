package com.example.LetsCharge.mapper;

import com.example.LetsCharge.controller.OrderController;
import com.example.LetsCharge.entity.Customer;
import com.example.LetsCharge.entity.Plan;
import com.example.LetsCharge.entity.Product;
import com.example.LetsCharge.helper.CalculateNextOrderDate;
import com.example.LetsCharge.repository.*;
import com.example.LetsCharge.scheduler.OrderJobScheduler;
import com.example.LetsCharge.services.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SubscriptionMapper {
/*
    SubscriptionMapper INSTANCE= Mappers.getMapper(SubscriptionMapper.class);
    Subscription entitySubscriptionToModelSubscription(com.example.LetsCharge.entity.Subscription subscription);
    com.example.LetsCharge.entity.Subscription modelSubscriptionToEntitySubscription(Subscription subscription);*/

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderJobScheduler orderJobScheduler;
    @Autowired
    private CalculateNextOrderDate nextDate;
    @Autowired
    private OrderController orderController;
    com.example.LetsCharge.entity.Subscription subscription = new com.example.LetsCharge.entity.Subscription();
    Customer customer = new Customer();
    Product product = new Product();
    Plan plan = new Plan();

    public com.example.LetsCharge.entity.Subscription modelSubscriptionToEntitySubscription
            (com.example.LetsCharge.services.model.Subscription body) {

        subscription.setActivated_at(LocalDateTime.now());
        subscription.setQuantity(body.getQuantity());
        subscription.setStatus(body.getStatus());

        com.example.LetsCharge.services.model.Customer customer1 = body.getCustomer();
        com.example.LetsCharge.services.model.Product product1 = body.getProduct();
        com.example.LetsCharge.services.model.Plan plan1 = body.getPlan();

        String customer_id = customer1.getCustomerId();
        String product_id = product1.getProductId();
        String plan_id = plan1.getPlanId();

        Optional<Customer> optionalCustomer = customerRepository.findById(customer_id);
        Optional<Product> optionalProduct = productRepository.findById(product_id);
        Optional<Plan> optionalPlan = planRepository.findById(plan_id);

        customer = optionalCustomer.get();
        product = optionalProduct.get();
        plan = optionalPlan.get();

        subscription.setCustomer(customer);
        subscription.setProduct(product);
        subscription.setPlan(plan);
        subscription.setFirst_name(customer.getFirst_name());
        subscription.setLast_name(customer.getLast_name());
        subscription.setEmail(customer.getEmail());
        subscription.setProduct_name(product.getProduct_name());
        subscription.setProduct_price(product.getProductPrice());
        subscription.setPlan_name(plan.getPlan_name());
        subscription.setPlan_type(plan.getPlanType());
        subscription.setPlan_frequency(plan.getPlan_frequency());

        return subscription;
    }
}