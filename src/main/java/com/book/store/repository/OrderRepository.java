package com.book.store.repository;

import com.book.store.entities.Book;
import com.book.store.entities.Customer;
import com.book.store.entities.Employee;
import com.book.store.entities.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String>{

        //find all order for customer dashboard side
        @Query("select o from Order as o where o.customer =:customer")
        public List<Order> findAllOrderParticularCustomer(@Param("customer")Customer customer);

        //find all order particular pincode for all mangaer but dose not assign any employee
        @Query("SELECT o FROM Order o WHERE o.customer.address.pinCode =:pinCode AND o.employee IS NOT NULL")
        public Page<Order> findAllOrderParticularManagerWithPincode(@Param("pinCode")String pinCode, Pageable pageable);


        //count pending order manager using particular pincode
        @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.address.pinCode =:pinCode AND (o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'OUTOFDELIVERY')")
        public int countPendingOrderParticularPincodeForManage(@Param("pinCode") String pinCode);

        //count Delivered order manager using particular pincode
        @Query("SELECT COUNT(o) FROM Order o WHERE o.customer.address.pinCode =:pinCode AND o.orderStatus = 'DELIVERED'")
        public int countDeliveredOrderParticularPincodeForManage(@Param("pinCode") String pinCode);


        //find all order particular pincode for all mangaer  assign employee
        @Query("SELECT o FROM Order o WHERE o.customer.address.pinCode =:pinCode AND o.employee IS NULL")
        public List<Order> findAllOrderParticularManagerWithPincodeNotEmployee(@Param("pinCode")String pinCode);

        //find order for staff order is confirm or shipped or out of delivery
        @Query("SELECT o FROM Order o WHERE o.employee = :employee AND (o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'OUTOFDELIVERY')")
        public List<Order>  findOrderForEmployee(@Param("employee")Employee employee);

        //count order for staff order is confirm or shipped or out of delivery
        @Query("SELECT COUNT(o) FROM Order o where o.employee =:employee AND (o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'OUTOFDELIVERY')")
        public int  countOrderForEmployee(@Param("employee")Employee employee);


        //find order for employee oder is completed
        @Query("SELECT o FROM Order o where o.employee =:employee AND o.orderStatus = 'DELIVERED'")
        public Page<Order>  findOrderForStaffDelivered(@Param("employee") Employee  employee,Pageable pageable);


        //particular employee total order
        @Query("SELECT COUNT(o) FROM Order o where o.employee =:employee")
        public int  countTotalOrderOfEmployee(@Param("employee") Employee employee);

        //count Particular Employee Pending Order
        @Query("SELECT COUNT(o) FROM Order o where o.employee =:employee AND (o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'OUTOFDELIVERY')")
        public int countPendingOrderOfEmployee(@Param("employee") Employee employee);

        //count Particular Employee Delivered Order
        @Query("SELECT COUNT(o) FROM Order o where o.employee =:employee AND o.orderStatus = 'DELIVERED'")
        public int countDeliveredOrderOfEmployee(@Param("employee") Employee employee);


        //find all Pending Order
        @Query("SELECT o FROM Order o where o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'OUTOFDELIVERY' ")
        public Page<Order> allPendingOrder(Pageable pageable);

        //Find All Delivered Order
        @Query("SELECT o FROM Order o where o.orderStatus = 'DELIVERED'")
        public Page<Order> allDeliveredOrder(Pageable pageable);

        //search Pending order
        @Query("SELECT o from Order o WHERE (o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'OUTOFDELIVERY') AND (o.orderId LIKE %:query% OR o.book.bookAuthor LIKE %:query% OR o.book.bookISBN LIKE %:query% OR o.book.bookTitle LIKE %:query% OR o.customer.address.pinCode LIKE %:query%)")
        public List<Order> searchPendingOrder(@Param("query") String query);

        //search Delivered order
        @Query("SELECT o from Order o WHERE o.orderStatus = 'DELIVERED'  AND (o.orderId LIKE %:query% OR o.book.bookAuthor LIKE %:query% OR o.book.bookISBN LIKE %:query% OR o.book.bookTitle LIKE %:query% OR o.customer.address.pinCode LIKE %:query%)")
        public List<Order> searchDeliveredOrder(@Param("query") String query);
        //count all order
        @Query("SELECT COUNT(o) FROM Order AS o")
        public int countALlOrder();


        //findOrder of Current date with particular staff
        @Query("SELECT o FROM Order o where o.employee =:employee AND o.outOfDeliveryDate =:date")
        public List<Order> findCurrentDateOrderForStaff(@Param("date")LocalDate date,@Param("employee") Employee employee);

        }
