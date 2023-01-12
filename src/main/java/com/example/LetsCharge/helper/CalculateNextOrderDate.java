package com.example.LetsCharge.helper;

import com.example.LetsCharge.entity.Plan;
import com.example.LetsCharge.entity.Subscription;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDateTime;

@Component
public class CalculateNextOrderDate {

    public LocalDateTime nextOrderDate(Subscription subscription){
        LocalDateTime localDateTime= LocalDateTime.now();
        LocalDateTime nextDate=LocalDateTime.now();
        Plan.PlanTypeEnum planType = subscription.getPlan_type();
        int planFrequency = subscription.getPlan_frequency();

        if (planType.equals(Plan.PlanTypeEnum.MONTHLY)) {
            nextDate = localDateTime.plusMonths(planFrequency);
            subscription.setNext_order_date(nextDate);

        } else if (planType.equals(Plan.PlanTypeEnum.WEEKLY)) {
            nextDate = localDateTime.plusWeeks(planFrequency);
            subscription.setNext_order_date(nextDate);

        } else if (planType.equals(Plan.PlanTypeEnum.ANNUALLY)) {
            nextDate = localDateTime.plusYears(planFrequency);
            subscription.setNext_order_date(nextDate);
        }
        return nextDate;
    }

}
