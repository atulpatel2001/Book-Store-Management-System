package com.book.store.repository;


import com.book.store.entities.Customer;
import com.book.store.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("select c from Customer c where c.customerEmailId =:customerEmailId")
    Customer findByCustomerEmailId(@Param("customerEmailId") String customerEmailId);

    @Query("select c from Customer c where c.customerRole = 'ROLE_CUSTOMER'")
    Page<Customer> findByAllPagination(Pageable pageable);

    @Query("SELECT c from Customer c where c.customerRole = 'ROLE_CUSTOMER' AND c.customerName LIKE %:query%")
    List<Customer> searchCustomer(@Param("query") String query);

    @Query("SELECT c from Customer c where c.customerRole = 'ROLE_EMPLOYEE-M' AND c.address.pinCode =:pinCode")
    Customer findManagerWithPincode(@Param("pinCode") String pinCode);

}
