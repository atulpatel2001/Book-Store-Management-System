package com.book.store.services;

import com.book.store.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {


    public Transaction add(Transaction transaction);

    public Transaction get(Long id);

    public List<Transaction>  findAll();

    public void delete(Transaction transaction);

    public Transaction getByOrderId(String orderId);

    public Page<Transaction> findAllPagination(Pageable pageable);


    public List<Transaction> searchTransaction(String query);
}
