package com.book.store.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPayLoad {

    private String orderId;

    private String bookImage;

    private String bookTitle;

    private String bookAuthor;

    private String bookQuantity;

    private  String payAmount;

}
