package com.book.store.controller;

import com.book.store.entities.*;
import com.book.store.helper.Message;
import com.book.store.services.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BookCategoryService bookCategoryService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AddressService addressService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;


    private Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        logger.info("Login By " + userName);

        // get User Detail By UserName(EmailId)
        Customer manager = this.customerService.FindByEmailId(userName);

        int numberOfStaff = this.employeeService.countStaffPariticularPinCode(manager.getAddress().getPinCode());
        int pendingOrder = this.orderService.countPendingOrderParticularPincodeForManage(manager.getAddress().getPinCode());
        int deliveredOrder = this.orderService.countDeliveredOrderParticularPincodeForManage(manager.getAddress().getPinCode());
        int numberOfBook = this.bookService.countAllBook();
        System.out.println(numberOfBook+"---"+pendingOrder+"---"+deliveredOrder+"----"+numberOfBook);
        model.addAttribute("numberOfStaff",numberOfStaff);
        model.addAttribute("deliveredOrder",deliveredOrder);
        model.addAttribute("pendingOrder",pendingOrder);
        model.addAttribute("numberOfBook",numberOfBook);
        model.addAttribute("manager", manager);
    }

    //dashboard
    @GetMapping("/index")
    public String managerDashboard(Model model, Principal principal) {
        model.addAttribute("title", "Manager | Dashboard");
        Customer customer = this.customerService.FindByEmailId(principal.getName());
        List<Order> orders = this.orderService.findAllOrderParticularManagerWithPincodeNotEmployee(customer.getAddress().getPinCode());

        List<Employee> staffs = this.employeeService.findAllStaffPariticularPinCode(customer.getAddress().getPinCode());
        model.addAttribute("orders", orders);

        model.addAttribute("staffs", staffs);
        return "manager/index";
    }


    //manage book
    @GetMapping("/manage-books/{page}")
    public String manageBooks(@PathVariable("page") int page, Model model) {
        model.addAttribute("title", "Manager |Manage-Book");
        Pageable pageble = PageRequest.of(page, 6);
        Page<Book> books = this.bookService.findAllPagination(pageble);
        List<BookCategory> categories = this.bookCategoryService.findAllCategory();
        model.addAttribute("categories", categories);

        model.addAttribute("books", books.getContent());
        model.addAttribute("currentPage", books.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", books.getTotalPages());
        return "manager/manage-book-manage";
    }

    //book-information
    @GetMapping("/{book_Id}/book-Info")
    public String bookDetail(@PathVariable("book_Id") Long book_Id, Model model) {
        Book book = this.bookService.get(book_Id);
        model.addAttribute("book", book);
        return "manager/book-info";
    }


    // delete book
    @PostMapping("/delete-book")
    @ResponseBody
    public String deleteBook(@RequestParam String book_id) {
        try {

            Book book = this.bookService.get(Long.valueOf(book_id));
            this.bookService.delete(book);
            this.logger.info("book has been deleted");
        } catch (Exception e) {
            e.getMessage();

            this.logger.info("Something went wrong during a book delete time");
        }

        return "book has been successfully deleted";
    }


    //handle book data

    @PostMapping("/add-bookData")
    public String handleService(@ModelAttribute("Book") Book book, @RequestParam("categoryId") Long categoryId, HttpSession httpSession) {
        try {
            BookCategory category = this.bookCategoryService.get(categoryId);
            book.setBookAddDate(LocalDate.now());
            book.setCategory(category);

            this.bookService.add(book);
            this.logger.info("succfully add  Book");
            httpSession.setAttribute("message", new Message("Successfully Add Service!!", "success"));
            return "redirect:/manager/manage-books/0";

        } catch (Exception e) {
            e.printStackTrace();
            this.logger.info("error during add book time");
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            return "redirect:/manager/manage-books/0";
        }

    }


    //update - book
    @PostMapping("/update-bookData")
    public String updateBookData(@ModelAttribute("book") Book book, HttpSession httpSession) {
        try {
            Book book1 = this.bookService.get(book.getBookId());
            book1.setBookAuthor(book.getBookAuthor());
            book1.setBookISBN(book.getBookISBN());
            book1.setBookPrice(book.getBookPrice());
            book1.setBookQuantity(book.getBookQuantity());
            book1.setBookImageUrl(book.getBookImageUrl());
            book1.setBookStatus(book.getBookStatus());
            book1.setBookTitle(book.getBookTitle());
            book1.setDiscription(book.getDiscription());
            Book update = this.bookService.add(book1);
            httpSession.setAttribute("message", new Message("Successfully Update Book!!-->" + update.getBookTitle(), "success"));

        } catch (Exception e) {
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }
        return "redirect:/manager/manage-books/0";
    }


    //staff
    @GetMapping("/manage-staff/{page}")
    public String manageStaff(@PathVariable("page") int page, Model model, Principal principal) {
        Pageable pageable = PageRequest.of(page, 5);
        Customer customer = this.customerService.FindByEmailId(principal.getName());

        Page<Employee> employees = this.employeeService.findAllStaffPincode(customer.getAddress().getPinCode(), pageable);

        model.addAttribute("employees", employees.getContent());
        model.addAttribute("currentPage", employees.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", employees.getTotalPages());
        model.addAttribute("title", "Manager | Manage-Staff");
        return "manager/manage-staff-manage";
    }


    @PostMapping("/add-staffData")
    public String addStaffData(@ModelAttribute("customer") Customer customer, @ModelAttribute("address") Address address, @ModelAttribute("employee") Employee employee, @RequestParam("password2") String password2, HttpSession session, Principal principal) {

        try {
            if (password2.equals(customer.getCustomerPassword())) {
                Customer check = this.customerService.FindByEmailId(customer.getCustomerEmailId());
                if (check == null) {
                    Customer customer2 = this.customerService.FindByEmailId(principal.getName());
                    address.setFullName(customer.getCustomerName());
                    address.setPinCode(customer2.getAddress().getPinCode());

                    address.setPhoneNumber(employee.getEmployeePhoneNumber());
                    customer.setCustomerRole("ROLE_EMPLOYEE-S");
                    employee.setEmployeePosition("Staff");
                    customer.setEnable(false);
                    customer.setCustomerPassword(this.passwordEncoder.encode(customer.getCustomerPassword()));
                    customer.setCustomerJoinDate(LocalDate.now());
                    Customer customer1 = this.customerService.add(customer);
                    address.setCustomer(customer1);
                    this.addressService.add(address);
                    employee.setCustomer(customer1);
                    this.employeeService.add(employee);
                    session.setAttribute("message", new Message("Successfully Register Staff", "success"));

                } else {
                    session.setAttribute("message", new Message("Check Your Email , This Email is Already Register !!!", "danger"));
                }
            } else {
                session.setAttribute("message", new Message("Password dose not Match!! Try Again !!", "danger"));

            }
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }

        return "redirect:/manager/manage-staff/0";
    }

    //Delete manager or Ben

    @PostMapping("/delete-staff")
    @ResponseBody
    public String deleteStaff(@RequestParam("staff_id") String staffId) {
        try {
            Employee employee = this.employeeService.get(Long.valueOf(staffId));
            this.employeeService.delete(employee);
            this.logger.info("Staff has been Ban!!");
        } catch (Exception e) {
            e.getMessage();

            this.logger.info("something went wrong during a Staff ban time");
        }

        return "Manager has been deleted";
    }

    @GetMapping("/{staff_id}/staff-info")
    public String managerInfo(@PathVariable("staff_id") Long staff_id, Model model) {
        model.addAttribute("title", "Manager | Manager-Information");
        Employee employee = this.employeeService.get(staff_id);
        model.addAttribute("employee", employee);
        return "manager/staff-info";
    }


    @GetMapping("/manage-order/{page}")
    public String orderDetail(@PathVariable("page") int page,Model model, Principal principal) {
        model.addAttribute("title", "Manager | Manage-Order");
        Pageable pageable= PageRequest.of(page, 5);
        Customer customer = this.customerService.FindByEmailId(principal.getName());
        Page<Order> orders = this.orderService.findAllOrderParticularManagerWithPincode(customer.getAddress().getPinCode(),pageable);

        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", orders.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", orders.getTotalPages());
        List<Employee> staffs = this.employeeService.findAllStaffPariticularPinCode(customer.getAddress().getPinCode());
        model.addAttribute("staffs", staffs);


        return "manager/manage-order";
    }


    //Assign Employee for order
    @PostMapping("/assign-order")
    public String assignEmployee(@RequestParam("orderId") String orderId, @RequestParam("empId") String empId, HttpSession session) {
        try {
            Order order1 = this.orderService.get(orderId);
            Employee employee = this.employeeService.get(Long.valueOf(empId));
            System.out.println(employee.getCustomer().getCustomerName() + "------" + order1.getBook().getBookTitle());

            order1.setEmployee(employee);
            Order order = this.orderService.add(order1);

            String text= "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Order Out Of Delivery</h2></div>" + "<div class='content'>" + "<p>Dear " + order.getCustomer().getAddress().getFullName() + ",</p>" + "<p>Your Order is On The Way ..</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3><div class='product'><span>Book Title:</span><span>" + order.getBook().getBookTitle() + "</span></div>" + "<div class='product'><span>Book Author:</span><span>"+order.getBook().getBookAuthor()+"</span></div>" + "<div class='product'><span>Book Quantity:</span><span>" + order.getBookQuantity() + "</span></div><div class='product'><span>Delivery Man Name:</span><span>"+order.getEmployee().getCustomer().getCustomerName()+"</span></div> <div class='product'><span>Phone Number:</span><span>"+order.getEmployee().getEmployeePhoneNumber()+"</span></div><div class='total'><span>Total Amount:</span><span>$" + order.getTransaction().getAmount() + "</span></div>" + "</div>" + "<p></p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p></p>" + "</div>" + "</div>" + "</body></html>";
            Email email=Email.builder().massage(text).subject("Order Delivery Detail").to(order.getCustomer().getCustomerEmailId()).build();
            this.emailService.sendEmail(email);
            session.setAttribute("message", new Message("Successfully Assign Order to Staff", "success"));
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong!!!!", "danger"));
        }

        return "redirect:/manager/manage-order/0";
    }


    //order detail
    @GetMapping("/{orderId}/order-detail")
    public String orderDetail(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("title","Manage | order-info");
        try {
            Order order = this.orderService.get(orderId);
            model.addAttribute("order", order);
        } catch (Exception e) {
            e.getMessage();
        }
        return "manager/order-detail";
    }

    //change orderStatus
    @PostMapping("/updateChangeStatus")
    @ResponseBody
    public String updateChangeStatus(@RequestParam("status") String status,@RequestParam("orderId") String orderId){
   String text=null;
        try{
            Order order = this.orderService.get(orderId);
             if(status.equals("1")){
                order.setOrderStatus(String.valueOf(OrderStatus.SHIPPED));
                order.setShippedDate(LocalDate.now());
                 text += "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Order Shipped</h2></div>" + "<div class='content'>" + "<p>Dear " + order.getCustomer().getAddress().getFullName() + ",</p>" + "<p>Successfully Shipped Your Book.After Your order will be Deliver.</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3><div class='product'><span>Book Title:</span><span>" + order.getBook().getBookTitle() + "</span></div>" + "<div class='product'><span>Book Author:</span><span>"+order.getBook().getBookAuthor()+"</span></div>" + "<div class='product'><span>Book Quantity:</span><span>" + order.getBookQuantity() + "</span></div>" + "<div class='total'><span>Total Amount:</span><span>$" + order.getTransaction().getAmount() + "</span></div>" + "</div>" + "<p></p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p></p>" + "</div>" + "</div>" + "</body></html>";

             }
             else {
                    order.setOrderStatus(String.valueOf(OrderStatus.OUTOFDELIVERY));
                    order.setOutOfDeliveryDate(LocalDate.now());
                 text += "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Order Out Of Delivery</h2></div>" + "<div class='content'>" + "<p>Dear " + order.getCustomer().getAddress().getFullName() + ",</p>" + "<p>Your Order is On The Way ..</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3><div class='product'><span>Book Title:</span><span>" + order.getBook().getBookTitle() + "</span></div>" + "<div class='product'><span>Book Author:</span><span>"+order.getBook().getBookAuthor()+"</span></div>" + "<div class='product'><span>Book Quantity:</span><span>" + order.getBookQuantity() + "</span></div>" + "<div class='total'><span>Total Amount:</span><span>$" + order.getTransaction().getAmount() + "</span></div>" + "</div>" + "<p></p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p></p>" + "</div>" + "</div>" + "</body></html>";

             }


        this.orderService.add(order);
            Email email=Email.builder().massage(text).subject("Order Delivery Detail").to(order.getCustomer().getCustomerEmailId()).build();
            this.emailService.sendEmail(email);
        }catch (Exception e){
            e.getMessage();
        }
        return "success";
    }



  //account Page
    @GetMapping("/account")
    public String accountPage(Model model){
        model.addAttribute("title","Manager | Account");
        return "manager/account";
    }

    //update profile
    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("customer") Customer customer,HttpSession session){
        try{

            Customer customer1 = this.customerService.findById(customer.getCustomerId());
            customer1.setCustomerName(customer.getCustomerName());
            customer1.setCustomerEmailId(customer.getCustomerEmailId());
            this.customerService.add(customer1);
            session.setAttribute("message", new Message("Successfully Update Profile!!", "success"));
        }
        catch (Exception e){
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }
        return "redirect:/manager/account";
    }


    @PostMapping("/change-password")
    public String updatePassword(@RequestParam("customerId") String id,@RequestParam("password1") String password1,@RequestParam("password2") String password2,HttpSession session){

        try{
            if(password2.equals(password1)){

                Customer customer = this.customerService.findById(Long.valueOf(id));

                customer.setCustomerPassword(this.passwordEncoder.encode(password1));
                this.customerService.add(customer);
                session.setAttribute("message", new Message("Successfully Change Password!!", "success"));
                this.logger.info("change password"+password1);
            }
            else {
                session.setAttribute("message", new Message("Dose not match password!!", "danger"));
            }
        }catch (Exception e){
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            e.getMessage();
        }

        return "redirect:/manager/account";
    }
}
