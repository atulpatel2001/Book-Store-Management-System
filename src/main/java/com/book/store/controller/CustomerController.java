package com.book.store.controller;

import com.book.store.entities.*;
import com.book.store.helper.Message;
import com.book.store.services.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/purchase-book")
public class CustomerController {
    @Autowired
    private BookService bookService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private BookCategoryService bookCategoryService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private FeedBackService feedBackService;
    @Autowired
    private EmailService emailService;


    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        this.logger.info("Login By " + userName);

        // get User Detail By UserName(EmailId)
        Customer customer = this.customerService.FindByEmailId(userName);

        int count = this.cartService.countCart(customer);
        if (count == 0) {
            model.addAttribute("countCart", count);
        } else {
            model.addAttribute("countCart", count);
        }

        List<BookCategory> categories = this.bookCategoryService.findAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("customer", customer);
    }

    //customer dashboard
    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Books");
        List<Book> newBook = this.bookService.findNewBook();
        List<Book> oldBook = this.bookService.findOldBook();
        List<Book> bestSellerBook = this.bookService.findBestSellerBook();

        model.addAttribute("newBook", newBook);
        model.addAttribute("oldBook", oldBook);

        model.addAttribute("bestSellerBook", bestSellerBook);
        return "customer/index";
    }


    //book information

    @GetMapping("/{bookId}/book-detail")
    public String bookInformation(@PathVariable("bookId") Long bookId, Model model) {
        Book book = this.bookService.get(bookId);
        model.addAttribute("title", "Book-Detail");
        model.addAttribute("book", book);


        return "customer/book-detail";
    }


    //show all product
    @GetMapping("/all-product")
    public String allBookShow(Model model, Principal principal) {
        model.addAttribute("title", "All - Books");

        List<Book> books = this.bookService.findAll();

        model.addAttribute("books", books);
        return "customer/all-bookshow";
    }

    //add cart data
    @PostMapping("/add-to-cart")
    @ResponseBody
    public String addToCart(@RequestParam("book_id") String bookId, Principal principal) {
        try {
            Book book = this.bookService.get(Long.valueOf(bookId));
            Customer customer = this.customerService.FindByEmailId(principal.getName());

            Cart cart = this.cartService.findCartForBookAndCustomer(customer, book);

            if (cart == null) {
                Cart cart2 = Cart.builder().customer(customer).book(book).itemQuantity(1L).build();
                this.cartService.add(cart2);
            } else {
                cart.setItemQuantity(cart.getItemQuantity() + 1);
                this.cartService.add(cart);

            }

        } catch (Exception e) {
            e.getMessage();
        }
        return "added to cart";
    }

    //cart page
    @GetMapping("/checkout")
    public String cartPage(Model model, Principal principal) {
        List<Cart> carts = this.cartService.findCartForCustomer(this.customerService.FindByEmailId(principal.getName()));
        model.addAttribute("carts", carts);
        int totalAmount = 0;
        for (Cart c : carts) {
            Long itemQuantity = c.getItemQuantity();
            String bookPrice = c.getBook().getBookPrice();
            totalAmount += Integer.parseInt(bookPrice) * itemQuantity;
        }
        model.addAttribute("totalPrice", totalAmount);
        model.addAttribute("title", "Shopping Cart");
        return "customer/cart-page";
    }


    //quantity change
    @PostMapping("/change-quantity")
    @ResponseBody
    public String changeQuantity(@RequestParam("quantity") String quantity, @RequestParam("cartId") String cartId) {
        try {

            if (Integer.parseInt(quantity) == 0) {
                Cart cart = this.cartService.get(Long.valueOf(cartId));
                this.cartService.delete(cart);
                this.logger.info("cart has been deleted because quantity value is zero");

            } else {
                Cart cart = this.cartService.get(Long.valueOf(cartId));
                cart.setItemQuantity(Long.valueOf(quantity));
                this.cartService.add(cart);

                this.logger.info("update quantity");

            }

        } catch (Exception e) {
            e.getMessage();
        }

        return "change";
    }


    //add address
    @PostMapping("/add-address")
    public String addAddress(@ModelAttribute("address") Address address, Principal principal, HttpSession session) {

        try {
            Customer manager = this.customerService.findManagerWithPincode(address.getPinCode());
            if(manager != null) {

                Customer customer = this.customerService.FindByEmailId(principal.getName());
                address.setCustomer(customer);
                this.addressService.add(address);
                session.setAttribute("message", new Message("Successfully Add Address", "success"));
            }
            else {
                session.setAttribute("message", new Message("Do not Available Delivery in this Pincode", "danger"));

            }
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong!! Try Again !!", "danger"));

        }
        return "redirect:/purchase-book/checkout";
    }

    //update address
    @PostMapping("/update-address")
    public String updateAddress(@ModelAttribute("address") Address address, HttpSession session) {


        try {
            Address address1 = this.addressService.get(address.getAddressId());
            address1.setFullName(address.getFullName());
            address1.setPhoneNumber(address.getPhoneNumber());
            address1.setState(address.getState());
            address1.setDistrict(address.getDistrict());
            address1.setCity(address.getCity());
            address1.setPinCode(address.getPinCode());
            address1.setHouseNo(address.getHouseNo());
            address1.setBuildingName(address.getBuildingName());
            address1.setArea(address.getArea());
            address1.setColony(address.getColony());
            this.addressService.add(address1);
            session.setAttribute("message", new Message("Successfully Add Address", "success"));
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong!! Try Again !!", "danger"));

        }
        return "redirect:/purchase-book/checkout";
    }


    //delete cart
    @PostMapping("/delete-cart")
    @ResponseBody
    public String deleteCart(@RequestParam("cart_id") String cartId) {
        try {
            Cart cart = this.cartService.get(Long.valueOf(cartId));
            this.cartService.delete(cart);
        } catch (Exception e) {
            e.getMessage();
        }
        return "deleted";
    }


    @GetMapping("/account")
    public String accountPage(Model model, Principal principal) {
        model.addAttribute("title", "Customer | Account");
        Customer customer = this.customerService.FindByEmailId(principal.getName());
        List<Order> orders = this.orderService.findAllCustomerOrder(customer);

        model.addAttribute("orders", orders);
        return "customer/account";
    }


    @PostMapping("/update-profile")

    public String updateProfile(@ModelAttribute("customer") Customer customer, HttpSession session) {
        try {

            Customer customer1 = this.customerService.findById(customer.getCustomerId());
            customer1.setCustomerName(customer.getCustomerName());
            customer1.setCustomerEmailId(customer.getCustomerEmailId());
            this.customerService.add(customer1);
            session.setAttribute("message", new Message("Successfully Update Profile!!", "success"));
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
        }
        return "redirect:/purchase-book/account";
    }


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

        return "redirect:/purchase-book/account";
    }

    @GetMapping("/books/{categoryTitle}")
    public String categoryViseBookShow(@PathVariable("categoryTitle") String categoryTitle, Model model) {
        try {
            List<Book> books = this.bookService.findByBookCategoryTitle(categoryTitle);

            model.addAttribute("categoryTitle", categoryTitle);
            model.addAttribute("books", books);
        } catch (Exception e) {
            e.getMessage();
        }
        return "customer/category-wise-book";
    }


    @PostMapping("/search-book")
    public String searchBook(@RequestParam("bookText") String bookText, Model model) {
        try {
            List<Book> books = this.bookService.searchBook(bookText);
            model.addAttribute("books", books);
        } catch (Exception e) {
            e.getMessage();
        }
        return "customer/search-books";
    }

    @GetMapping("/{orderId}/order-details")
    public String orderDetails(@PathVariable("orderId") String ordeId, Model model) {
        model.addAttribute("title", "Order Information");

        Order order = this.orderService.get(ordeId);
        model.addAttribute("order", order);
        return "customer/order-details";
    }


    @GetMapping("/{orderId}/download-invoice")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable("orderId") String orderId) throws IOException {

        try {
            Order order = this.orderService.get(orderId);

            // Generate PDF content
            ByteArrayInputStream pdf = this.pdfService.createPdf(order);

            // Convert the PDF content to byte array
            byte[] pdfBytes = pdf.readAllBytes();

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + orderId + ".pdf");

            // Return the ResponseEntity with the PDF content and headers
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }


    @PostMapping("/feedback-data")
    public String feedback(@ModelAttribute("feedBack") FeedBack feedBack, @RequestParam("bookId") String bookId, HttpSession session) {
        try {

            Book book = this.bookService.get(Long.valueOf(bookId));
            feedBack.setBook(book);
            feedBack.setFeedBackDate(LocalDate.now());
            this.feedBackService.add(feedBack);
            session.setAttribute("message", new Message("Successfully Give FeedBack!!", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong!!", "danger"));

        }

        return "redirect:/purchase-book/account";
    }

    @GetMapping("/{orderId}/cancel-order")
    public String cancelBooking(@PathVariable("orderId") String orderId, HttpSession  session) {
        try {
            Order order = this.orderService.get(orderId);
            this.orderService.delete(order);

            String text = "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Order Cancelation</h2></div>" + "<div class='content'>" + "<p>Dear " + order.getCustomer().getAddress().getFullName() + ",</p>" + "<p>Successfully Cancel Order. Your Amount will be Refund in b   Three Day..</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3><div class='product'><span>Book Title:</span><span>" + order.getBook().getBookTitle() + "</span></div>" + "<div class='product'><span>Book Author:</span><span>"+order.getBook().getBookAuthor()+"</span></div>" + "<div class='product'><span>Book Quantity:</span><span>" + order.getBookQuantity() + "</span></div>" + "<div class='total'><span>Total Amount:</span><span>$" + order.getTransaction().getAmount() + "</span></div>" + "</div>" + "<p></p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p></p>" + "</div>" + "</div>" + "</body></html>";
            Email email=Email.builder().massage(text).subject("Order Confirmation").to(order.getCustomer().getCustomerEmailId()).build();
            this.emailService.sendEmail(email);
            session.setAttribute("message", new Message("Successfully delete Order!!!!", "success"));
        } catch (Exception e) {
            e.getMessage();
            session.setAttribute("message", new Message("Something Went Wrong!!", "danger"));

        }
        return "redirect:/purchase-book/account";
    }

}
