package com.book.store.services.imple;

import com.book.store.entities.Employee;
import com.book.store.repository.EmployeeRepository;

import com.book.store.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceImple implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Employee add(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @Override
    public Employee get(Long id) {
        return this.employeeRepository.findById(id).get();
    }

    @Override
    public List<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Page<Employee> findAllPagination(Pageable pageable) {
        return this.employeeRepository.findAll(pageable);
    }

    @Override
    public Employee findByCustomerId(Long customerId) {
        return this.employeeRepository.findByCustomerId(customerId);
    }

    @Override
    public void delete(Employee employee) {
        this.employeeRepository.delete(employee);
    }

    @Override
    public List<Employee> searchManager(String query) {
        return this.employeeRepository.searchManager(query);
    }

    @Override
    public List<Employee> searchStaff(String query) {
        return this.employeeRepository.searchStaff(query);
    }

    @Override
    public Page<Employee> allManager(Pageable pageable) {
        return this.employeeRepository.allManager(pageable);
    }

    @Override
    public Page<Employee> allStaff(Pageable pageable) {
        return this.employeeRepository.allStaff(pageable);
    }

    @Override
    public Page<Employee> findAllStaffPincode(String pinCode, Pageable pageable) {
       return this.employeeRepository.allStaffWithPincode(pinCode,pageable);
    }

    @Override
    public List<Employee> searchEmployeePincode(String query, String pincode) {
        return this.employeeRepository.searchStaffManagerDashboardPincode(query,pincode);
    }

    @Override
    public List<Employee> findAllStaffPariticularPinCode(String pincode) {
        return this.employeeRepository.findAllStaffPariticularPinCode(pincode);
    }

    @Override
    public int countStaffPariticularPinCode(String pincode) {
        return this.employeeRepository.countStaffPariticularPinCode(pincode);
    }

    @Override
    public int countAllStaff() {
        return this.employeeRepository.countAllStaff();
    }

    @Override
    public int countAllManager() {
        return this.employeeRepository.countAllManager();
    }
}
