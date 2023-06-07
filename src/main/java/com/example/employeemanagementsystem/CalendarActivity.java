package com.example.employeemanagementsystem;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private ZonedDateTime date;
    private String username;
    private Integer hoursWorked;

    public CalendarActivity(ZonedDateTime date, String username, Integer hoursWorked) {
        this.date = date;
        this.username = username;
        this.hoursWorked = hoursWorked;
    }

    public ZonedDateTime getDate() { return date; }
    public void setDate(ZonedDateTime date) { this.date = date; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Integer hoursWorked) { this.hoursWorked = hoursWorked; }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", username='" + username + '\'' +
                ", hoursWorked=" + hoursWorked +
                '}';
    }
}