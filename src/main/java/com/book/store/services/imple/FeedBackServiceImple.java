package com.book.store.services.imple;

import com.book.store.entities.Book;
import com.book.store.entities.FeedBack;
import com.book.store.repository.FeedBackRepository;
import com.book.store.services.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedBackServiceImple implements FeedBackService {
    @Autowired
    private FeedBackRepository feedBackRepository;

    @Override
    public FeedBack get(long id) {
        return this.feedBackRepository.findById(id).get();
    }

    @Override
    public FeedBack add(FeedBack feedBack) {
        return this.feedBackRepository.save(feedBack);
    }

    @Override
    public List<FeedBack> findAll() {
        return this.feedBackRepository.findAll();
    }

    @Override
    public List<FeedBack> findByBook(Book book) {
        return this.feedBackRepository.findByBook(book);
    }

    @Override
    public void delete(FeedBack feedBack) {
        this.feedBackRepository.delete(feedBack);
    }
}
