package com.vermeg.bookland.controllers;

import com.vermeg.bookland.dtos.BookDto;
import com.vermeg.bookland.dtos.UserDto;
import com.vermeg.bookland.entities.Role;
import com.vermeg.bookland.entities.User;
import com.vermeg.bookland.repositories.RoleRepository;
import com.vermeg.bookland.repositories.UserRepository;
import com.vermeg.bookland.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    List<BookDto> books = new ArrayList<>();

    @PostMapping("/save")
    public String save(@Valid UserDto userDto, BindingResult result, Model model, RedirectAttributes redirectAttrs) throws MessagingException {

        if (result.hasErrors()) {
            return "home/signUp";
        }else if(!userDto.getPassword().equals(userDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat", "error.user", "Password don't match");
            return "home/signUp";
        }else if(userRepository.existsByEmail(userDto.getEmail())){
            result.rejectValue("email", "error.user", "An account already exists for this email");
            return "home/signUp";
        }

        int verificationCode = (int)Math.floor(Math.random()*(99999-10000+1)+10000);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRole("ROLE_USER"));
        User user = new User(userDto.getName(),userDto.getSurname(),userDto.getEmail(),
                bCryptPasswordEncoder.encode(userDto.getPassword()), userDto.getBirthday(),
                userDto.getPhoneNumber(),verificationCode,roles);

        user.setVerification(verificationCode);
        Map<String,Object> templateModel = new HashMap<>();
        templateModel.put("code",verificationCode);
        templateModel.put("name",user.getName()+" "+user.getSurname().toUpperCase());

        emailService.sendEmailUsingThymeleaf(user.getEmail(),templateModel,"Confirm your email address");
        userRepository.save(user);

        redirectAttrs.addFlashAttribute("email", user.getEmail());
        return "redirect:../verification";
    }
}
