package com.example.SpringSecurity.registration.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenService implements IVerificationTokenService{

	private final VerificationTokenRepository tokenRepository;

	@Override
	public void deleteUserToken(Long id) {
		tokenRepository.deleteByUserId(id);
	}
}
