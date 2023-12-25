package com.book.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_Id")
    private Long bookId;
    @Column(name = "book_Title")
    private String bookTitle;
    @Column(name = "book_Author")
    private String bookAuthor;
    @Column(name = "book_ISBN")
    private String bookISBN;
    @Column(name = "book_Status")
    private String bookStatus;
    @Column(name = "book_Price")
    private String bookPrice;
    @Column(name = "book_Quantity")
    private int bookQuantity;
    @Column(name = "book_ImageUrl")
    private String bookImageUrl;
    @Column(name = "book_discription",length = 6000)
    private String discription;
    @Column(name = "book_Add_Date")
    private LocalDate bookAddDate;


    @ManyToOne
    @JoinColumn(name = "bookCategory_Id")
    private BookCategory category;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Cart> carts;
}
