package com.asiryk.shoppingsite.pojo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private Integer paymentId;
    private Order order;
    private Double amount;
    private PaymentStatus status;

    public enum PaymentStatus {
        COMPLETED,
        FAILED
    }
}

