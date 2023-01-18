package com.example.LetsCharge.job;

import com.example.LetsCharge.controller.OrderController;
import com.example.LetsCharge.entity.Subscription;
import com.example.LetsCharge.scheduler.OrderJobScheduler;
import com.example.LetsCharge.repository.SubscriptionRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
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

        //check subscription status
        /*Optional<com.example.LetsCharge.entity.Subscription> optionalSubscription =
                subscriptionRepository.findAllById(jobDataMap.getString("subscription_id"));*/
        List<String> stringList =
                subscriptionRepository.findStatusById(jobDataMap.getString("subscription_id"));
        if (stringList.size() !=0){
            String status = stringList.get(0);
            if (status.equals("RUNNING") || status.equals("running"))
            {
                orderController.recurringOrderScheduler(jobDataMap);
            } else if (status.equals("CANCEL") || status.equals("cancel"))
            {
                try {
                    orderController.cancelOrderScheduler();
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            System.out.println(stringList.size()+" ### "+stringList.get(0));
        }
    }

}
