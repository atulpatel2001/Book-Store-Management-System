package com.book.store.services;

import com.book.store.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {


    public Book  add(Book book);

    public Book get(Long id);

    public void delete(Book book);

    public int countAllBook();




    public List<Book> findAll();

    public List<Book> searchBook(String query);

    public Book getByCategoryId(Long id);

    public List<Book> findByBookCategoryTitle(String categoryTitle);

    public Page<Book> findAllPagination(Pageable pageable);


    public List<Book> findNewBook();
    public List<Book> findOldBook();

    public List<Book> findBestSellerBook();
}
