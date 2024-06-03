package com.kzcse.springboot.inventory.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "InventoryTable")
public class InventoryEntity {
    @Id
    private String pid;
    private int quantity;

}
