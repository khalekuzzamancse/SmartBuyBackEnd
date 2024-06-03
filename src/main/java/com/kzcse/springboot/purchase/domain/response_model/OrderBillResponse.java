package com.kzcse.springboot.purchase.domain.response_model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderBillResponse {
   private List<OrderResponse> bills;
   private int total;
}
