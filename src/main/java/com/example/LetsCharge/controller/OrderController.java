package com.example.LetsCharge.controller;

import com.example.LetsCharge.entity.Plan;
import com.example.LetsCharge.entity.Product;
import com.example.LetsCharge.entity.Subscription;
import com.example.LetsCharge.helper.CalculateNextOrderDate;
import com.example.LetsCharge.mapper.OrderMapper;
import com.example.LetsCharge.repository.CustomerRepository;
import com.example.LetsCharge.repository.OrderRepository;
import com.example.LetsCharge.repository.SubscriptionRepository;
import com.example.LetsCharge.scheduler.OrderJobScheduler;
import com.example.LetsCharge.services.api.*;
import com.example.LetsCharge.services.model.Order;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderController implements AddOrderApi, UpdateOrderByIdApi, DeleteOrderByIdApi, FetchOrderByIdApi, FetchAllOrdersApi {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private OrderJobScheduler orderJobScheduler;
    Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CalculateNextOrderDate calculateNextOrder;

    //----------------------------- API TO CREATE ORDER --------------------------------------------------
    @Override
    public ResponseEntity<Order> addOrderPost(Order body) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(body.getSubscriptionId());

        if (optionalSubscription.isPresent()) {
            com.example.LetsCharge.entity.Order_details order_details = new com.example.LetsCharge.entity.Order_details();
            com.example.LetsCharge.entity.Order order = new com.example.LetsCharge.entity.Order();
            Subscription subscription = optionalSubscription.get();
            order.setSubscription_id(body.getSubscriptionId());
            double d = (subscription.getProduct_price()) * (subscription.getQuantity());

            order.setOrder_total(d);
            order_details.setProductName(subscription.getProduct_name());
            order_details.setProductPrice(subscription.getProduct_price());
            order_details.setQuantity(subscription.getQuantity());
            order.setOrder_details(order_details);
            order.setPlanType(subscription.getPlan_type());

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");
            String currentDate = dateTimeFormatter.format(localDateTime);
            order.setOrder_generation_date(currentDate);

            Plan.PlanTypeEnum planType = subscription.getPlan_type();
            int planFrequency = subscription.getPlan_frequency();

            LocalDateTime nextDate = LocalDateTime.now();

            if (planType.equals(Plan.PlanTypeEnum.MONTHLY)) {
                nextDate = localDateTime.plusMonths(planFrequency);
                order.setNext_order_date(nextDate);

            } else if (planType.equals(Plan.PlanTypeEnum.WEEKLY)) {
                nextDate = localDateTime.plusWeeks(planFrequency);
                // nextDate = dateTimeFormatter.format(localDateTime);
                order.setNext_order_date(nextDate);

            } else if (planType.equals(Plan.PlanTypeEnum.ANNUALLY)) {
                nextDate = localDateTime.plusYears(planFrequency);
                //nextDate = dateTimeFormatter.format(localDateTime);
                order.setNext_order_date(nextDate);
            }
            order = orderRepository.save(order);
            logger.info("Order created successfully..!!!");
            return new ResponseEntity(order, HttpStatus.CREATED);
        } else {
            logger.info("Order not found...!!!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //--------------------- API TO UPDATE ORDER BY ID--------------------------
    @Override
    public ResponseEntity<Order> updateOrderByIdPut(String id, Order body) {
        Optional<com.example.LetsCharge.entity.Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            com.example.LetsCharge.entity.Order order = orderRepository.save(orderMapper.orderModelToOrderEntity(body));
            logger.info("order Updated successfully...!!!");
            return new ResponseEntity(order, HttpStatus.OK);
        } else {
            logger.info("order not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //--------------------- API TO DELETE ORDER BY ID --------------------------
    @Override
    public ResponseEntity<Order> deleteOrderByIdDelete(String id) {
        Optional<com.example.LetsCharge.entity.Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            com.example.LetsCharge.entity.Order order = optionalOrder.get();

            orderRepository.delete(order);
            logger.info("Order deleted successfully..!!!");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.info("Order not found...!!!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //--------------------- API TO FETCH A ORDER BY ID  --------------------------
    @Override
    public ResponseEntity<Order> fetchOrderByIdGet(String id) {
        Optional<com.example.LetsCharge.entity.Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            com.example.LetsCharge.entity.Order order = optionalOrder.get();
            logger.info("Order Found...!!!");
            return new ResponseEntity(order, HttpStatus.FOUND);
        } else {
            logger.info("Order Not Found ...!!!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //--------------------- API TO LIST ALL ORDER --------------------------
    @Override
    public ResponseEntity<List<Order>> fetchAllOrdersGet() {
        List<com.example.LetsCharge.entity.Order> orderlist = orderRepository.findAll();
        return new ResponseEntity(orderlist, HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    //Method to schedule the next order automatically
    public ResponseEntity<com.example.LetsCharge.entity.Order> recurringOrderScheduler(JobDataMap jobDataMap) {

        String customerId = jobDataMap.getString("customerId");
        String planId = jobDataMap.getString("planId");
        String productId = jobDataMap.getString("productId");
        String subscription_id = jobDataMap.getString("subscription_id");
       /* //String statusEnum= jobDataMap.getString("status");
        com.example.LetsCharge.services.model.Subscription.StatusEnum o= (com.example.LetsCharge.services.model.Subscription.StatusEnum) jobDataMap.get("status");
*/
        logger.info("inside order Controller");

        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscription_id);
        if (optionalSubscription.isPresent()) {
            com.example.LetsCharge.entity.Order_details order_details = new com.example.LetsCharge.entity.Order_details();
            com.example.LetsCharge.entity.Order order = new com.example.LetsCharge.entity.Order();
            Subscription subscription = optionalSubscription.get();
            order.setSubscription_id(subscription_id);
            double d = (subscription.getProduct_price()) * (subscription.getQuantity());

            order.setOrder_total(d);
            order_details.setProductName(subscription.getProduct_name());
            order_details.setProductPrice(subscription.getProduct_price());
            order_details.setQuantity(subscription.getQuantity());
            order.setOrder_details(order_details);
            order.setPlanType(subscription.getPlan_type());

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");
            String currentDate = dateTimeFormatter.format(localDateTime);
            order.setOrder_generation_date(currentDate);


            //to set nextOrderDate
            order.setNext_order_date(calculateNextOrder.nextOrderDate(subscription));

            order = orderRepository.save(order);
            logger.info("Order created successfully..!!!");
            return new ResponseEntity(order, HttpStatus.CREATED);
        } else {
            logger.info("Order not found...!!!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public void cancelOrderScheduler() throws SchedulerException {
        orderJobScheduler.cancelNextOrder();
    }


    //-------------------- METHOD TO CREATE FIRST DEFAULT ORDER FOR NEW USER -------------------------------
    public void createFirstDefaultOrder(Product product, com.example.LetsCharge.services.model.Subscription subscription) {
        com.example.LetsCharge.entity.Order order = new com.example.LetsCharge.entity.Order();
        com.example.LetsCharge.entity.Order_details order_details = new com.example.LetsCharge.entity.Order_details();
        double d = (product.getProductPrice()) * subscription.getQuantity();
        order.setOrder_total(d);
        order.setSubscription_id(subscription.getSubscriptionId());
        order_details.setProductName("FirstDefaultOrder");
        order_details.setProductPrice(0.0);
        order_details.setQuantity(1);
        order.setOrder_details(order_details);
        order.setPlanType(com.example.LetsCharge.services.model.Plan.PlanTypeEnum.MONTHLY);

        //order_generation_date
        java.time.LocalDateTime localDateTime = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a");
        String currentDate = dateTimeFormatter.format(localDateTime);
        order.setOrder_generation_date(currentDate);

        //next_order_date
        org.threeten.bp.LocalDateTime dateTime = org.threeten.bp.LocalDateTime.now();
        order.setNext_order_date(dateTime.plusMonths(3));
        order = orderRepository.save(order);
        logger.info("First Default Order created successfully..!!!");
    }
}

