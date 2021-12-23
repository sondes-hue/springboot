package com.vermeg.bookland.servicesImpl;

import com.vermeg.bookland.entities.User;
import com.vermeg.bookland.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectUrl = "";
        User user = userRepository.findUserByEmail(request.getParameter("email"));
        if(user == null || !bCryptPasswordEncoder.matches(request.getParameter("password"),user.getPassword()))
            redirectUrl = "/home?error=true";
        else if(user.getVerification() != 0)
            redirectUrl = "/verification";

        request.getSession().setAttribute("email", request.getParameter("email"));
        response.sendRedirect(redirectUrl);

    }
}
