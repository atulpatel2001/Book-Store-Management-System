package com.book.store.repository;

import com.book.store.entities.Book;
import com.book.store.entities.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {

    @Query("SELECT f FROM FeedBack as f where f.book =:book")
    public List<FeedBack> findByBook(@Param("book") Book book);
}
