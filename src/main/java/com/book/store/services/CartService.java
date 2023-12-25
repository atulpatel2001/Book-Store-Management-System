package com.book.store.services;

import com.book.store.entities.Book;
import com.book.store.entities.Cart;
import com.book.store.entities.Customer;

import java.util.List;

public interface CartService {

    public Cart add(Cart cart);

    public Cart get(Long id);

    public List<Cart> findAll();


    public void delete(Cart cart);


    public int countCart(Customer customer);


    public Cart findCartForBookAndCustomer(Customer customer,Book book);


    public List<Cart> findCartForCustomer(Customer customer);
}
