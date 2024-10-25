package com.db_ride_hailing_sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.db_ride_hailing_sys.dao")
public class DbRideHailingSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbRideHailingSysApplication.class, args);
    }

}
