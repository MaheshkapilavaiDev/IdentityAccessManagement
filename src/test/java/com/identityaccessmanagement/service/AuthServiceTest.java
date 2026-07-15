package com.identityaccessmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.identityaccessmanagement.dto.ApiResponse;
import com.identityaccessmanagement.dto.AuthResponse;
import com.identityaccessmanagement.dto.LoginRequest;
import com.identityaccessmanagement.dto.RegisterRequest;
import com.identityaccessmanagement.entity.Otp;
import com.identityaccessmanagement.entity.Role;
import com.identityaccessmanagement.entity.User;
import com.identityaccessmanagement.exception.ResourceNotFoundException;
import com.identityaccessmanagement.exception.UserAlreadyExistsException;
import com.identityaccessmanagement.repository.OtpRepository;
import com.identityaccessmanagement.repository.RefreshTokenRepository;
import com.identityaccessmanagement.repository.RoleRepository;
import com.identityaccessmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private OtpRepository otpRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private RedisService redisService;

	@Mock
	private EmailService emailService;

	@Mock
	private AuditLogService auditLogService;

	@InjectMocks
	private AuthService authService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void shouldRegisterUserSuccessfully() {

		RegisterRequest request = new RegisterRequest();
		request.setFirstName("Mahesh");
		request.setLastName("Kumar");
		request.setEmail("mahesh@gmail.com");
		request.setPhone("9876543210");
		request.setPassword("Password@123");

		Role role = new Role();
		role.setId(1L);
		role.setName("USER");

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(userRepository.existsByPhone(request.getPhone())).thenReturn(false);
		when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		ApiResponse response = authService.register(request);

		assertNotNull(response);
		assertEquals("User registered successfully. Please verify your email.", response.getMessage());

		verify(userRepository, times(1)).save(any(User.class));
		verify(auditLogService, times(1)).logRegistration(any(User.class));
	}

	@Test
	void shouldSendOtpSuccessfully() {

		User user = new User();
		user.setEmail("mahesh@gmail.com");

		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		String response = authService.sendOtp(user.getEmail());

		assertEquals("OTP sent successfully", response);

		verify(otpRepository).save(any(Otp.class));
		verify(redisService).saveOtp(anyString(), anyString());
	}

	@Test
	void shouldVerifyEmailSuccessfully() {

		User user = new User();
		user.setEmail("mahesh@gmail.com");

		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		String response = authService.verifyEmail(user.getEmail());

		assertEquals("Email verified successfully", response);

		verify(userRepository).save(user);
	}

}
