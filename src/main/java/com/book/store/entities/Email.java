package com.book.store.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {
    private String to;
    private String subject;
    private String massage;
    private String file;
}
