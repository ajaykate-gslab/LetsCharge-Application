package com.example.LetsCharge.controller;

import com.example.LetsCharge.entity.*;
import com.example.LetsCharge.helper.CalculateNextOrderDate;
import com.example.LetsCharge.repository.*;
import com.example.LetsCharge.scheduler.OrderJobScheduler;
import com.example.LetsCharge.services.api.CreateSubscriptionApi;
import com.example.LetsCharge.services.api.FetchAllSubscriptionApi;
import com.example.LetsCharge.services.api.UpdateSubscriptionApi;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SubscriptionController implements CreateSubscriptionApi, UpdateSubscriptionApi, FetchAllSubscriptionApi {
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

    Subscription subscription = new Subscription();
    Customer customer = new Customer();
    Product product = new Product();
    Plan plan = new Plan();

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    //------------------------------ API TO CREATE SUBSCRIPTION ---------------------------
    @Override
    public ResponseEntity<com.example.LetsCharge.services.model.Subscription> createSubscriptionPost(com.example.LetsCharge.services.model.Subscription body) {

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
        org.threeten.bp.LocalDateTime nextOrderDate = org.threeten.bp.LocalDateTime.now();

        if (optionalCustomer.isPresent()) {
            if (optionalProduct.isPresent()) {
                if (optionalPlan.isPresent()) {
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

                    // To create first default order for new customer
                    List<String> subscriptionList = subscriptionRepository.findCustomerIdById(body.getCustomer().getCustomerId());
                    if (subscriptionList.size() == 0) {
                        orderController.createFirstDefaultOrder(product, body);
                    }
                    //to set next_order_date
                    nextOrderDate = nextDate.nextOrderDate(subscription);
                    subscription.setNext_order_date(nextOrderDate);

                    subscription = subscriptionRepository.save(subscription);
                    logger.info("Subscription created successfully...!!!");

                    //Job scheduling at next_order date
                    String subscriptionId = subscription.getSubscription_id();
                    ZoneId zoneId = ZoneId.of("Asia/Kolkata");
                    ResponseEntity<JobResponse> jobResponseResponseEntity = orderJobScheduler.scheduleNextOrder(body, nextOrderDate, zoneId, subscriptionId);
                    subscription.setSucess(jobResponseResponseEntity.getBody().isSuccess());
                    return new ResponseEntity(subscription, HttpStatus.CREATED);
                }
            }
        }
        logger.info("object not found...!!!");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //------------------------------ API TO LIST ALL SUBSCRIPTIONS ---------------------------
    @Override
    public ResponseEntity<List<com.example.LetsCharge.services.model.Subscription>> fetchAllSubscriptionGet() {
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        return new ResponseEntity(subscriptionList, HttpStatus.OK);
    }


    //------------------------------ API TO UPDATE SUBSCRIPTION BY ID ---------------------------
    @Override
    public ResponseEntity<com.example.LetsCharge.services.model.Subscription> updateSubscriptionPatch(String id, com.example.LetsCharge.services.model.Subscription body) {

        List<String> subscriptionList = subscriptionRepository.findSubscriptionIdById(id);
        if (body.getStatus().equals(null)) {
            logger.info("Enter valid status like (running,cancel)..!!!");
            return new ResponseEntity("Enter valid status like (running,cancel)..!!!", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (subscriptionList.size() != 0) {

            String status = String.valueOf(body.getStatus());

            int isUpdated = subscriptionRepository.updateSubscriptionStatus(status, id);
            if (isUpdated == 1) {
                logger.info("Subscription updated successfully...!!!");
                try {
                    orderJobScheduler.cancelNextOrder();
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
                return new ResponseEntity("Subscription updated successfully...!!!", HttpStatus.CREATED);
            } else {
                logger.info("Subscription not updated...!!!");
                return new ResponseEntity("Subscription not updated...!!!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.info("Subscription not found");
            return new ResponseEntity("Subscription not found", HttpStatus.NOT_FOUND);
        }
    }
}
