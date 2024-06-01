package com.kzcse.springboot;

import com.kzcse.springboot.discount.data.DiscountJsonUtils;
import com.kzcse.springboot.discount.data.repository.DiscountByPriceRepository;
import com.kzcse.springboot.discount.data.repository.DiscountByProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);

        System.out.println(new DiscountJsonUtils()
                .addDiscountByProduct(context.getBean(DiscountByProductRepository.class)));
        System.out.println(new DiscountJsonUtils().
                addDiscountByPrice(context.getBean(DiscountByPriceRepository.class)));
    }


}


