package com.vermeg.bookland.controllers;

import com.vermeg.bookland.Dtos.BookDto;
import com.vermeg.bookland.Dtos.ClientDto;
import com.vermeg.bookland.entities.Client;
import com.vermeg.bookland.repositories.ClientRepository;
import com.vermeg.bookland.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmailService emailService;

    List<BookDto> books = new ArrayList<>();

    @GetMapping("/home")
    public String home(){
        return "client/clientHome";
    }

    @PostMapping("/save")
    public String save(@Valid ClientDto clientDto, BindingResult result, Model model) throws MessagingException {

        if (result.hasErrors()) {
            return "home/signUp";
        }else if(!clientDto.getPassword().equals(clientDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat", "error.client", "Password don't match");
            return "home/signUp";
        }else if(clientRepository.findClientByEmail(clientDto.getEmail()) != null){
            result.rejectValue("email", "error.client", "An account already exists for this email");
            return "home/signUp";
        }

        int verificationCode = (int)Math.floor(Math.random()*(99999-10000+1)+10000);
        Client client = new Client(clientDto.getName(),clientDto.getSurname(),clientDto.getEmail(),
                clientDto.getPassword(), clientDto.getBirthday(),clientDto.getPhoneNumber(),verificationCode);

        client.setVerification(verificationCode);
        Map<String,Object> templateModel = new HashMap<>();
        templateModel.put("code",verificationCode);
        templateModel.put("name",client.getName()+" "+client.getSurname().toUpperCase());

        emailService.sendEmailUsingThymeleaf(client.getEmail(),templateModel,"Confirm your email address");
        clientRepository.save(client);

        return "redirect:../home";
    }
}
