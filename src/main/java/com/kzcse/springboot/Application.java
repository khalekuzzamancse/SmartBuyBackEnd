package com.kzcse.springboot;

import com.kzcse.springboot.enitity.Product;
import com.kzcse.springboot.enitity.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var repo = context.getBean(ProductRepository.class);
        var result=repo.save(new Product("1", "Abul Kalam"));
        System.out.println(result);
    }

}
