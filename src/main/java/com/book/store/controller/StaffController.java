package com.book.store.controller;


import com.book.store.entities.*;
import com.book.store.helper.GeneratUniqueRandomNumber;
import com.book.store.helper.Message;
import com.book.store.services.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeService employeeService;

    private Logger logger = LoggerFactory.getLogger(StaffController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private BookService bookService;

    @Autowired
    private AddressService addressService;
    @Autowired
    private EmailService emailService;


    //common data for all page
    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        logger.info("Login By " + userName);

        // get User Detail By UserName(EmailId)
        Customer staff = this.customerService.FindByEmailId(userName);
        model.addAttribute("staff", staff);

        Employee employee = this.employeeService.findByCustomerId(staff.getCustomerId());
        int countOrder = this.orderService.countOrderForEmployee(employee);
        model.addAttribute("countOrder", countOrder);

        int numberOrder = this.orderService.countTotalOrderOfEmployee(employee);
        int numberOfPendingOrder = this.orderService.countPendingOrderOfEmployee(employee);
        int numberOfDeliveredOrder = this.orderService.countDeliveredOrderOfEmployee(employee);

        model.addAttribute("numberOrder",numberOrder);
        model.addAttribute("numberOfPendingOrder",numberOfPendingOrder);
        model.addAttribute("numberOfDeliveredOrder",numberOfDeliveredOrder);


    }

    //index page
    @GetMapping("/index")
    public String index(Model model,Principal principal) {
        model.addAttribute("", "Staff | Dashboard");
        Customer customer = this.customerService.FindByEmailId(principal.getName());
        Employee employee = this.employeeService.findByCustomerId(customer.getCustomerId());
        List<Order> orders = this.orderService.findCurrentDateOrderForStaff(LocalDate.now(), employee);
        model.addAttribute("orders",orders);
        return "staff/index";
    }


    //account page
    @GetMapping("/account")
    public String accountPage(Model model, Principal principal) {
        model.addAttribute("title", "Staff | Account");
        return "staff/account";
    }


    //Update Profile
    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("customer") Customer customer, @RequestParam("employeePhoneNumber") String phoneNo, HttpSession session) {
        try {

            Customer customer1 = this.customerService.findById(customer.getCustomerId());
            customer1.setCustomerName(customer.getCustomerName());
            customer1.setCustomerEmailId(customer.getCustomerEmailId());
            customer1.getEmployee().setEmployeePhoneNumber(phoneNo);
            this.customerService.add(customer1);
            session.setAttribute("message", new Message("Successfully Update Profile!!", "success"));
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }
        return "redirect:/staff/account";
    }


    //change Password
    @PostMapping("/change-password")
    public String updatePassword(@RequestParam("customerId") String id, @RequestParam("password1") String password1, @RequestParam("password2") String password2, HttpSession session) {

        try {
            if (password2.equals(password1)) {

                Customer customer = this.customerService.findById(Long.valueOf(id));

                customer.setCustomerPassword(this.passwordEncoder.encode(password1));
                this.customerService.add(customer);
                session.setAttribute("message", new Message("Successfully Change Password!!", "success"));
                this.logger.info("change password" + password1);
            } else {
                session.setAttribute("message", new Message("Dose not match password!!", "danger"));
            }
        } catch (Exception e) {
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            e.getMessage();
        }

        return "redirect:/staff/account";
    }


    //show all new Order
    @GetMapping("/new-order")
    public String manageNewOrder(Model model, Principal principal) {
        model.addAttribute("title", "Staff | Pending-Order");
        try {
            Customer customer = this.customerService.FindByEmailId(principal.getName());
            Employee employee = this.employeeService.findByCustomerId(customer.getCustomerId());
            System.out.println(employee.getEmployeeId()+"---------------------------");
            List<Order> orders = this.orderService.findOrderForEmployee(employee);
            model.addAttribute("orders", orders);
        } catch (Exception e) {
            e.getMessage();
        }
        return "staff/manage-new-order";
    }


    //order Detail
    @GetMapping("/{orderId}/order-detail")
    public String orderInfo(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("title", "Order-Detail");

        try {
            Order order = this.orderService.get(orderId);

            model.addAttribute("order", order);
        } catch (Exception e) {
            e.getMessage();
        }

        return "staff/order-info";
    }

    //book information
    @GetMapping("/{book_Id}/book-Info")
    public String bookDetail(@PathVariable("book_Id") Long book_Id, Model model) {
        Book book = this.bookService.get(book_Id);
        model.addAttribute("book", book);
        return "staff/book-info";
    }




    @PostMapping("/generateOtp")
    @ResponseBody
    public String Generate(@RequestParam("emailId") String emailId,HttpSession session){

        try {
            int otp = GeneratUniqueRandomNumber.generateRandomNumber(1000, 9999);
            Customer customer = this.customerService.FindByEmailId(emailId);
            String subject = "Verification Otp For Delivery";
            String text = "<html><head><style>" +
                    "h3 { color: #007BFF; }" +
                    "</style></head><body>" +
                    "<p>Dear " + customer.getCustomerName() + ",</p>" +
                    "<br>" +
                    "<p>Before Receive Your Book Give Varification code For Verify..</p>" +
                    "<p>To ensure the security of your account, Please Give (OTP) within the Our Staff:</p>" +
                    "<br>" +
                    "<h3>OTP: <span style=\"color: #007BFF; font-weight: bold;\">" + otp + "</span></h3>" +
                    "<br>" +
                    "</body></html>";

            Email email = Email.builder().to(customer.getCustomerEmailId()).subject(subject).massage(text).build();
            this.emailService.sendEmail(email);
            session.setAttribute("StatusOtp", otp);
        }
        catch (Exception e){
            e.getMessage();
        }
        return "success";
    }

    @PostMapping("/veryfie-otp-deliverd-order")
    public String veryfieOtpDeliverdOrder(@RequestParam("orderId") String orderId,@RequestParam("otp") int otp,HttpSession session){

        try{
            int otp1 = (int) session.getAttribute("StatusOtp");
           if(otp == otp1){
               Order order = this.orderService.get(orderId);
               order.setOrderStatus(String.valueOf(OrderStatus.DELIVERED));
               order.setDeliveredDate(LocalDate.now());
               this.orderService.add(order);
               String text= "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Book Delivered</h2></div>" + "<div class='content'>" + "<p>Dear " + order.getCustomer().getAddress().getFullName() + ",</p>" + "<p>Your Book is Successfully delivered..</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3><div class='product'><span>Book Title:</span><span>" + order.getBook().getBookTitle() + "</span></div>" + "<div class='product'><span>Book Author:</span><span>"+order.getBook().getBookAuthor()+"</span></div>" + "<div class='product'><span>Book Quantity:</span><span>" + order.getBookQuantity() + "</span></div><div class='total'><span>Total Amount:</span><span>$" + order.getTransaction().getAmount() + "</span></div>" + "</div>" + "<p>Thank Your For Chossing Our Plateform and give feedback for our product</p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p></p>" + "</div>" + "</div>" + "</body></html>";

               session.setAttribute("message", new Message("OTP is Valid ,Order is Completed", "success"));
               Email email=Email.builder().massage(text).subject("Order Delivery Detail").to(order.getCustomer().getCustomerEmailId()).build();
               this.emailService.sendEmail(email);
           }
           else {
               session.setAttribute("message", new Message("Doesn't Match OTP so Give Valid OTP!!", "warning"));
           }
        }catch (Exception e){
            session.setAttribute("message", new Message("Something Went Wrong !!", "warning"));
            e.getMessage();
        }

        return "redirect:/staff/new-order";
    }


    //show all order history
    @GetMapping("/delivered-order-detail/{page}")
    public String orderHistory(@PathVariable("page") int page,Model model,Principal principal){
        try{
            model.addAttribute("title","Staff | Order-History");
            Pageable pageable= PageRequest.of(page,5);
            Customer customer = this.customerService.FindByEmailId(principal.getName());
            Employee employee = this.employeeService.findByCustomerId(customer.getCustomerId());
            Page<Order> orders = this.orderService.findOrderForStaffDelivered(employee,pageable);

            model.addAttribute("orders",orders.getContent());
            model.addAttribute("currentPage", orders.getNumber()); // Page number is zero-based, so we add 1
            model.addAttribute("totalPages", orders.getTotalPages());

        }catch (Exception e){
            e.getMessage();
        }
        return "staff/delivered-order";
    }

}
