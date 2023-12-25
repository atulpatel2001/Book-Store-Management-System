package com.book.store.services.imple;

import com.book.store.entities.Book;
import com.book.store.repository.BookRepository;
import com.book.store.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImple implements BookService {

    @Autowired
    private BookRepository  bookRepository;
    @Override
    public Book add(Book book) {

        return this.bookRepository.save(book);
    }

    @Override
    public Book get(Long id) {
        return this.bookRepository.findById(id).get();
    }

    @Override
    public void delete(Book book) {
        this.bookRepository.delete(book);
    }

    @Override
    public int countAllBook() {
        return this.bookRepository.countAllBook();
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    @Override
    public List<Book> searchBook(String query) {
        return this.bookRepository.searchBook(query);
    }


    @Override
    public Book getByCategoryId(Long id) {
        return this.bookRepository.findByBookCategoryId(id--);
    }

    @Override
    public List<Book> findByBookCategoryTitle(String categoryTitle) {
        return this.bookRepository.findByBookCategoryTitle(categoryTitle);
    }

    @Override
    public Page<Book> findAllPagination(Pageable pageable) {
        return this.bookRepository.findAll(pageable);
    }

    @Override
    public List<Book> findNewBook() {
        return this.bookRepository.findNewBook();
    }

    @Override
    public List<Book> findOldBook() {
        return this.bookRepository.findOldBook();
    }

    @Override
    public List<Book> findBestSellerBook() {
        return this.bookRepository.findBestsellerBook();
    }
}
