package com.book.store.controller;

import com.book.store.entities.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookCategoryService bookCategoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private OrderService  orderService;

    @Autowired
    private LoggedInCustomer loggedInCustomer;

    @Autowired
    private TransactionService transactionService;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        this.logger.info("Login By " + userName);

        // get User Detail By UserName(EmailId)
        Customer customer = this.customerService.FindByEmailId(userName);


        int numberOfBook = this.bookService.countAllBook();
        int numberOfManager = this.employeeService.countAllManager();
        int numberOfStaff=this.employeeService.countAllStaff();
        int numberOfOrder=this.orderService.countALlOrder();

        model.addAttribute("admin", customer);
        model.addAttribute("numberOfBook",numberOfBook);
        model.addAttribute("numberOfManager",numberOfManager);
        model.addAttribute("numberOfStaff",numberOfStaff);
        model.addAttribute("numberOfOrder",numberOfOrder);


    }


    //admin dashboard
    @GetMapping("/index")
    public String adminDashBoard(Model model) {

        model.addAttribute("title", "Admin | DashBoard");

        List<Customer> list=new ArrayList<>();
        Set<String> coustomerNames = this.loggedInCustomer.getUsers();
        for (String name: coustomerNames) {
            Customer customer = this.customerService.FindByEmailId(name);
            list.add(customer);
        }

        model.addAttribute("loggedCustomer",list);
        return "admin/index";
    }

    //book category page
    @GetMapping("/manage-category/{page}")
    public String manageCategory(@PathVariable("page") int page, Model model) {
        model.addAttribute("title", "Admin |Manage-Book-Category");
        Pageable pageable = PageRequest.of(page, 5);
        Page<BookCategory> pageResult = this.bookCategoryService.findAll(pageable);
        List<BookCategory> content = pageResult.getContent();

        model.addAttribute("categories", pageResult.getContent());
        model.addAttribute("currentPage", pageResult.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", pageResult.getTotalPages());
        return "admin/manage-Bookcategory";
    }


    //add book category in db
    @PostMapping("/add-categoryData")
    public String bookCategoryData(@ModelAttribute BookCategory bookCategory, HttpSession session) {

        try {
            bookCategory.setCategoryAddDate(LocalDate.now());
            BookCategory category = this.bookCategoryService.add(bookCategory);
            session.setAttribute("message", new Message("Successfully Add Category!!  " + category.getCategoryTitle(), "success"));
        } catch (Exception e) {
            session.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
        }
        return "redirect:/admin/manage-category/0";
    }


    //update Book category or geners
    @PostMapping("/update-categoryData")
    public String upadteBookCategory(@ModelAttribute BookCategory bookCategory, HttpSession session) {
        try {
            BookCategory category = this.bookCategoryService.get(bookCategory.getCategoryId());
            category.setCategoryDiscription(bookCategory.getCategoryDiscription());
            category.setCategoryTitle(bookCategory.getCategoryTitle());
            this.bookCategoryService.add(category);
            session.setAttribute("message", new Message("Successfully Add Category!!--->" + category.getCategoryTitle(), "success"));
        } catch (Exception e) {
            session.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
        }
        return "redirect:/admin/manage-category/0";
    }

    //delete Book Category or geners
    @PostMapping("/delete-book-category")
    @ResponseBody
    public String deleteCategory(@RequestParam String category_id) {
        try {
            BookCategory category = this.bookCategoryService.get(Long.valueOf(category_id));

            this.bookCategoryService.delete(category);
            this.logger.info("Book Category Has Been Deleted");

        } catch (Exception e) {
            e.getMessage();

            this.logger.info("Something Went Wrong during delete a book category!!!");
        }
        return "Book Category deleted successfully";
    }


    //book page
    @GetMapping("/manage-books/{page}")
    public String manageBooks(@PathVariable("page")int page, Model model) {
        model.addAttribute("title", "Admin |Manage-Book");
        Pageable pageble = PageRequest.of(page,6);
        Page<Book> books = this.bookService.findAllPagination(pageble);
        List<BookCategory> categories = this.bookCategoryService.findAllCategory();
        model.addAttribute("categories", categories);

        model.addAttribute("books", books.getContent());
        model.addAttribute("currentPage", books.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", books.getTotalPages());
        return "admin/manage-book";
    }

  //add -book
    @PostMapping("/add-bookData")
    public String handleService(@ModelAttribute("Book") Book book, @RequestParam("categoryId") Long categoryId, HttpSession httpSession) {
        try {
            BookCategory category = this.bookCategoryService.get(categoryId);
            book.setBookAddDate(LocalDate.now());
            book.setCategory(category);

            this.bookService.add(book);
            this.logger.info("succfully add  Book");
            httpSession.setAttribute("message", new Message("Successfully Add Book!!", "success"));
            return "redirect:/admin/manage-books/0";

        } catch (Exception e) {
            e.printStackTrace();
            this.logger.info("error during add book time");
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            return "redirect:/admin/manage-books/0";
        }

    }

    //update - book
    @PostMapping("/update-bookData")
    public String updateBookData(@ModelAttribute("book")Book book,HttpSession httpSession){
        try
        {
            Book book1 = this.bookService.get(book.getBookId());
            book1.setBookAuthor(book.getBookAuthor());
            book1.setBookISBN(book.getBookISBN());
            book1.setBookPrice(book.getBookPrice());
            book1.setBookQuantity(book.getBookQuantity());
            book1.setBookImageUrl(book.getBookImageUrl());
            book1.setBookStatus(book.getBookStatus());
            book1.setBookTitle(book.getBookTitle());
            book1.setDiscription(book.getDiscription());
            Book update = this.bookService.add(book);
            httpSession.setAttribute("message", new Message("Successfully Update Book!!-->"+update.getBookTitle(), "success"));

        }catch (Exception e){
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }
        return "redirect:/admin/manage-books/0";
    }


    // delete book
    @PostMapping("/delete-book")
    @ResponseBody
    public String deleteBook(@RequestParam String book_id){
        try{

            Book book = this.bookService.get(Long.valueOf(book_id));
            this.bookService.delete(book);
            this.logger.info("book has been deleted");
        }
        catch (Exception e){
            e.getMessage();

            this.logger.info("Something went wrong during a book delete time");
        }

        return "book has been successfully deleted";
    }

    //book information
    @GetMapping("/{book_Id}/book-Info")
    public String bookDetail(@PathVariable("book_Id") Long book_Id, Model model)
    {
        Book book = this.bookService.get(book_Id);
        model.addAttribute("book",book);
        return "admin/book_info";
    }



    //  manager Page
    @GetMapping("/manage-manager/{page}")
    public String managerPage(@PathVariable("page") int page, Model model){
        model.addAttribute("title","Admin | Manager");
        model.addAttribute("back","green");
        Pageable pageable = PageRequest.of(page, 5);
        Page<Employee> employees = this.employeeService.allManager(pageable);

        model.addAttribute("employees", employees.getContent());
        model.addAttribute("currentPage", employees.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", employees.getTotalPages());
        return "admin/manage-Manager";
    }

    //Add Employee- Manager
    @PostMapping("/add-employeeData")
    public String addEmployeeData(@ModelAttribute("customer")Customer customer, @ModelAttribute("address")Address address, @ModelAttribute("employee")Employee employee,@RequestParam("password2") String password2,HttpSession session){

        try{
            if(password2.equals(customer.getCustomerPassword())){
                Customer check = this.customerService.FindByEmailId(customer.getCustomerEmailId());
                if(check == null){
                    address.setFullName(customer.getCustomerName());
                    address.setPhoneNumber(employee.getEmployeePhoneNumber());
                    customer.setCustomerRole("ROLE_EMPLOYEE-M");
                    employee.setEmployeePosition("Manager");
                    customer.setEnable(false);
                    customer.setCustomerPassword(this.passwordEncoder.encode(customer.getCustomerPassword()));
                    customer.setCustomerJoinDate(LocalDate.now());
                    Customer customer1 = this.customerService.add(customer);
                    address.setCustomer(customer1);
                    this.addressService.add(address);
                    employee.setCustomer(customer1);
                    this.employeeService.add(employee);
                    session.setAttribute("message", new Message("Successfully Register Employee", "success"));

                }
                else {
                    session.setAttribute("message", new Message("Check Your Email , This Email is Already Register !!!", "danger"));
                }
              }
            else {
                session.setAttribute("message", new Message("Password dose not Match!! Try Again !!", "danger"));

            }
        }
        catch (Exception e){
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }

        return "redirect:/admin/manage-manager/0";
    }

//Delete manager or Ben

    @PostMapping("/delete-manager")
    @ResponseBody
    public String deleteManager(@RequestParam("employee_id") String employeeId){
        try{
            System.out.println(employeeId);
            Employee employee = this.employeeService.get(Long.valueOf(employeeId));
            System.out.println(employee.getCustomer().getCustomerName());
            this.employeeService.delete(employee);
            this.logger.info("Manager has been Ban!!");
        }catch (Exception e){
            e.getMessage();

            this.logger.info("something went wrong during a manager ban time");
        }

        return "Manager has been deleted";
    }



  //Employee info

    @GetMapping("/{managerId}/manager-info")
    public String managerInfo(@PathVariable("managerId")Long managerId,  Model model){
        model.addAttribute("title","Admin | Manager-Information");
        Employee employee = this.employeeService.get(managerId);
        model.addAttribute("employee",employee);
        return "admin/manager-info";
    }
    @GetMapping("/manage-staff/{page}")
    public String manageStaff(@PathVariable("page") int page,Model model){
        try{
            model.addAttribute("title","Admin | Staff");
            Pageable pageable = PageRequest.of(page, 5);
            Page<Employee> employees = this.employeeService.allStaff(pageable);

            model.addAttribute("employees", employees.getContent());
            model.addAttribute("currentPage", employees.getNumber()); // Page number is zero-based, so we add 1
            model.addAttribute("totalPages", employees.getTotalPages());
        }
        catch (Exception e){
            e.getMessage();

        }

        return "admin/manage-staff";
    }



    //Add Employee- staff
    @PostMapping("/add-staffData")
    public String addStaffData(@ModelAttribute("customer")Customer customer, @ModelAttribute("address")Address address, @ModelAttribute("employee")Employee employee,@RequestParam("password2") String password2,HttpSession session){

        try{
            if(password2.equals(customer.getCustomerPassword())){
                Customer check = this.customerService.FindByEmailId(customer.getCustomerEmailId());
                if(check == null){
                    address.setFullName(customer.getCustomerName());
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

                }
                else {
                    session.setAttribute("message", new Message("Check Your Email , This Email is Already Register !!!", "danger"));
                }
            }
            else {
                session.setAttribute("message", new Message("Password dose not Match!! Try Again !!", "danger"));

            }
        }
        catch (Exception e){
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }

        return "redirect:/admin/manage-staff/0";
    }

     //delete staff


//Delete manager or Ben

    @PostMapping("/delete-staff")
    @ResponseBody
    public String deleteStaff(@RequestParam("staff_id") String staffId){
        try{
            Employee employee = this.employeeService.get(Long.valueOf(staffId));
            this.employeeService.delete(employee);
            this.logger.info("Staff has been Ban!!");
        }catch (Exception e){
            e.getMessage();

            this.logger.info("something went wrong during a Staff ban time");
        }

        return "Manager has been deleted";
    }


    // customer
    @GetMapping("/manage-customer/{page}")
    public String manageCustomer(@PathVariable("page")int page, Model model){
        model.addAttribute("title","Admin | Customer");
        Pageable pageable=PageRequest.of(page,5);
        Page<Customer> customers = this.customerService.findAllPagination(pageable);
        model.addAttribute("customers", customers.getContent());
        model.addAttribute("currentPage", customers.getNumber()); // Page number is zero-based, so we add 1
        model.addAttribute("totalPages", customers.getTotalPages());
        return "admin/manage-customer";
    }

    @GetMapping("/{customer_id}/delete-customer")
    public String deleteCustomerId(@PathVariable("customer_id") Long customer_Id,HttpSession session){
        try{
            Customer customer = this.customerService.findById(customer_Id);
            this.customerService.delete(customer);
            session.setAttribute("message", new Message("Successfully Delete Customer", "success"));

        }
        catch (Exception e){
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));

        }
        return "redirect:/admin/manage-customer/0";
    }


    //all pending order page
    @GetMapping("/pending-order/{page}")
    public String allPendingOrder(@PathVariable("page") int page,Model model){
        try {
            model.addAttribute("title","Admin | Pending-Order");
            Pageable pageable=PageRequest.of(page,5);
            Page<Order> orders = this.orderService.allPendingOrder(pageable);

            model.addAttribute("orders", orders.getContent());
            model.addAttribute("currentPage", orders.getNumber()); // Page number is zero-based, so we add 1
            model.addAttribute("totalPages", orders.getTotalPages());
        }catch (Exception e){
            e.getMessage();
        }
        return "admin/pending-order";
    }


    //order detail

    @GetMapping("/{orderId}/order-detail")
    public String orderDetail(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("title","Admin | order-information");
        try {
            Order order = this.orderService.get(orderId);
            model.addAttribute("order", order);
        } catch (Exception e) {
            e.getMessage();
        }
        return "admin/order-detail";
    }


    //all delivered order page
    @GetMapping("/delivered-order/{page}")
    public String allDeliveredOrder(@PathVariable("page") int page,Model model){
        try {
            model.addAttribute("title","Admin | Delivered-Order");
            Pageable pageable=PageRequest.of(page,5);
            Page<Order> orders = this.orderService.allDeliveredOrder(pageable);

            model.addAttribute("orders", orders.getContent());

            model.addAttribute("currentPage", orders.getNumber()); // Page number is zero-based, so we add 1
            model.addAttribute("totalPages", orders.getTotalPages());
        }catch (Exception e){
            e.getMessage();
        }
        return "admin/delivered-order";
    }


    //account Page
    @GetMapping("/account")
    public String accountPage(Model model){
        model.addAttribute("title","ADMIN | Account");
        return "admin/account";
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
        return "redirect:/admin/account";
    }


    @PostMapping("/change-password")
    public String updatePassword(@RequestParam("customerId") String id,@RequestParam("password1") String password1,@RequestParam("password2") String password2,HttpSession session){

        try{
            if(password2.equals(password1)){
                Customer customer = this.customerService.findById(Long.valueOf(id));
                customer.setCustomerPassword(this.passwordEncoder.encode(password1));
                this.customerService.add(customer);
                session.setAttribute("message", new Message("Successfully Change Password!!", "success"));
                this.logger.info("change password "+password1);
            }
            else {
                session.setAttribute("message", new Message("Dose not match password!!", "danger"));
            }
        }catch (Exception e){
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            e.getMessage();
        }

        return "redirect:/admin/account";
    }


    //show all transcation detail
    @GetMapping("/transaction-detail/{page}")
    public String showAllTransaction(@PathVariable("page")int page, Model model){
        try{
            Pageable pageable=PageRequest.of(page,5);

            Page<Transaction> transactions = this.transactionService.findAllPagination(pageable);
            model.addAttribute("transactions", transactions.getContent());

            model.addAttribute("currentPage", transactions.getNumber());
            model.addAttribute("totalPages", transactions.getTotalPages());

        }catch (Exception e){
            e.getMessage();
        }
      return "admin/Transction-Detail";
    }


    //change role

    @PostMapping("/change-role")
    @ResponseBody
    public String changeUserRole(@RequestParam("newRole") String newRole, @RequestParam("customerId") String customerId) {

        Customer customer = this.customerService.findById(Long.valueOf(customerId));
        customer.setCustomerRole(newRole);
        this.customerService.add(customer);
        logger.info("Role Updated!!");
        return "Role Successfully Update";

    }


}
