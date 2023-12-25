package com.book.store.services.imple;

import com.book.store.entities.Customer;
import com.book.store.entities.Employee;
import com.book.store.entities.Order;
import com.book.store.repository.OrderRepository;
import com.book.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class OrderServiceImple implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Order add(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public Order get(String id) {
        return this.orderRepository.findById(id).get();
    }


    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    @Override
    public void delete(Order order) {
        this.orderRepository.delete(order);
    }

    @Override
    public List<Order> findAllCustomerOrder(Customer customer) {
        return this.orderRepository.findAllOrderParticularCustomer(customer);
    }

    @Override
    public Page<Order> findAllOrderParticularManagerWithPincode(String pincode, Pageable pageable) {
        return this.orderRepository.findAllOrderParticularManagerWithPincode(pincode,pageable);
    }

    @Override
    public List<Order> findAllOrderParticularManagerWithPincodeNotEmployee(String pincode) {
        return this.orderRepository.findAllOrderParticularManagerWithPincodeNotEmployee(pincode);
    }

    @Override
    public List<Order> findOrderForEmployee(Employee employee) {
        return this.orderRepository.findOrderForEmployee(employee);
    }

    @Override
    public int countOrderForEmployee(Employee employee) {
        return this.orderRepository.countOrderForEmployee(employee);
    }

    @Override
    public Page<Order> findOrderForStaffDelivered(Employee employee,Pageable pageable) {
        return this.orderRepository.findOrderForStaffDelivered(employee,pageable);
    }

    @Override
    public int countPendingOrderParticularPincodeForManage(String pinCode) {
        return this.orderRepository.countPendingOrderParticularPincodeForManage(pinCode);
    }

    @Override
    public int countDeliveredOrderParticularPincodeForManage(String pinCode) {
        return this.orderRepository.countDeliveredOrderParticularPincodeForManage(pinCode);
    }

    @Override
    public int countTotalOrderOfEmployee(Employee employee) {
        return this.orderRepository.countTotalOrderOfEmployee(employee);
    }

    @Override
    public int countPendingOrderOfEmployee(Employee employee) {
        return this.orderRepository.countPendingOrderOfEmployee(employee);
    }

    @Override
    public int countDeliveredOrderOfEmployee(Employee employee) {
        return this.orderRepository.countDeliveredOrderOfEmployee(employee);
    }

    @Override
    public Page<Order> allPendingOrder(Pageable pageable) {
        return this.orderRepository.allPendingOrder(pageable);
    }

    @Override
    public Page<Order> allDeliveredOrder(Pageable pageable) {
        return this.orderRepository.allDeliveredOrder(pageable);
    }

    @Override
    public List<Order> searchPendingOrder(String query) {
        return this.orderRepository.searchPendingOrder(query);
    }

    @Override
    public List<Order> searchDeliveredOrder(String query) {
        return this.orderRepository.searchDeliveredOrder(query);
    }

    @Override
    public int countALlOrder() {
        return this.orderRepository.countALlOrder();
    }

    @Override
    public List<Order> findCurrentDateOrderForStaff(LocalDate date, Employee employee) {
        return this.orderRepository.findCurrentDateOrderForStaff(date,employee);
    }

}
