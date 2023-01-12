package com.example.LetsCharge.mapper;

import com.example.LetsCharge.entity.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Order orderModelToOrderEntity(com.example.LetsCharge.services.model.Order order)
    {
        Order orderEntity= this.modelMapper.map(order,Order.class);
        return orderEntity;
    }

    public com.example.LetsCharge.services.model.Order orderEntityToOrderModel(Order order)
    {
        com.example.LetsCharge.services.model.Order orderModel=
                this.modelMapper.map(order, com.example.LetsCharge.services.model.Order.class);
        return orderModel;

    }
}
