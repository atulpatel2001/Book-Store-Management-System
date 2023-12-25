package com.book.store.services;

import com.book.store.entities.Book;
import com.book.store.entities.FeedBack;

import java.util.List;

public interface FeedBackService {


    public FeedBack get(long id);

    public FeedBack add(FeedBack feedBack);

    public List<FeedBack> findAll();

    public List<FeedBack> findByBook(Book book);


    public void delete(FeedBack feedBack);
}
