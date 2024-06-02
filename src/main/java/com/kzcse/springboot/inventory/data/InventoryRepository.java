package com.kzcse.springboot.inventory.data;

import com.sun.net.httpserver.Authenticator;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InventoryRepository extends CrudRepository<InventoryEntity, String> {


//
//        @Modifying
//    @Transactional
//    @Query("update InventoryEntity i set i.quantity = i.quantity - ?2 where i.pid = ?1")
//    void subtractQuantity(String pid, int quantity);
    @Modifying
    @Transactional
    @Query("update InventoryEntity i set i.quantity = i.quantity - ?2 where i.pid = ?1")
    int subtractQuantity(String pid, int quantity);

    @Modifying
    @Transactional
    @Query("update InventoryEntity i set i.quantity = i.quantity + ?2 where i.pid = ?1")
    int addQuantity(String pid, int quantity);


    @Query("select i.quantity from InventoryEntity i where i.pid = ?1")
    Optional<Integer> getQuantityByPid(String pid);
}
