package com.book.store.services;
import org.springframework.data.domain.Page;
import com.book.store.entities.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {


    Customer FindByEmailId(String emailId);
    Customer findById(Long id);

    Customer add(Customer customer);

    void delete(Customer customer);

    Page<Customer> findAllPagination(Pageable pageable);

    List<Customer> searchCustomer(String query);

    Customer findManagerWithPincode (String pinCode);


}
