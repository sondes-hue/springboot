package com.vermeg.bookland.controllers;

import com.vermeg.bookland.dtos.*;
import com.vermeg.bookland.entities.User;
import com.vermeg.bookland.repositories.UserRepository;
import com.vermeg.bookland.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class HomeController {

    List<BookDto> books = new ArrayList<>();

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;

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
    public String verification(Model model,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttrs){
        String email = (String) request.getSession().getAttribute("email");
        if(email == null)
            email = (String) model.asMap().get("email");
        else
            request.getSession().removeAttribute("email");

        VerificationDto verificationDto = new VerificationDto(email);
        if(model.asMap().get("to") != null && model.asMap().get("to").equals("forgotPassword"))
            verificationDto.setTo(model.asMap().get("to").toString());

        model.addAttribute("verificationDto",verificationDto);
        return "home/verification";
    }

    @PostMapping("/verify")
    public String verify(@Valid VerificationDto verificationDto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs){

        if(userRepository.existsByEmailAndVerification(verificationDto.getEmail(),verificationDto.getVerification())){
            User user = userRepository.findUserByEmail(verificationDto.getEmail());
            user.setVerification(0);
            userRepository.save(user);
            if(verificationDto.getTo() != null && verificationDto.getTo().equals("forgotPassword")){
                redirectAttrs.addFlashAttribute("email", user.getEmail());
                return "redirect:/changePassword";
            }
            return "redirect:/home";
        }
        result.rejectValue("verification", "error.verificationDto", "Invalid verification code");
        return "home/verification";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model){
        model.addAttribute("emailDto",new EmailDto());
        return "home/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@Valid EmailDto emailDto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttrs) throws MessagingException {
        if(userRepository.existsByEmail(emailDto.getEmail())){
            User user = userRepository.findUserByEmail(emailDto.getEmail());

            int verificationCode = (int)Math.floor(Math.random()*(99999-10000+1)+10000);
            Map<String,Object> templateModel = new HashMap<>();
            templateModel.put("code",verificationCode);
            templateModel.put("name",user.getName()+" "+user.getSurname().toUpperCase());
            emailService.sendEmailUsingThymeleaf(user.getEmail(),templateModel,"Reset your password");

            user.setVerification(verificationCode);
            userRepository.save(user);

            redirectAttrs.addFlashAttribute("email", emailDto.getEmail());
            redirectAttrs.addFlashAttribute("to", "forgotPassword");
            return "redirect:/verification";
        }
        result.rejectValue("email", "error.emailDto", "There is no account with this email");
        return "home/forgotPassword";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model){
        String email = (String) model.asMap().get("email");
        model.addAttribute("changePasswordDto",new ChangePasswordDto(email));
        return "home/changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid ChangePasswordDto changePasswordDto, BindingResult result, Model model){
        if(changePasswordDto.getPassword() == null || changePasswordDto.getPassword().length() < 6){
            result.rejectValue("password", "error.changePasswordDto", "Password must contain 6 characters at least");
            return "home/changePassword";
        }else if(!changePasswordDto.getPassword().equals(changePasswordDto.getRepeatPassword())){
            result.rejectValue("password", "error.changePasswordDto", "Password don't match");
            return "home/changePassword";
        }else {
            User user = userRepository.findUserByEmail(changePasswordDto.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getPassword()));
            userRepository.save(user);
        }
        return "redirect:/home";
    }

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }

}
