package com.book.store.services.imple;

import com.book.store.entities.BookCategory;
import com.book.store.repository.BookCategoryRepository;
import com.book.store.services.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCategoryServiceImple implements BookCategoryService {

    @Autowired
    private BookCategoryRepository categoryRepository;


    @Override
    public BookCategory add(BookCategory bookCategory) {

     return this.categoryRepository.save(bookCategory);
    }

    @Override
    public BookCategory get(Long id) {
        return this.categoryRepository.findById(id).get();
    }

    @Override
    public Page<BookCategory> findAll(Pageable pageable) {
        return this.categoryRepository.findAll(pageable);
    }

    @Override
    public void delete(BookCategory bookCategory) {
          this.categoryRepository.delete(bookCategory);
    }

    @Override
    public List<BookCategory> searchByTitle(String title) {
        return this.categoryRepository.searchByTitle(title);
    }

    @Override
    public List<BookCategory> findAllCategory() {
        return this.categoryRepository.findAll();
    }
}
