package com.vermeg.bookland.controllers;

import com.vermeg.bookland.Dtos.BookDto;
import com.vermeg.bookland.Dtos.ClientDto;
import com.vermeg.bookland.entities.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    List<BookDto> books = new ArrayList<>();

    @GetMapping("/home")
    public String home(Model model){
        books.clear();
        books.add(new BookDto(1,"title 1","Desdf ksjfd kjsf kjsdf khsdf ksdf ksjdf kjdf","ddd"));
        books.add(new BookDto(2,"title 2","Desdf ksjfd kjsf kjsdf khsdf ksdf ksjdf kjdf","ddd"));
        books.add(new BookDto(3,"title 3","Desdf ksjfd kjsf kjsdf khsdf ksdf ksjdf kjdf","ddd"));
        model.addAttribute("books",books);
        return "home/home";
    }

    @GetMapping("/index")
    public String index(){
        return "redirect:home";
    }

    @GetMapping("/")
    public String redirect(){
        return "redirect:home";
    }

    @GetMapping("/signUp")
    public String redirect(Model model){
        model.addAttribute("clientDto",new ClientDto());
        return "home/signUp";
    }
}
