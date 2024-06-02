package com.kzcse.springboot.inventory.data;

import com.kzcse.springboot.common.ErrorMessage;
import com.kzcse.springboot.product.domain.ProductFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductFactory productFactory;

    public InventoryService(InventoryRepository inventoryRepository, ProductFactory productFactory) {
        this.inventoryRepository = inventoryRepository;
        this.productFactory = productFactory;
    }

    public void updateInventoryOrThrow(List<InventoryEntity> entities) throws Exception {
        checkAllProductExitOrThrow(entities);
        var iterator = inventoryRepository.saveAll(entities);
        var savedEntities = StreamSupport
                .stream(iterator
                                .spliterator(),
                        false
                )
                .toList();
        var isNotSaved = savedEntities.size() != entities.size();
        if (isNotSaved) {
            throw new ErrorMessage()
                    .setMessage("failed")
                    .setCauses("Not all products inventory were not added successfully")
                    .setSource("InventoryService::updateInventoryOrThrow")
                    .toException();
        }

    }

    private void checkAllProductExitOrThrow(List<InventoryEntity> entities) throws Exception {
        for (var entity : entities) {
            productFactory.createProductAbsentUseCase().throwIfNotExit(entity.getPid());
        }
    }

    public List<InventoryEntity> getAllOrThrows() throws Exception {
        var iterator = inventoryRepository.findAll();
        return StreamSupport
                .stream(iterator
                                .spliterator(),
                        false
                )
                .toList();
    }
}
