package com.vermeg.bookland.controllers;

import com.vermeg.bookland.dtos.BookDto;
import com.vermeg.bookland.dtos.LoginDto;
import com.vermeg.bookland.dtos.UserDto;
import com.vermeg.bookland.dtos.VerificationDto;
import com.vermeg.bookland.entities.Client;
import com.vermeg.bookland.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class HomeController {

    List<BookDto> books = new ArrayList<>();

    @Autowired
    ClientRepository clientRepository;

    @GetMapping({"/home", "/index","/"})
    public String home(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        Collection <GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        for(GrantedAuthority authority:authorities){
            role = authority.toString();
            break;
        }
        if(!role.equals("anonymousUser")){
            if(role.equals("ROLE_USER")){
                return "redirect:/user/home";
            }else if(role.equals("ROLE_ADMIN")){
                return "redirect:/admin/home";
            }
        }

        model = getBooks(model);
        model.addAttribute("view","home");
        return "home/home";
    }

    private Model getBooks(Model model) {
        books.clear();
        books.add(new BookDto(1,"title 1","Desdf ksjfd kjsf kjsdf khsdf ksdf ksjdf kjdf","ddd"));
        books.add(new BookDto(2,"title 2","Desdf ksjfd kjsf kjsdf khsdf ksdf ksjdf kjdf","ddd"));
        books.add(new BookDto(3,"title 3","Desdf ksjfd kjsf kjsdf khsdf ksdf ksjdf kjdf","ddd"));
        model.addAttribute("books",books);
        if(model.asMap().get("loginDto") == null)
            model.addAttribute("loginDto",new LoginDto());
        return model;
    }

    @GetMapping("/signUp")
    public String redirect(Model model){
        model.addAttribute("userDto",new UserDto());
        return "home/signUp";
    }

    /*@PostMapping("/login")
    public String login(@Valid LoginDto loginDto, BindingResult result, Model model, RedirectAttributes redirectAttrs){
        Client connectedClient = clientRepository.findClientByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
        System.out.println(connectedClient);
        System.out.println("--------");
        if(connectedClient != null){
            if(connectedClient.getVerification() != 0){
                redirectAttrs.addFlashAttribute("email", loginDto.getEmail());
                return "redirect:/verification";
            }
            System.out.println("Connected as client");
            return "user/home";
        }
        model = getBooks(model);
        model.addAttribute("view","loginBox");
        return "home/home";
        return null;
    }*/

    @GetMapping("logout")
    public String logout(Model model){
        model = getBooks(model);
        model.addAttribute("view","loginBox");
        return "home/home";
    }

    @GetMapping("/verification")
    public String verification(Model model){
        String email = (String) model.asMap().get("email");
        model.addAttribute("verificationDto",new VerificationDto(email));
        return "home/verification";
    }

    @PostMapping("/verify")
    public String verify(@Valid VerificationDto verificationDto, BindingResult result, Model model){
        if(clientRepository.existsByEmailAndVerification(verificationDto.getEmail(),verificationDto.getVerification())){
            Client client = clientRepository.findClientByEmail(verificationDto.getEmail());
            client.setVerification(0);
            clientRepository.save(client);
            return "redirect:/home";
        }
        result.rejectValue("verification", "error.verificationDto", "Invalid verification code");
        return "home/verification";
    }

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }

}
