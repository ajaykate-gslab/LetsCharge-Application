package com.example.LetsCharge.mapper;

import com.example.LetsCharge.entity.Customer;
import com.example.LetsCharge.entity.Plan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Plan planModelTOPlanEntity(com.example.LetsCharge.services.model.Plan plan){
        Plan planEntity=this.modelMapper.map(plan, Plan.class);
        return planEntity;
    }

    public com.example.LetsCharge.services.model.Plan planEntityToPlanModel(Plan plan){
        com.example.LetsCharge.services.model.Plan planModel=
                this.modelMapper.map(plan, com.example.LetsCharge.services.model.Plan.class);
        return  planModel;
    }

}
