package com.book.store.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePayLoad {

    private Long empId;

    private String employeeName;

    private String employeeEmail;

    private String phoneNumber;

    private String pincode;

    private String salary;



}
