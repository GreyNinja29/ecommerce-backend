package com.ecommerce.product.service;


import com.ecommerce.product.dto.AddProductDto;
import com.ecommerce.product.dto.UpdateProdDto;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepo;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    MongoTemplate mongoTemplate;



    public ResponseEntity<List<Product>> getAllProds() {

        List<Product> prod=productRepo.findAll();
        return new ResponseEntity<>(prod, HttpStatus.OK);
    }


    public ResponseEntity<?> addProd(AddProductDto addProductDto) {

        Product prod=Product.builder()
                        .name(addProductDto.getName())
                                .description(addProductDto.getDescription())
                                        .price(addProductDto.getPrice())
                                                .stock(addProductDto.getStock())
                                                        .category(addProductDto.getCategory())
                                                                .attributes(addProductDto.getAttributes())
                                                                        .build();


        productRepo.save(prod);

        return new ResponseEntity<>("Added",HttpStatus.OK);
    }

    public ResponseEntity<List<Product>> searchProducts(String keyword) {

        TextCriteria criteria=TextCriteria.forDefaultLanguage().matching(keyword);

        Query query=org.springframework.data.mongodb.core.query.TextQuery.queryText(criteria);

        List<Product> searchResult= mongoTemplate.find(query, Product.class);

        return new ResponseEntity<>(searchResult,HttpStatus.OK);

    }

    public ResponseEntity<?> updateProd(UpdateProdDto updateProdDto) {
        String prodId=updateProdDto.getId();



        if(!productRepo.existsById(prodId)) {
            return new ResponseEntity<>("Product Does Not exist!",HttpStatus.BAD_REQUEST);
        }

        Product oldProduct=productRepo.findById(prodId).get();




        if(updateProdDto.getName()!=null) oldProduct.setName(updateProdDto.getName());
        if(updateProdDto.getDescription()!=null) oldProduct.setDescription(updateProdDto.getDescription());
        if(updateProdDto.getPrice()!=null) oldProduct.setPrice(updateProdDto.getPrice());
        if(updateProdDto.getStock()!=null) oldProduct.setStock(updateProdDto.getStock());
        if(updateProdDto.getCategory()!=null) oldProduct.setCategory(updateProdDto.getCategory());

        if(updateProdDto.getAttributes()!=null) {
            Map<String,Object> attributes=oldProduct.getAttributes();
            if(attributes==null) {
                attributes=new HashMap<>();
            }

            Map<String,Object> updateAttribute=updateProdDto.getAttributes();



            for(String str:updateAttribute.keySet()) {

                attributes.put(str,updateAttribute.get(str));

            }

            oldProduct.setAttributes(attributes);
        }

        productRepo.save(oldProduct);

        return new ResponseEntity<>("Updated",HttpStatus.OK);

    }

    public ResponseEntity<?> deleteProd(String prodId) {

        if(!productRepo.existsById(prodId)) {
            return new ResponseEntity<>("invalid product!",HttpStatus.BAD_REQUEST);
        }

        productRepo.deleteById(prodId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> getProdById(String prodId) {
        Product prod=productRepo.findById(prodId).orElse(null);

        if(prod==null) {
            return new ResponseEntity<>("Product does not exist!",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(prod,HttpStatus.OK);




    }


    public ResponseEntity<?> reduceStock(String prodId,int quant) {

        //we are doing this as to lock the document for concurrency as doing this will allow us to
        //send only one request to db but performing the selection and reducing operations in single call

        //which will allow only one user to update the same product

        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(prodId));
        query.addCriteria(Criteria.where("stock").gte(quant));

        Update update=new Update().inc("stock",-quant);

        UpdateResult result=mongoTemplate.updateFirst(query,update,Product.class);

        if(result.getModifiedCount()==0) {
            throw new RuntimeException("Insufficient stock for product Id:"+prodId);
        }

        return new ResponseEntity<>(HttpStatus.OK);



    }
}
