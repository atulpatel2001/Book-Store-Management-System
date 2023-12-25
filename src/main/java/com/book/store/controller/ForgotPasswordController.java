package com.book.store.controller;

import com.book.store.entities.Customer;
import com.book.store.entities.Email;
import com.book.store.helper.GeneratUniqueRandomNumber;
import com.book.store.helper.Message;
import com.book.store.services.CustomerService;
import com.book.store.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/password")
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/check-valid-email")
    public String checkValidEmail(@RequestParam("email") String email, HttpSession session) {
        int otp = GeneratUniqueRandomNumber.generateRandomNumber(1000, 9999);
        try {
            Customer customer = this.customerService.FindByEmailId(email);
            if (customer == null) {
                session.setAttribute("message", new Message("Do Not Register Email or Email is Wrong !!", "danger"));
                return "redirect:/signin";
            } else {
                String subject = " Reset Your Password";
                String text = "<html><head><style>" +
                        "h3 { color: #007BFF; }" +
                        "</style></head><body>" +
                        "<p>Dear " + customer.getCustomerName() + ",</p>" +
                        "<br>" +
                        "<p>We have received a request to reset your password for the Online Purchase Book.</p>" +
                        "<p>To ensure the security of your account, please enter the following One-Time Password (OTP) within the Online Book Store application:</p>" +
                        "<br>" +
                        "<h3>OTP: <span style=\"color: #007BFF; font-weight: bold;\">" + otp + "</span></h3>" +
                        "<br>" +
                        "<p>Please note that the OTP is valid for a limited time period only. If you don't enter the OTP within 10 minutes, you may need to restart the password reset process.</p>" +
                        "</body></html>";
                Email email1 = Email.builder().to(customer.getCustomerEmailId()).subject(subject).massage(text).build();
                boolean isDeliverd = emailService.sendEmail(email1);
                if (isDeliverd) {
                    session.setAttribute("myOtp", otp);
                    session.setAttribute("email", email);
                    session.setAttribute("message", new Message(" OTP Sent Successfully!!", "success"));
                    return "otp";
                } else {
                    session.setAttribute("message", new Message("Something Went Wrong Try Again!!!", "danger"));
                    return "redirect:/signin";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong!!", "danger"));
            return "redirect:/signin";
        }
    }

    @PostMapping("/veryfieotp")
    public String veryFileEmailOtp(@RequestParam("otp") int otp, Model model, HttpSession session) {
        model.addAttribute("title", "Forgot-Password");

        int otp1 = (int) session.getAttribute("myOtp");


        if (otp1 == otp) {
            session.setAttribute("message", new Message("OTP is Valid ,You Can Create New Password", "success"));
            return "newpassword";
        } else {
            session.setAttribute("message", new Message("Doesn't Match OTP so Give Valid OTP!!", "warning"));
            this.logger.info("not match----------------------------------");
            return "otp";
        }


    }

    @PostMapping("/new-password")
    public String newpasswordhandler(@RequestParam("password") String newPassword, Model model, HttpSession session) {
        model.addAttribute("title", "New-Password");
        try {
            String email = (String) session.getAttribute("email");
            Customer customer = this.customerService.FindByEmailId(email);
            customer.setCustomerPassword(this.passwordEncoder.encode(newPassword));
            this.customerService.add(customer);
            return "redirect:/signin?change=Successfully Create New Password!!";

        } catch (Exception e) {
            session.setAttribute("message", new Message("Something Went Wrong TryAgain!!", "warning"));
            return "redirect:/signin";
        }

    }
}
