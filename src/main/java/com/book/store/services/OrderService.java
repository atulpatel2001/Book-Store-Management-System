package com.book.store.services;

import com.book.store.entities.Customer;
import com.book.store.entities.Employee;
import com.book.store.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {


    public Order add(Order order);

    public Order get(String id);


    public List<Order>  findAll();

    public void delete(Order order);

    public List<Order>  findAllCustomerOrder(Customer customer);



    public Page<Order> findAllOrderParticularManagerWithPincode(String pincode, Pageable pageable);


    public List<Order>  findAllOrderParticularManagerWithPincodeNotEmployee(String pincode);


    public List<Order> findOrderForEmployee(Employee employee);

    public int countOrderForEmployee(Employee employee);


    public Page<Order> findOrderForStaffDelivered(Employee employee,Pageable pageable);


    public int countPendingOrderParticularPincodeForManage(String pinCode);

    public int countDeliveredOrderParticularPincodeForManage(String pinCode);


    public int countTotalOrderOfEmployee(Employee employee);

    public int countPendingOrderOfEmployee(Employee employee);

    public int countDeliveredOrderOfEmployee(Employee employee);


    public Page<Order> allPendingOrder(Pageable pageable);

    public Page<Order> allDeliveredOrder(Pageable pageable);


    public List<Order> searchPendingOrder(String query);

    public List<Order> searchDeliveredOrder(String query);

    public int countALlOrder();

    List<Order> findCurrentDateOrderForStaff(LocalDate date,Employee employee);
}

