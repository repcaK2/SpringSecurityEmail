package com.example.SpringSecurity.user;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);
    void saveUserVerificationToken(User theUser, String verificationToken);
    String validateToken(String theToken);
    void delete(Long id);
}