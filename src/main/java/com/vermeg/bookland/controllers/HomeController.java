package com.vermeg.bookland.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(){
        return "home/home";
    }
    @GetMapping("/index")
    public String index(){
        return "home/home";
    }
    @GetMapping("/")
    public String redirect(){
        return "home/home";
    }
}
