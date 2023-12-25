package com.book.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long feedBackId;

    private String rating;

   private LocalDate feedBackDate;

    private String description;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
