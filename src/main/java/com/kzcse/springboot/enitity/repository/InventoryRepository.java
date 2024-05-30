package com.kzcse.springboot.enitity.repository;

import com.kzcse.springboot.enitity.entity.InventoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<InventoryEntity, String> {


}
