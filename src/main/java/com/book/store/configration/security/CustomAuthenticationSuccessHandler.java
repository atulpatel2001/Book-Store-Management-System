package com.book.store.configration.security;

import com.book.store.entities.Customer;
import com.book.store.entities.LoggedInCustomer;
import com.book.store.services.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    // Autowire the LoggedInUsers bean
    @Autowired
    private LoggedInCustomer loggedInCustomer;

    @Autowired
    private CustomerService customerService;


    private Logger logger= LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        this.logger.info("LOGiN CUSTOMER:--"+username);
        Customer customer = this.customerService.FindByEmailId(username);
        customer.setEnable(true);
        this.customerService.add(customer);
        this.loggedInCustomer.addUser(username);

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/Book-Store/admin/index");
                return;
            } else if (auth.getAuthority().equals("ROLE_EMPLOYEE-M")) {
                response.sendRedirect("/Book-Store/manager/index");
                return;
            } else if (auth.getAuthority().equals("ROLE_EMPLOYEE-S")) {
                response.sendRedirect("/Book-Store/staff/index");
                return;
            }
        }
        response.sendRedirect("/Book-Store/purchase-book");


    }

}

