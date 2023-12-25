package com.book.store.controller;

import com.book.store.entities.*;
import com.book.store.payload.EmployeePayLoad;
import com.book.store.payload.OrderPayLoad;
import com.book.store.payload.TransactionPayLoad;
import com.book.store.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SearchController {

    @Autowired
    private BookCategoryService bookCategoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionService transactionService;


    //search geners
    @GetMapping("/search-book-category/{bookCategoryTitle}")
    public ResponseEntity<?> searchBookCategory(@PathVariable String bookCategoryTitle){
        return ResponseEntity.ok(this.bookCategoryService.searchByTitle(bookCategoryTitle));
    }

    //search book
    @GetMapping("/search-book/{query}")
    public ResponseEntity<?> searchBook(@PathVariable String query){
        System.out.println(query);
        List<Book> books = this.bookService.searchBook(query);
        System.out.println(books);
        return ResponseEntity.ok(books);
    }


    //search Manager
    @GetMapping("/search-manager/{query}")
    public ResponseEntity<?> searchManager(@PathVariable String query){
        List<Employee> search = this.employeeService.searchManager(query);

        List<EmployeePayLoad> managers= new ArrayList<>();
          for(Employee manager:search){
              EmployeePayLoad build = EmployeePayLoad.builder().
                      employeeName(manager.getCustomer().getCustomerName()).
                      employeeEmail(manager.getCustomer().getCustomerEmailId()).
                      empId(manager.getEmployeeId()).
                      phoneNumber(manager.getEmployeePhoneNumber()).
                      pincode(manager.getCustomer().getAddress().getPinCode()).
                      salary(manager.getEmployeeSalary()).
                      build();
              managers.add(build);
          }
        return ResponseEntity.ok(managers);
    }

    //search staff
    @GetMapping("/search-staff/{query}")
    public ResponseEntity<?> searchStaff(@PathVariable String query){
        List<Employee> search = this.employeeService.searchStaff(query);

        List<EmployeePayLoad> staffs= new ArrayList<>();
        for(Employee staff:search){
            EmployeePayLoad build = EmployeePayLoad.builder().
                    employeeName(staff.getCustomer().getCustomerName()).
                    employeeEmail(staff.getCustomer().getCustomerEmailId()).
                    empId(staff.getEmployeeId()).
                    phoneNumber(staff.getEmployeePhoneNumber()).
                    pincode(staff.getCustomer().getAddress().getPinCode()).
                    salary(staff.getEmployeeSalary()).
                    build();
            staffs.add(build);
        }
        return ResponseEntity.ok(staffs);
    }

    //search customer
    @GetMapping("/search-customer/{query}")
    public ResponseEntity<?> searchCustomer(@PathVariable String query){

        List<Customer> customers = this.customerService.searchCustomer(query);


        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search-staffm/{query}")
    public ResponseEntity<?> searchStaffm(@PathVariable String query, Principal principal){
        Customer customer = this.customerService.FindByEmailId(principal.getName());
        List<Employee> search = this.employeeService.searchEmployeePincode(query,customer.getAddress().getPinCode());

        List<EmployeePayLoad> staffs= new ArrayList<>();
        for(Employee staff:search){
            EmployeePayLoad build = EmployeePayLoad.builder().
                    employeeName(staff.getCustomer().getCustomerName()).
                    employeeEmail(staff.getCustomer().getCustomerEmailId()).
                    empId(staff.getEmployeeId()).
                    phoneNumber(staff.getEmployeePhoneNumber()).
                    pincode(staff.getCustomer().getAddress().getPinCode()).
                    salary(staff.getEmployeeSalary()).
                    build();
            staffs.add(build);
        }
        return ResponseEntity.ok(staffs);
    }


    @GetMapping("/search-pending-order/{query}")
    public ResponseEntity<?> searchPendingOrder(@PathVariable String query){
        List<Order> orders = this.orderService.searchPendingOrder(query);
        List<OrderPayLoad> orders2= new ArrayList<>();
         for(Order order:orders){
             OrderPayLoad orderPayLoad = OrderPayLoad.builder()
                     .orderId(order.getOrderId())
                     .bookAuthor(order.getBook().getBookAuthor())
                     .bookImage(order.getBook().getBookImageUrl())
                     .bookQuantity(order.getBookQuantity())
                     .bookTitle(order.getBook().getBookTitle())
                     .payAmount(order.getTransaction().getAmount()).build();
                      orders2.add(orderPayLoad);
         }

        return ResponseEntity.ok(orders2);
    }

    @GetMapping("/search-delivered-order/{query}")
    public ResponseEntity<?> searchDeliveredOrder(@PathVariable String query){
        List<Order> orders = this.orderService.searchDeliveredOrder(query);
        List<OrderPayLoad> orders2= new ArrayList<>();
        for(Order order:orders){
            OrderPayLoad orderPayLoad = OrderPayLoad.builder()
                    .orderId(order.getOrderId())
                    .bookAuthor(order.getBook().getBookAuthor())
                    .bookImage(order.getBook().getBookImageUrl())
                    .bookQuantity(order.getBookQuantity())
                    .bookTitle(order.getBook().getBookTitle())
                    .payAmount(order.getTransaction().getAmount()).build();
            orders2.add(orderPayLoad);
        }

        return ResponseEntity.ok(orders2);
    }


    @GetMapping("/transaction/{query}")
    public ResponseEntity<?> searchTransaction(@PathVariable String query){
        List<Transaction> transactions = this.transactionService.searchTransaction(query);
         List<TransactionPayLoad> transactions2=new ArrayList<>();

         for (Transaction t:transactions){
             TransactionPayLoad transactionPayLoad = TransactionPayLoad.builder()
                     .paymentId(t.getPaymentId())
                     .orderId(t.getOrder().getOrderId())
                     .amount(t.getAmount())
                     .bookImage(t.getOrder().getBook().getBookImageUrl())
                     .bookTitle(t.getOrder().getBook().getBookTitle())
                     .transcationStatus(t.getTranscationStatus())
                     .bookId(t.getOrder().getBook().getBookId())
                     .build();
             transactions2.add(transactionPayLoad);
         }
        return ResponseEntity.ok(transactions2);
    }
}
