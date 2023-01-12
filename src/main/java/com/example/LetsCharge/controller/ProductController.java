package com.example.LetsCharge.controller;

import com.example.LetsCharge.mapper.ProductMapper;
import com.example.LetsCharge.repository.ProductRepository;
import com.example.LetsCharge.services.api.FetchAllProductsApi;
import com.example.LetsCharge.services.api.FetchProductByIdApi;
import com.example.LetsCharge.services.api.InsertProductApi;
import com.example.LetsCharge.services.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController implements InsertProductApi, FetchProductByIdApi, FetchAllProductsApi {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    Logger logger= LoggerFactory.getLogger(ProductController.class);

    //------------------------ API TO CREATE PRODUCT --------------------------
    @Override
    public ResponseEntity<Product> insertProductPost(Product body) {
        com.example.LetsCharge.entity.Product product=new com.example.LetsCharge.entity.Product();
        product.setProduct_name(body.getProductName());
        product.setProduct_code(body.getProductCode());
        product.setProductPrice(body.getProductPrice());
        product.setStatus(body.getStatus());
        product=productRepository.save(product);

       // product = productRepository.save(productMapper.productModelToProductEntity(body));
        logger.info("product created successfully...!!!");
        return new ResponseEntity(product,HttpStatus.CREATED);
    }


    //------------------------------ API TO FIND PRODUCT BY ID ---------------------------
    @Override
    public ResponseEntity<Product> fetchProductByIdGet(String id) {
        Optional<com.example.LetsCharge.entity.Product> optionalProduct=
                productRepository.findById(id);
        if (optionalProduct.isPresent()){
            com.example.LetsCharge.entity.Product product=optionalProduct.get();
            logger.info("product found...!!!");
            return new ResponseEntity(product, HttpStatus.FOUND);
        }
        else{
            logger.info("product not found...!!!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//------------------------------ API TO LIST ALL PRODUCTS  ---------------------------

    @Override
    public ResponseEntity<List<Product>> fetchAllProductsGet() {
        List<com.example.LetsCharge.entity.Product> productList = productRepository.findAll();
        return new ResponseEntity(productList,HttpStatus.FOUND);
    }
}
