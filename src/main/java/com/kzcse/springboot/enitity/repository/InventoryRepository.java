package com.kzcse.springboot.enitity.repository;

import com.kzcse.springboot.enitity.entity.InventoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<InventoryEntity, String> {

    @Modifying
    @Transactional
    @Query("update InventoryEntity i set i.quantity = i.quantity - ?2 where i.pid = ?1")
    void subtractQuantity(String pid, int quantity);

    @Modifying
    @Transactional
    @Query("update InventoryEntity i set i.quantity = i.quantity + ?2 where i.pid = ?1")
    void addQuantity(String pid, int quantity);
}
