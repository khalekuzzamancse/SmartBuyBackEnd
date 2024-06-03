package com.kzcse.springboot.inventory.domain;

import com.kzcse.springboot.inventory.data.InventoryRepository;
import com.kzcse.springboot.inventory.domain.usecase.AddToExitingInventoryUseCase;
import com.kzcse.springboot.inventory.domain.usecase.GetInventoryUseCase;
import com.kzcse.springboot.product.domain.ProductFactory;
import org.springframework.stereotype.Component;

@Component
public class InventoryFactory {
    private final InventoryRepository repository;
    private final ProductFactory productFactory;


    public InventoryFactory(InventoryRepository repository, ProductFactory productFactory) {
        this.repository = repository;

        this.productFactory = productFactory;
    }
    public AddToExitingInventoryUseCase createAddToExitingInventoryUseCase(){
        return  new AddToExitingInventoryUseCase(productFactory,repository);
    }
    public GetInventoryUseCase createGetInventoryUseCase(){
        return  new GetInventoryUseCase(productFactory,repository);
    }


}
