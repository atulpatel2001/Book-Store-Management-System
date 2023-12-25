package com.book.store.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_Id")
    private Long customerId;

    @Column(name = "customer_Name")
    private String customerName;
    @Column(name = "customer_Email_Id")
    private String customerEmailId;

    @Column(name = "customer_Password")
    private String customerPassword;

    @Column(name = "customer_Enable")
    private boolean enable;

    @Column(name = "customer_Role")
    private String customerRole;

    @Column(name = "customer_Join_Date")
    private LocalDate customerJoinDate;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Address address;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL)
    @JsonBackReference
    private Employee employee;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Cart> carts;
}
