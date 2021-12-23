package com.vermeg.bookland;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vermeg.bookland.controllers.ControllerBook;

@SpringBootApplication
public class BooklandApplication {

    public static void main(String[] args) {
    	new File(ControllerBook.uploadDirectory).mkdir();
		new File(ControllerBook.pdfDirectory).mkdir();
        SpringApplication.run(BooklandApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
