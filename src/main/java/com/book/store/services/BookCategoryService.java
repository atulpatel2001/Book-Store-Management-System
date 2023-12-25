package com.book.store.services;

import com.book.store.entities.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookCategoryService {



    BookCategory add(BookCategory bookCategory);

    BookCategory get(Long id);

    Page<BookCategory> findAll(Pageable pageable);


    void delete(BookCategory bookCategory);

    List<BookCategory> searchByTitle(String title);

    List<BookCategory> findAllCategory();
}
