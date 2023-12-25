package com.book.store.services.imple;

import com.book.store.entities.Customer;
import com.book.store.repository.CustomerRepository;
import com.book.store.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImple implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public Customer FindByEmailId(String emailId) {
        Customer customer = this.customerRepository.findByCustomerEmailId(emailId);
        if(customer==null){
            return null;
        }
        else {
            return customer;
        }
    }

    @Override
    public Customer findById(Long id) {
        return this.customerRepository.findById(id).get();
    }

    @Override
    public Customer add(Customer customer) {
        return this.customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        this.customerRepository.delete(customer);
    }

    @Override
    public Page<Customer> findAllPagination(Pageable pageable) {
        return this.customerRepository.findByAllPagination(pageable);
    }

    @Override
    public List<Customer> searchCustomer(String query) {
        return this.customerRepository.searchCustomer(query);
    }

    @Override
    public Customer findManagerWithPincode(String pinCode) {
        return this.customerRepository.findManagerWithPincode(pinCode);
    }
}
