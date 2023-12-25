package com.book.store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_bookcategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookCategory_Id")
    private Long categoryId;

    @Column(name = "bookCategory_Title")
    private String categoryTitle;

    @Column(name = "bookCategory_Discription")
    private String categoryDiscription;

    @Column(name = "bookCategory_AddDate")
    private LocalDate categoryAddDate;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;
}
