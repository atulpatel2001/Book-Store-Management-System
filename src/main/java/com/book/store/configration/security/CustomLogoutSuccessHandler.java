package com.book.store.configration.security;


import com.book.store.entities.Customer;
import com.book.store.entities.LoggedInCustomer;
import com.book.store.services.CustomerService;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    // Autowire the LoggedInUsers bean
    @Autowired
    private LoggedInCustomer loggedInCustomer;
    @Autowired
    private CustomerService customerService;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            String username = authentication.getName();
            Customer customer = this.customerService.FindByEmailId(username);
            customer.setEnable(false);
            this.customerService.add(customer);
            this.loggedInCustomer.removeUser(username);
        }
          response.sendRedirect("/Book-Store/signin");
    }
}

