package com.example.ejb.remote.singletonbean;


import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;

@Singleton
public class Scheduler {
    @Schedule(hour = "*", minute = "*/1", persistent = false)
    public void generateReports() {
        System.out.println("Hello after 1 minute");
    }
}