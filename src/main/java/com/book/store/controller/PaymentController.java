package com.book.store.controller;

import com.book.store.entities.*;
import com.book.store.services.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private TransactionService transcationService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/create_order_single")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {
        int amount = Integer.parseInt(data.get("amount").toString());
        int quantity=Integer.parseInt(data.get("quantity").toString());
        int total=amount*quantity;
        var client = new RazorpayClient("rzp_test_ADEnLyqI9oALQY", "dtZFd3HrvdyoXYGLpfTrEDUv");
        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", total * 100); // amount in the smallest currency unit
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_235425");
        Order order = client.orders.create(orderRequest);

        Transaction transaction = Transaction.builder()
                .orderId(order.get("id"))
                .amount(String.valueOf(total))
                .paymentId(null)
                .receipt(order.get("receipt"))
                .transcationdate(LocalDateTime.now())
                .orderStatus("created")

        .build();

        Transaction add = this.transcationService.add(transaction);
       return  order.toString();
    }

    @PostMapping("/update_singleBookPayment")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, String> data,Principal principal) {

        Transaction transaction = this.transcationService.getByOrderId(data.get("order_id").toString());
        transaction.setPaymentId(data.get("payment_id").toString());
        transaction.setTranscationStatus(String.valueOf(OrderStatus.COMPLETED));

        transaction.setTranscationdate(LocalDateTime.now());
        Transaction transaction1 = this.transcationService.add(transaction);

        Book book = this.bookService.get(Long.valueOf(data.get("book_id")));

        Customer customer = this.customerService.FindByEmailId(principal.getName());
        int quantity= Integer.parseInt(data.get("quantity"));
        int bookQuntity= book.getBookQuantity();
        System.out.println(bookQuntity-quantity);
        book.setBookQuantity(bookQuntity-quantity);

        com.book.store.entities.Order order = com.book.store.entities.Order.builder().book(book).orderStatus(String.valueOf(OrderStatus.CONFIRMED)).customer(customer).transaction(transaction).bookQuantity(data.get("quantity").toString()).orderDate(LocalDate.now()).build();

        com.book.store.entities.Order order1 = this.orderService.add(order);

        Cart cart = this.cartService.findCartForBookAndCustomer(customer, book);
        this.cartService.delete(cart);

        String text = "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Order Confirmation</h2></div>" + "<div class='content'>" + "<p>Dear " + order1.getCustomer().getAddress().getFullName() + ",</p>" + "<p>Successfully Order Your Book. After Seven Day Your Book will be delivered. After Check your Book Give Otp to Our Delivery Boy.</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3><div class='product'><span>Book Title:</span><span>" + order1.getBook().getBookTitle() + "</span></div>" + "<div class='product'><span>Book Author:</span><span>"+order1.getBook().getBookAuthor()+"</span></div>" + "<div class='product'><span>Book Quantity:</span><span>" + order1.getBookQuantity() + "</span></div>" + "<div class='total'><span>Total Amount:</span><span>$" + order1.getTransaction().getAmount() + "</span></div>" + "</div>" + "<p></p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p></p>" + "</div>" + "</div>" + "</body></html>";

        Email email=Email.builder().massage(text).subject("Order Confirmation").to(order1.getCustomer().getCustomerEmailId()).build();
         this.emailService.sendEmail(email);
        return ResponseEntity.ok(Map.of("msg", "updated"));
    }


    //all checkout

    @PostMapping("/create_order_all")
    @ResponseBody
    public String AllCartBookCheckOut(@RequestBody Map<String, Object> data,Principal principal) throws RazorpayException {
        List<Cart> carts = this.cartService.findCartForCustomer(this.customerService.FindByEmailId(principal.getName()));
        int total=0;
        for (Cart c:carts){
            Long itemQuantity = c.getItemQuantity();
            String bookPrice = c.getBook().getBookPrice();
            total+= Integer.parseInt(bookPrice)*itemQuantity;
        }

        var client = new RazorpayClient("rzp_test_ADEnLyqI9oALQY", "dtZFd3HrvdyoXYGLpfTrEDUv");
        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", total * 100); // amount in the smallest currency unit
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_235425");
        Order order = client.orders.create(orderRequest);

        Transaction transaction = Transaction.builder()
                .orderId(order.get("id"))
                .amount(String.valueOf(total))
                .paymentId(null)
                .receipt(order.get("receipt"))
                .transcationdate(LocalDateTime.now())
                .orderStatus("created")

                .build();

        this.transcationService.add(transaction);


        return order.toString();
    }


    //update payment

    @PostMapping("/update_doubleBookPayment")
    @ResponseBody
    public ResponseEntity<?> updateOrderCheckOut(@RequestBody Map<String, String> data,Principal principal) {

        Transaction transaction = this.transcationService.getByOrderId(data.get("order_id").toString());
        transaction.setPaymentId(data.get("payment_id").toString());
        transaction.setTranscationStatus(String.valueOf(OrderStatus.COMPLETED));

        transaction.setTranscationdate(LocalDateTime.now());
        Transaction transaction1 = this.transcationService.add(transaction);
        Customer customer = this.customerService.FindByEmailId(principal.getName());

        List<Cart> carts = this.cartService.findCartForCustomer(customer);
        int i=0;
        for (Cart c:carts) {
            Book book = c.getBook();
            i++;
            transaction1.setTranscationId(transaction1.getTranscationId()+'('+i+')');

            com.book.store.entities.Order order = com.book.store.entities.Order.builder().book(book).orderStatus(String.valueOf(OrderStatus.CONFIRMED)).customer(customer).transaction(transaction1).bookQuantity(String.valueOf(c.getItemQuantity())).build();
            this.orderService.add(order);
            this.cartService.delete(c);
        }
        //Email


        return ResponseEntity.ok(Map.of("msg", "updated"));
    }


}
