package com.book.store.repository;

import com.book.store.entities.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory,Long> {

    @Query(" select b from BookCategory as b where b.categoryTitle LIKE %:title%")
    public List<BookCategory> searchByTitle(@Param("title") String title);

}
