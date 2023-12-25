package com.book.store.controller;

import com.book.store.entities.Book;
import com.book.store.entities.BookCategory;
import com.book.store.entities.Customer;
import com.book.store.entities.FeedBack;
import com.book.store.repository.FeedBackRepository;
import com.book.store.services.BookCategoryService;
import com.book.store.services.BookService;
import com.book.store.services.CustomerService;
import com.book.store.services.FeedBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    private CustomerService  customerService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private BookService bookService;
    @Autowired
    private BookCategoryService bookCategoryService;

    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private FeedBackService feedBackService;


    @ModelAttribute
    public void commonData(Model model){
        List<BookCategory> categories = this.bookCategoryService.findAllCategory();
        model.addAttribute("categories",categories);

    }

    //index page
    @GetMapping
    public String home(Model model){
        model.addAttribute("title","Online Book Store");

        List<Book> newBook = this.bookService.findNewBook();
        List<Book> oldBook = this.bookService.findOldBook();
        List<Book> bestSellerBook=this.bookService.findBestSellerBook();

        model.addAttribute("newBook",newBook);
        model.addAttribute("oldBook",oldBook);

        model.addAttribute("bestSellerBook",bestSellerBook);

        return "index";
    }

    //sign in page
    @GetMapping("/signin")
    public String signin(Model model){
        model.addAttribute("title","Customer | Login-Page");
        return "signin";
    }

    //signup page
    @GetMapping("/customer-signup")
    public String signup(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("title", "Customer | Register-Page");
        return "signup";
    }

    //register data handle
    @PostMapping("/doSignup")
    @ResponseBody
    public ResponseEntity<?> doSignup(@ModelAttribute("customer") Customer customer) {
        try {

            Customer customer1 = this.customerService.FindByEmailId(customer.getCustomerEmailId());
            if (customer1 ==  null){
                customer.setEnable(false);
                customer.setCustomerJoinDate(LocalDate.now());
                customer.setCustomerRole("ROLE_CUSTOMER");
                customer.setCustomerPassword(this.passwordEncoder.encode(customer.getCustomerPassword()));
                this.customerService.add(customer);
                this.logger.info("SuccessFull Register " + customer.getCustomerEmailId());
            }
            else {
                return ResponseEntity.ok("Already Register This Email!!");
            }
        } catch (Exception e) {
            this.logger.info("Something Went Wrong " + customer.getCustomerEmailId());
            return ResponseEntity.ok("error");
        }

        return ResponseEntity.ok("success");

    }


    //book information

    @GetMapping("/{bookId}/book-detail")
    public String bookInformation(@PathVariable("bookId") Long bookId, Model model){
        Book book = this.bookService.get(bookId);

        List<FeedBack> feedbacks = this.feedBackService.findByBook(book);
        model.addAttribute("title","Book-Detail");
        model.addAttribute("book",book);
        model.addAttribute("feedbacks",feedbacks);

        return "book-detail";
    }


    @GetMapping("/all-product")
    public String allBookShow(Model model){
        model.addAttribute("title","All - Books");
        List<Book> books = this.bookService.findAll();
        model.addAttribute("books",books);
        return "all-bookshow";
    }

    @GetMapping("/books/{categoryTitle}")
    public String categoryViseBookShow(@PathVariable("categoryTitle") String categoryTitle,Model model){
        try{
            List<Book> books = this.bookService.findByBookCategoryTitle(categoryTitle);

            model.addAttribute("categoryTitle",categoryTitle);
            model.addAttribute("books",books);
        }catch (Exception e){
            e.getMessage();
        }
        return "category-wise-book";
    }

    @PostMapping("/search-book")
    public String searchBook(@RequestParam("bookText")String bookText, Model model){
        try{
            System.out.println(bookText+"----------------");
            List<Book> books = this.bookService.searchBook(bookText);
            model.addAttribute("books",books);
        }catch (Exception e){
            e.getMessage();
        }
        return "search-books";
    }

}
