package com.example.employeemanagementsystem;

import java.sql.Date;

public class EmployeeData {
    private Integer employeeID;
    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNum;
    private String position;
    private String image;
    private Date date;
    private Double salary;

    public EmployeeData(
            Integer employeeID, String firstName, String lastName, String gender,
            String phoneNum, String position, String image, Date date) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.position = position;
        this.image = image;
        this.date = date;
    }
    public EmployeeData(Integer employeeID, String firstName, String lastName, String position, Double salary) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPosition() {
        return position;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public Double getSalary() {
        return salary;
    }
}
