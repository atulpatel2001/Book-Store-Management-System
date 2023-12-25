package com.book.store.repository;

import com.book.store.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("SELECT book from Book book WHERE book.bookTitle LIKE %:query% OR book.bookAuthor LIKE %:query% OR book.bookISBN LIKE %:query% OR book.category.categoryTitle LIKE %:query%")
    public List<Book> searchBook(@Param("query") String query);

    @Query("select book from Book as book where book.category.categoryId =:categoryId")
    public Book findByBookCategoryId(@Param("categoryId") Long categoryId);

    @Query("select book from Book as book where book.category.categoryTitle =:categoryTitle")
    public List<Book> findByBookCategoryTitle(@Param("categoryTitle") String categoryTitle);

    @Query("select book from Book as book where book.bookStatus = 'NEW' ORDER BY book.bookId DESC LIMIT 8")
    public List<Book> findNewBook();

    @Query("select book from Book as book where book.bookStatus = 'OLD' ORDER BY book.bookId DESC LIMIT  8")
    public List<Book> findOldBook();

    @Query("select book from Book as book where book.bookStatus = 'BESTSELLERS' ORDER BY book.bookId DESC LIMIT  8")
    public List<Book> findBestsellerBook();

    @Query("SELECT COUNT(b) from Book as b")
    public int countAllBook();
}
