package com.book.store.services;

import com.book.store.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    Employee add(Employee  employee);

    Employee get(Long id);

    List<Employee> findAll();

    Page<Employee>  findAllPagination(Pageable pageable);

    Employee findByCustomerId(Long customerId);

    void delete(Employee employee);

    List<Employee> searchManager(String query);

    List<Employee> searchStaff(String query);

    Page<Employee> allManager(Pageable pageable);

    Page<Employee> allStaff(Pageable pageable);

    Page<Employee> findAllStaffPincode(String pinCode,Pageable pageable);


    List<Employee> searchEmployeePincode(String query,String pincode);

    List<Employee> findAllStaffPariticularPinCode(String pincode);


    int countStaffPariticularPinCode(String pincode);


    int countAllStaff();

    int countAllManager();


}
