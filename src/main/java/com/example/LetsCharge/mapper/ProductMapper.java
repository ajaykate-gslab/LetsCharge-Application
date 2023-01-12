package com.example.LetsCharge.mapper;

import com.example.LetsCharge.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    @Autowired
    private ModelMapper modelMapper;

    public Product productModelToProductEntity(com.example.LetsCharge.services.model.Product product){
        Product productEntity= this.modelMapper.map(product,Product.class);
        return productEntity;
    }

    /*public com.example.LetsCharge.services.model.Product productEntityToProductModel(Product product){
        com.example.LetsCharge.services.model.Product productModel=
                this.modelMapper.map(product,Product.class);
        return productModel;
    }*/

}
