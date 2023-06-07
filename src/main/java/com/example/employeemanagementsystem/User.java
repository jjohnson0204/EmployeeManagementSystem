package com.example.employeemanagementsystem;

public class User {
    private Integer id;
    private String username;
    private String password;

    public User(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() { return  id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return username + " with ID: " + id + " has password: " + password;
    }
}