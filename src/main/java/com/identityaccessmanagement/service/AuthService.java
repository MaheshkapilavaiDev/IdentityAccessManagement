package com.identityaccessmanagement.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.identityaccessmanagement.dto.ApiResponse;
import com.identityaccessmanagement.dto.AuthResponse;
import com.identityaccessmanagement.dto.LoginRequest;
import com.identityaccessmanagement.dto.RefreshTokenRequest;
import com.identityaccessmanagement.dto.RefreshTokenResponse;
import com.identityaccessmanagement.dto.RegisterRequest;
import com.identityaccessmanagement.dto.ResetPasswordRequest;
import com.identityaccessmanagement.dto.VerifyOtpRequest;
import com.identityaccessmanagement.entity.Otp;
import com.identityaccessmanagement.entity.PasswordResetToken;
import com.identityaccessmanagement.entity.RefreshToken;
import com.identityaccessmanagement.entity.Role;
import com.identityaccessmanagement.entity.User;
import com.identityaccessmanagement.exception.ResourceNotFoundException;
import com.identityaccessmanagement.repository.OtpRepository;
import com.identityaccessmanagement.repository.PasswordResetTokenRepository;
import com.identityaccessmanagement.repository.RefreshTokenRepository;
import com.identityaccessmanagement.repository.RoleRepository;
import com.identityaccessmanagement.repository.UserRepository;
import com.identityaccessmanagement.security.CustomUserDetails;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class AuthService {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
		
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
    private  AuthenticationManager authenticationManager;
	
	@Autowired
	private  EmailService emailService;
	
	@Autowired
	private OtpRepository otpRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private AccountLockService accountLockService;
	
	@Autowired
	private AuditLogService auditLogService;
	
	@Autowired
	private RedisService redisService;
	 
	public ApiResponse register(RegisterRequest request) {

	    if (userRepository.existsByEmail(request.getEmail())) {
	        throw new RuntimeException("Email already exists");
	    }

	    if (userRepository.existsByPhone(request.getPhone())) {
	        throw new RuntimeException("Phone number already exists");
	    }

	    Role role = roleRepository.findByName("USER")
	            .orElseThrow(() -> new RuntimeException("Default role not found"));

	    User user = new User();

	    user.setUuid(UUID.randomUUID().toString());
	    user.setFirstName(request.getFirstName());
	    user.setLastName(request.getLastName());
	    user.setEmail(request.getEmail());
	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    user.setPhone(request.getPhone());

	    user.setEnabled(true);
	    user.setLocked(false);
	    user.setFailedAttempts(0);
	    user.setEmailVerified(true);
	    user.setMfaEnabled(false);

	    user.getRoles().add(role);

	    userRepository.save(user);
	    
	    auditLogService.logRegistration(user);

	    UserDetails userDetails = new CustomUserDetails(user);

	    return new ApiResponse("User registered successfully. Please verify your email.");
	}
	
	public AuthResponse login(LoginRequest request) {

	   /* authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    request.getEmail(),
	                    request.getPassword()
	            )
	    );*/

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    
	    if (Boolean.TRUE.equals(user.getLocked())) {
	        throw new RuntimeException("Your account is locked. Please contact the administrator.");
	    }
	    
	    try {

	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        request.getEmail(),
	                        request.getPassword()
	                )
	        );

	        // Reset failed attempts after successful login
	        accountLockService.resetFailedAttempts(request.getEmail());

	    } catch (Exception ex) {

	        // Increase failed attempts
	        accountLockService.increaseFailedAttempts(request.getEmail());

	        throw new RuntimeException("Invalid email or password");
	    }

	    UserDetails userDetails = new CustomUserDetails(user);

	    String accessToken = jwtService.generateToken(userDetails);
	    
	    String refreshToken = UUID.randomUUID().toString();

	    RefreshToken token = new RefreshToken();

	    token.setToken(refreshToken);
	    token.setUser(user);
	    token.setExpiryDate(LocalDateTime.now().plusDays(7));

	    refreshTokenRepository.save(token);

	    
	    sessionService.createSession(user, accessToken);
	    
	    auditLogService.logLogin(user);

	    return new AuthResponse(
	            accessToken,
	            null,
	            "Bearer",
	            900000L
	    );
	}
	
	public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

	    RefreshToken refreshToken = refreshTokenRepository
	            .findByToken(request.getRefreshToken())
	            .orElseThrow(() ->
	                    new RuntimeException("Invalid Refresh Token"));

	    if (refreshToken.getRevoked()) {
	        throw new RuntimeException("Refresh Token has been revoked");
	    }

	    if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("Refresh Token has expired");
	    }

	    User user = refreshToken.getUser();

	    UserDetails userDetails = new CustomUserDetails(user);

	    String newAccessToken = jwtService.generateToken(userDetails);

	    return new RefreshTokenResponse(
	            newAccessToken,
	            refreshToken.getToken()
	    );
	}
	
	public String logout(String refreshTokenValue) {

	    RefreshToken refreshToken = refreshTokenRepository
	            .findByToken(refreshTokenValue)
	            .orElseThrow(() ->
	                    new RuntimeException("Refresh Token not found"));

	    refreshToken.setRevoked(true);

	    refreshTokenRepository.save(refreshToken);
	    

	    return "Logout Successful";
	}
	
	@Transactional
	public String sendOtp(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    
	    otpRepository.deleteByUserAndVerifiedFalse(user);

	    String otpCode = String.valueOf(
	            100000 + new Random().nextInt(900000));

	    Otp otp = new Otp();

	    otp.setUser(user);
	    otp.setCode(otpCode);
	    otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
	    otp.setVerified(false);

	    otpRepository.save(otp);

	    redisService.saveOtp(email, otpCode);
	    
	    emailService.sendOtpEmail(user.getEmail(), otpCode);

	    return "OTP sent successfully";
	}
	
	public String verifyOtp(VerifyOtpRequest request) {

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    
	 // Check Redis first
	    String cachedOtp = redisService.getOtp(request.getEmail());

	    if (cachedOtp != null) {

	        if (!cachedOtp.equals(request.getOtp())) {
	            throw new RuntimeException("Invalid OTP");
	        }

	        redisService.deleteOtp(request.getEmail());

	    } else {

	    // Check Database
	    Otp otp = otpRepository.findByCode(request.getOtp())
	            .orElseThrow(() -> new RuntimeException("Invalid OTP"));

	    if (!otp.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("OTP does not belong to this user");
	    }

	    if (otp.getVerified()) {
	        throw new RuntimeException("OTP already verified");
	    }

	    if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("OTP has expired");
	    }

	    otp.setVerified(true);

	    otpRepository.save(otp);
	    }
	    
	    auditLogService.logOtpVerification(user);

	    return "OTP verified successfully";
	}
	
	public String verifyEmail(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    user.setEmailVerified(true);

	    userRepository.save(user);

	    return "Email verified successfully";
	}
	
	public String forgotPassword(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    String token = UUID.randomUUID().toString();

	    PasswordResetToken resetToken = new PasswordResetToken();

	    resetToken.setToken(token);
	    resetToken.setUser(user);
	    resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
	    resetToken.setUsed(false);

	    passwordResetTokenRepository.save(resetToken);
	    
	    auditLogService.logEmailVerification(user);

	    // Send reset link through email
	    emailService.sendPasswordResetEmail(user.getEmail(), token);

	    return "Password reset link sent successfully";
	}
	
	public String resetPassword(ResetPasswordRequest request) {

	    PasswordResetToken resetToken = passwordResetTokenRepository
	            .findByToken(request.getToken())
	            .orElseThrow(() ->
	                    new RuntimeException("Invalid reset token"));

	    if (resetToken.getUsed()) {
	        throw new RuntimeException("Reset token already used");
	    }

	    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("Reset token has expired");
	    }

	    User user = resetToken.getUser();

	    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

	    userRepository.save(user);
	    
	    auditLogService.logPasswordReset(user);

	    resetToken.setUsed(true);

	    passwordResetTokenRepository.save(resetToken);

	    return "Password reset successfully";
	}

}
