package com.book.store.repository;

import com.book.store.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    public Transaction findByOrderId(String orderId);

    @Query("SELECT t FROM Transaction as t where t.paymentId LIKE %:query%  OR t.order.orderId LIKE %:query%")
    public List<Transaction> searchTransaction(@Param("query") String query);
}
