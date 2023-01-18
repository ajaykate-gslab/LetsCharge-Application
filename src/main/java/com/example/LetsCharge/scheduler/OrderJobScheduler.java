package com.example.LetsCharge.scheduler;

import com.example.LetsCharge.entity.JobResponse;
import com.example.LetsCharge.job.OrderJob;
import com.example.LetsCharge.repository.SubscriptionRepository;
import com.example.LetsCharge.services.model.Subscription;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;

@RestController
public class OrderJobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderJobScheduler.class);
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    String subscription_id;
    @Value( "${quartzJobScheduler.time}")
    private int time;
    //---------------------------------------------------------------------------
        public ResponseEntity<JobResponse> scheduleNextOrder(Subscription subscription, org.threeten.bp.LocalDateTime nextOrderDate, ZoneId timeZone, String subscriptionId) {
            try {


                //LocalDateTime temp = LocalDateTime.of(2023, 01, 10, 8, 24, 00, 788000000);
                this.subscription_id= subscriptionId;
                LocalDateTime temp1=LocalDateTime.now();
                LocalDateTime temp2 = temp1.plusSeconds(time);

                ZonedDateTime dateTime = ZonedDateTime.of(temp2, timeZone);
                if(dateTime.isBefore(ZonedDateTime.now())) {
                    JobResponse jobResponse = new JobResponse(false,
                            "dateTime must be after current time");
                    return ResponseEntity.badRequest().body(jobResponse);
                }

                JobDetail jobDetail = buildJobDetail(subscription);
                Trigger trigger = buildJobTrigger(jobDetail, dateTime);
                scheduler.scheduleJob(jobDetail, trigger);

                JobResponse jobResponse = new JobResponse(true,jobDetail.getKey().getName(),jobDetail.getKey().getGroup(),"Order job scheduled successfully...!!!");
                return ResponseEntity.ok(jobResponse);
            } catch (SchedulerException ex) {
                logger.error("Error scheduling Order", ex);

                JobResponse jobResponse = new JobResponse(false,
                        "Error scheduling Order. Please try later!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jobResponse);
            }
        }
        //----------------------------------------------------------------------------------------

        private JobDetail buildJobDetail(Subscription subscription) {
            JobDataMap jobDataMap = new JobDataMap();

            jobDataMap.put("customerId",subscription.getCustomer().getCustomerId());
            jobDataMap.put("planId",subscription.getPlan().getPlanId());
            jobDataMap.put("productId",subscription.getProduct().getProductId());
            jobDataMap.put("status",subscription.getStatus());
            jobDataMap.put("subscription_id",subscription_id);

            return JobBuilder.newJob(OrderJob.class)
                    .withIdentity(UUID.randomUUID().toString(), "Order-jobs")
                    .withDescription("Send Order Job")
                    .usingJobData(jobDataMap)
                    .storeDurably()
                    .build();
        }

        private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "Order-triggers")
                    .withDescription("Send Order Trigger")
                    .startAt(Date.from(startAt.toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
        }

        public void cancelNextOrder() throws SchedulerException {
            JobDetail job = newJob(OrderJob.class).withIdentity("MyJobName", "MyJobGroup").build();
            JobKey jobKey= new JobKey(job.getJobClass().getName());
            scheduler.deleteJob(job.getKey());
          //  scheduler.deleteJob(jobKey);
            logger.info("Order cancelled successfully");
        }

    }

