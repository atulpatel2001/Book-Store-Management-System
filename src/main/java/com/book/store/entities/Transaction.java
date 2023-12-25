package com.book.store.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_transcation_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction-Id")
    private Long transcationId;
    @Column(name = "OrderId")
    private String orderId;
    @Column(name = "transaction_Amount")
    private String amount;
    @Column(name = "transaction_Receipt")
    private String receipt;
    @Column(name = "order_Status")
    private String orderStatus;
    @Column(name = "transaction_Status")
    private String transcationStatus;
    @Column(name = "payment_Id")
    private String paymentId;
    @Column(name = "transcation_Date")
    private LocalDateTime transcationdate;

    @OneToOne(mappedBy = "transaction")
    @JsonBackReference
    private Order order;

}
