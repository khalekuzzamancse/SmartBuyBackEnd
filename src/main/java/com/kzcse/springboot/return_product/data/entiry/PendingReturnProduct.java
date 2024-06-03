package com.kzcse.springboot.return_product.data.entiry;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@EqualsAndHashCode
@Table(name = "PendingReturnProductTable")
public class PendingReturnProduct {
        @Id String id;
        String purchasedProductId;
        String bonusProductId; //product that was given was discount
        int mainProductReturnQuantity;//amount that wants to back/return
        int bonusReturnQuantity; //free product that will be back along with main product
}
