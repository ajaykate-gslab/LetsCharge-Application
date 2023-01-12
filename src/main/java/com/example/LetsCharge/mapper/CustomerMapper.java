package com.example.LetsCharge.mapper;


import com.example.LetsCharge.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Customer customerModelToCustomerEntity(com.example.LetsCharge.services.model.Customer customer) {

        Customer customerEntity=this.modelMapper.map(customer,Customer.class);
        return customerEntity;
    }


    public com.example.LetsCharge.services.model.Customer customerEntityToCustomerModel(Customer customer) {
        com.example.LetsCharge.services.model.Customer customerModel=
                this.modelMapper.map(customer, com.example.LetsCharge.services.model.Customer.class);
        return customerModel;
    }

}

