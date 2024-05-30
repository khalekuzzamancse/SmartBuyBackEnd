package com.kzcse.springboot;

import com.kzcse.springboot.enitity.repository.DiscountByProductRepository;
import com.kzcse.springboot.enitity.repository.ProductRepository;
import com.kzcse.springboot.enitity.souce.DummyOffer;
import com.kzcse.springboot.enitity.souce.DummyProductList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
//        var repo = context.getBean(ProductRepository.class);
//        var result = repo.saveAll(new DummyProductList().productsList);
//        System.out.println(result);
//        var result=repo.saveAll(new DummyInventory().inventories);
//        System.out.println(result);
//        var repo = context.getBean(DiscountByProductRepository.class);
//        var result = repo.saveAll(new DummyOffer().offers);
//        System.out.println(result);
    }

}
