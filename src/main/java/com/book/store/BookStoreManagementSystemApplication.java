package com.book.store;

import com.book.store.entities.Customer;
import com.book.store.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class BookStoreManagementSystemApplication  implements CommandLineRunner {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private BCryptPasswordEncoder  bCryptPasswordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BookStoreManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Customer customer = Customer.builder().customerName("Atul Patel").customerEmailId("ajpatel7096@gmail.com").customerPassword(this.bCryptPasswordEncoder.encode("atul@2001")).customerRole("ROLE_ADMIN").enable(false).customerJoinDate(LocalDate.now()).build();

		Customer customer1 = this.customerService.FindByEmailId(customer.getCustomerEmailId());
		if(customer1 == null){
			this.customerService.add(customer);
		}
		else {

		}
	}
}
