package com.kzcse.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
//        var repo = context.getBean(ProductRepository.class);
//        var result = repo.saveAll(new DummyProductList().productsList);
//        System.out.println(result);
//        var repo = context.getBean(InventoryRepository.class);
//        var result=repo.saveAll(new DummyInventory().inventories);
//        System.out.println(result);
//        var repo = context.getBean(DiscountByProductRepository.class);
//        var result = repo.saveAll(new DummyOffer().offers);
//        System.out.println(result);
    }

}
