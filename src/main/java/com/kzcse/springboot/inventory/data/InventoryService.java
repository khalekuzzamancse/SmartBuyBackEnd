package com.kzcse.springboot.inventory.data;

import com.kzcse.springboot.common.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void updateInventoryOrThrow(List<InventoryEntity> entities) throws Exception {
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
                    .setCauses("Not all products inventory were added successfully")
                    .setSource("InventoryService::updateInventoryOrThrow")
                    .toException();
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
