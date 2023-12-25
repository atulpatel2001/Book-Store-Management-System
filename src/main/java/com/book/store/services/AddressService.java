package com.book.store.services;

import com.book.store.entities.Address;

import java.util.List;

public interface AddressService {

    public Address add(Address address);

    public Address get(Long id);

    public List<Address> findAll();

    public void delete(Address address);

    public List<Address> byCustomerId(Long cId);
}
