package com.example.SpringSecurity.registration;


public record RegistrationRequest(
		String firstName,
		String lastName,
		String email,
		String password,
		String role) {
}
