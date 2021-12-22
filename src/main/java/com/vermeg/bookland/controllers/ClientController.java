package com.vermeg.bookland.controllers;

import com.vermeg.bookland.dtos.BookDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class ClientController {

    List<BookDto> books = new ArrayList<>();

    @GetMapping("/home")
    public String home(){
        return "user/home";
    }


}
