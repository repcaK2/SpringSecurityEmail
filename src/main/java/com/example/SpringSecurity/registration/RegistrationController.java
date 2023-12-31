package com.example.SpringSecurity.registration;

import com.example.SpringSecurity.event.RegistrationCompleteEvent;
import com.example.SpringSecurity.registration.token.VerificationToken;
import com.example.SpringSecurity.registration.token.VerificationTokenRepository;
import com.example.SpringSecurity.user.RegistrationRequest;
import com.example.SpringSecurity.user.User;
import com.example.SpringSecurity.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @PostMapping("/NewUser")
    public String registerUser(@RequestBody
                               @RequestParam("firstName") String firstName,
                               @RequestParam("lastName") String lastName,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               final HttpServletRequest request){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName(firstName);
        registrationRequest.setLastName(lastName);
        registrationRequest.setEmail(email);
        registrationRequest.setPassword(password);
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!  Please, check your email to complete your registration";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if(theToken.getUser().isEnabled()){
            return "user is verified, please login";
        }
        String verificationResult = userService.validateToken(token);
        if(verificationResult.equalsIgnoreCase("valid")){
            String loginLink = "<a href='/login'>Login to your account</a>";
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }

    public String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
