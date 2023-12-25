package com.book.store.configration.security;

import com.book.store.entities.Customer;
import com.book.store.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerDetailServiceImple implements UserDetailsService{

	@Autowired
	private CustomerService customerService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer =this.customerService.FindByEmailId(username);
		if(customer== null) {
			throw new UsernameNotFoundException("could Not Found !!! ");
		}
		CustomCustomerDetail customCustomerDetail = new CustomCustomerDetail(customer);
		return customCustomerDetail;
	}


}
