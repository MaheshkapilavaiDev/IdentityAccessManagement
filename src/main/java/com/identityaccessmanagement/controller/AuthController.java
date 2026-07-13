package com.identityaccessmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.identityaccessmanagement.dto.AuthResponse;
import com.identityaccessmanagement.dto.LoginRequest;
import com.identityaccessmanagement.dto.RefreshTokenRequest;
import com.identityaccessmanagement.dto.RefreshTokenResponse;
import com.identityaccessmanagement.dto.RegisterRequest;
import com.identityaccessmanagement.dto.ResetPasswordRequest;
import com.identityaccessmanagement.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

		return ResponseEntity.ok(authService.register(request));

	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

		return ResponseEntity.ok(authService.login(request));

	}

	@PostMapping("/refresh")
	public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {

		return ResponseEntity.ok(authService.refreshToken(request));

	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestParam String refreshToken) {

		return ResponseEntity.ok(authService.logout(refreshToken));

	}

	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOtp(@RequestParam String email) {
		return ResponseEntity.ok(authService.sendOtp(email));

	}

	@GetMapping("/verify-email")
	public ResponseEntity<String> verifyEmail(@RequestParam String email) {

		return ResponseEntity.ok(authService.verifyEmail(email));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {

		return ResponseEntity.ok(authService.forgotPassword(email));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

		return ResponseEntity.ok(authService.resetPassword(request));
	}
}
