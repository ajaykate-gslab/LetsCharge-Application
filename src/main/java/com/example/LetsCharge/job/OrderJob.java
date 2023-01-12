package com.example.LetsCharge.job;

import com.example.LetsCharge.controller.OrderController;
import com.example.LetsCharge.scheduler.OrderJobScheduler;
import com.example.LetsCharge.repository.SubscriptionRepository;
import com.example.LetsCharge.services.model.Subscription;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderJob extends QuartzJobBean {
    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderJobScheduler orderJobScheduler;
    private static final Logger logger = LoggerFactory.getLogger(OrderJob.class);
    @Autowired
    private SubscriptionRepository subscriptionRepository;



    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        //orderController.recurringOrderScheduler(jobDataMap);


        //check subscription status
        Subscription subscription=new Subscription();
        Optional<com.example.LetsCharge.entity.Subscription> optionalSubscription
                = subscriptionRepository.findById(jobDataMap.getString("subscription_id"));
        if (optionalSubscription.isPresent()){
            Subscription.StatusEnum status =optionalSubscription.get().getStatus();
            if (status.equals(Subscription.StatusEnum.RUNNING)){
                orderController.recurringOrderScheduler(jobDataMap);
            }
            else if(status.equals(Subscription.StatusEnum.CANCEL)){
                try {
                    orderController.cancelOrderScheduler();
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
