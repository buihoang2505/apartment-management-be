package com.apartment.apartmentmanagement;

import com.apartment.Application;
import org.springframework.boot.SpringApplication;

/**
 * Legacy entry point — delegates to com.apartment.Application.
 * Use Application.java as the canonical main class.
 */
public class ApartmentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}