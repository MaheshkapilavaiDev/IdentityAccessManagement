package com.identityaccessmanagement.service;


import com.identityaccessmanagement.entity.User;
import com.identityaccessmanagement.exception.ResourceNotFoundException;
import com.identityaccessmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountLockService {
	
	@Autowired
	private AuditLogService auditLogService;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Autowired
    private  UserRepository userRepository;

    // Increase Failed Attempts
    public void increaseFailedAttempts(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        int attempts = user.getFailedAttempts() + 1;

        user.setFailedAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setLocked(true);
        }

        userRepository.save(user);
    }

    // Reset Failed Attempts
    public void resetFailedAttempts(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFailedAttempts(0);

        userRepository.save(user);
    }

    public String lockUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setLocked(true);

        userRepository.save(user);
        
        auditLogService.logAccountLocked(user);

        return "User account locked successfully";
    }

    public String unlockUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setLocked(false);
        user.setFailedAttempts(0);

        userRepository.save(user);
        
        auditLogService.logAccountUnlocked(user);

        return "User account unlocked successfully";
    }
}
