package com.book.store.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionPayLoad {

    private String orderId;

    private String bookTitle;

    private String bookImage;

    private String transcationStatus;

    private String amount;

    private String paymentId;

    private Long bookId;

}
