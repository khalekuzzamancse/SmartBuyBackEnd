package com.kzcse.springboot.enitity.souce;

import com.kzcse.springboot.enitity.entity.InventoryEntity;

import java.util.List;

public class DummyInventory {
    public List<InventoryEntity> inventories=List.of(
            new InventoryEntity("1", 50),
            new InventoryEntity("2", 60),
            new InventoryEntity("3", 70),
            new InventoryEntity("4", 80),
            new InventoryEntity("5", 90),
            new InventoryEntity("6", 100),
            new InventoryEntity("7", 55),
            new InventoryEntity("8", 65),
            new InventoryEntity("9", 75),
            new InventoryEntity("10", 85)
    );
}
