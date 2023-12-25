package com.book.store.repository;

import com.book.store.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    //By Coustomer Id
    @Query("SELECT e FROM Employee e where e.customer.customerId =:customerId")
    Employee findByCustomerId(@Param("customerId") Long customerId);

    //Find All Manager
    @Query("SELECT e FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-M'")
    Page<Employee> allManager(Pageable pageable);

    //find All Staff
    @Query("SELECT e FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S'")
    Page<Employee> allStaff(Pageable pageable);


    //Find All Staff Particular Pincode
    @Query("SELECT e FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S' AND e.customer.address.pinCode =:pinCode")
    Page<Employee> allStaffWithPincode(@Param("pinCode") String pinCode,Pageable pageable);

    //search Manager
    @Query("SELECT e from Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-M' AND (e.customer.customerName LIKE %:query% OR e.customer.address.pinCode LIKE %:query%)")
    List<Employee> searchManager(@Param("query") String query);

    //search Staff
    @Query("SELECT e from Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S' AND (e.customer.customerName LIKE %:query% OR e.customer.address.pinCode LIKE %:query%)")
    List<Employee> searchStaff(@Param("query") String query);
    //search Staff For Particular Pincode
    @Query("SELECT e from Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S' AND e.customer.address.pinCode =:pinCode AND (e.customer.customerName LIKE %:query% OR e.customer.address.pinCode LIKE %:query%)")
    List<Employee> searchStaffManagerDashboardPincode(@Param("query") String query,@Param("pinCode") String pinCode);
    //find All Staff For Particular Pincode
    @Query("SELECT e FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S' AND e.customer.address.pinCode =:pinCode")
    public List<Employee> findAllStaffPariticularPinCode(@Param("pinCode") String pincode);
    //Count Staff Particular Pincode
    @Query("SELECT COUNT(e) FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S' AND e.customer.address.pinCode =:pinCode")
    public int countStaffPariticularPinCode(@Param("pinCode") String pincode);


    //count all Staff
    @Query("SELECT COUNT(e) FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-S'")
    public int countAllStaff();

    //count all Manager
    @Query("SELECT COUNT(e) FROM Employee e where e.customer.customerRole = 'ROLE_EMPLOYEE-M'")
    public int countAllManager();



}
