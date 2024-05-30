package com.kzcse.springboot.return_product.data.entiry;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class ReturningPendingEntity{
        @Id String id;
        String purchasedProductId;
        String freeProductId;
        int purchaseQuantity;//amount that wants to back/return
        int freeItemQuantity; //free product that will be back along with main product
}
