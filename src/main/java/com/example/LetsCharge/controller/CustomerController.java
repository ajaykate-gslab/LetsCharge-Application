package com.example.LetsCharge.controller;

import com.example.LetsCharge.mapper.CustomerMapper;
import com.example.LetsCharge.repository.CustomerRepository;
import com.example.LetsCharge.services.api.FetchAllCustomersApi;
import com.example.LetsCharge.services.api.FetchCustomerByIdApi;
import com.example.LetsCharge.services.api.InsertCustomerApi;
import com.example.LetsCharge.services.api.UpdateCustomerByIdApi;
import com.example.LetsCharge.services.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@Component
@RestController
@RequestMapping("/api")
@Validated
public class CustomerController implements InsertCustomerApi , FetchCustomerByIdApi , UpdateCustomerByIdApi ,FetchAllCustomersApi{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    Logger logger= LoggerFactory.getLogger(CustomerController.class);


    com.example.LetsCharge.entity.Customer customer= new com.example.LetsCharge.entity.Customer();

    //-------------------- API TO FIND THE CUSTOMER BY ID -------------------------
    @Override
    public ResponseEntity<Customer> fetchCustomerByIdGet(String id) {
        Optional<com.example.LetsCharge.entity.Customer>
                optionalCustomer=customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            customer=optionalCustomer.get();
            logger.info("Customer Found ");
            return new ResponseEntity<>(customer,HttpStatus.FOUND);
        }
        else {
            logger.info("Customer Not Found for this id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //--------------------- API TO INSERT THE CUSTOMER -------------------------
    @Override
    public ResponseEntity<Customer> insertCustomerPost(@Valid Customer body) {
        Customer customer1=body;

     // customer=customerMapper.customerModelToCustomerEntity(body);
        customer.setCustomer_id(body.getCustomerId());
        customer.setFirst_name(body.getFirstName());
        customer.setLast_name(body.getLastName());
        customer.setStatus(body.getStatus());
        customer.setEmail(body.getEmail());

        customer=customerRepository.save(customer);

        logger.info("Customer created successfully...!!!");
        return new ResponseEntity<>(customer,HttpStatus.CREATED);
    }

    //-------------------------- API TO UPDATE THE CUSTOMER -------------------------
    @Override
    public ResponseEntity<Customer> updateCustomerByIdPut(String id, Customer body) {

        Optional<com.example.LetsCharge.entity.Customer>
                optionalCustomer=customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            customer=optionalCustomer.get();
            customer.setFirst_name(body.getFirstName());
            customer.setLast_name(body.getLastName());
            customer.setStatus(body.getStatus());
            customer.setEmail(body.getEmail());

            customer = customerRepository.save(customer);
            logger.info("Customer updated successfully...!!!");

            return new ResponseEntity<>(customer,HttpStatus.OK);
        }
        else {
            logger.info("Customer Not Found for this id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//-------------------------- API TO LIST ALL THE CUSTOMERS -------------------------

    @Override
    public ResponseEntity<List<Customer>> fetchAllCustomersGet() {
        List<com.example.LetsCharge.entity.Customer> customerList=customerRepository.findAll();
        return  new ResponseEntity(customerList,HttpStatus.OK);
    }
}
