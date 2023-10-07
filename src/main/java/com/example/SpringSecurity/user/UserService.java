package com.example.SpringSecurity.user;

import com.example.SpringSecurity.exception.UserAlreadyExistsException;
import com.example.SpringSecurity.registration.token.VerificationToken;
import com.example.SpringSecurity.registration.token.VerificationTokenRepository;
import com.example.SpringSecurity.registration.token.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final VerificationTokenService verificationTokenService;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()){
            throw new UserAlreadyExistsException("User with email: " + request.getEmail() + " already exist");
        }
        var theUser = new User();
        theUser.setFirstName(request.getFirstName());
        theUser.setLastName(request.getLastName());
        theUser.setEmail(request.getEmail());
        theUser.setPassword(passwordEncoder.encode(request.getPassword()));
        theUser.setRole(request.getRole());
        return userRepository.save(theUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String Token) {
        var verificationToken = new VerificationToken(Token, theUser);
        tokenRepository.save(verificationToken);
    }


    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid token verification";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if(token.getTokenExpirationTime().getTime() - calendar.getTime().getTime() <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<User> theUser = userRepository.findById(id);
        if (theUser.isPresent()){
            User user = theUser.get();
            verificationTokenService.deleteUserToken(user.getId());
            userRepository.deleteById(id);
        }
    }
}
