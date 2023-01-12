package com.example.LetsCharge.controller;

import com.example.LetsCharge.mapper.PlanMapper;
import com.example.LetsCharge.repository.PlanRepository;
import com.example.LetsCharge.services.api.FetchAllPlanApi;
import com.example.LetsCharge.services.api.FetchPlanByIdApi;
import com.example.LetsCharge.services.api.InsertPlanApi;
import com.example.LetsCharge.services.model.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.LocalDate;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api")
public class PlanController implements InsertPlanApi, FetchPlanByIdApi, FetchAllPlanApi {
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanMapper planMapper;
    Logger logger= LoggerFactory.getLogger(PlanController.class);

    // --------------------- API TO FIND PLAN BY ID -------------------------------
    @Override
    public ResponseEntity<Plan> fetchPlanByIdGet(String id) {
        Optional<com.example.LetsCharge.entity.Plan> optionalPlan=
                planRepository.findById(id);
        if (optionalPlan.isPresent()){
            com.example.LetsCharge.entity.Plan plan=optionalPlan.get();
            logger.info("Plan found for this id");
            return new ResponseEntity<>(plan, HttpStatus.FOUND);
        }
        else{
            logger.info("Plan not found for this id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // --------------------- API TO CREATE PLAN -------------------------------
    @Override
    public ResponseEntity<Plan> insertPlanPost(@Valid Plan body) {
        com.example.LetsCharge.entity.Plan plan=new com.example.LetsCharge.entity.Plan();
        plan.setPlan_name(body.getPlanName());
       plan.setPlanType(body.getPlanType());
        plan.setPlan_frequency(body.getPlanFrequency());
        plan.setStatus(body.getStatus());
        plan=planRepository.save(plan);
       // planRepository.save(planMapper.planModelTOPlanEntity(body));
        logger.info("Plan created successfully...!!!");
        return new ResponseEntity<>(plan,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Plan>> fetchAllPlanGet() {
        List<com.example.LetsCharge.entity.Plan> planList=planRepository.findAll();
        return new ResponseEntity(planList,HttpStatus.FOUND);
    }
}
