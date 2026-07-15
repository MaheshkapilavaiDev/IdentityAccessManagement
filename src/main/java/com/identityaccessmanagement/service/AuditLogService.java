package com.identityaccessmanagement.service;

import com.identityaccessmanagement.dto.AuditLogResponse;
import com.identityaccessmanagement.entity.AuditLog;
import com.identityaccessmanagement.entity.Permission;
import com.identityaccessmanagement.entity.Role;
import com.identityaccessmanagement.entity.User;
import com.identityaccessmanagement.exception.ResourceNotFoundException;
import com.identityaccessmanagement.repository.AuditLogRepository;
import com.identityaccessmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

	@Autowired
    private  AuditLogRepository auditLogRepository;
	
	@Autowired
    private  UserRepository userRepository;

    public void logRegistration(User user) {

        saveAudit(user, "USER_REGISTRATION",
                "User registered successfully");
    }

    public void logLogin(User user) {

        saveAudit(user, "USER_LOGIN",
                "User logged in successfully");
    }

    // User Logout
    public void logLogout(User user) {

        saveAudit(user, "USER_LOGOUT",
                "User logged out successfully");
    }

    public void logPasswordReset(User user) {

        saveAudit(user, "PASSWORD_RESET",
                "Password reset successfully");
    }

    public void logOtpVerification(User user) {

        saveAudit(user, "OTP_VERIFICATION",
                "OTP verified successfully");
    }

    public void logEmailVerification(User user) {

        saveAudit(user, "EMAIL_VERIFICATION",
                "Email verified successfully");
    }

    // Role Assigned
    public void logRoleAssigned(User user, Role role) {

        saveAudit(user,
                "ROLE_ASSIGNED",
                "Assigned Role : " + role.getName());
    }

    // Permission Assigned
    public void logPermissionAssigned(Role role, Permission permission) {

        AuditLog audit = new AuditLog();

        audit.setAction("PERMISSION_ASSIGNED");
        audit.setDescription("Permission " + permission.getName()
                + " assigned to Role " + role.getName());

        audit.setTimestamp(LocalDateTime.now());
        audit.setDeviceName("System");
        audit.setIpAddress("127.0.0.1");

        auditLogRepository.save(audit);
    }

    public void logAccountLocked(User user) {

        saveAudit(user,
                "ACCOUNT_LOCKED",
                "User account locked");
    }

    public void logAccountUnlocked(User user) {

        saveAudit(user,
                "ACCOUNT_UNLOCKED",
                "User account unlocked");
    }

    public List<AuditLogResponse> getUserAuditLogs(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return auditLogRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getAllAuditLogs() {

        return auditLogRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Common Save Method
    private void saveAudit(User user,
                           String action,
                           String description) {

        AuditLog audit = new AuditLog();

        audit.setUser(user);
        audit.setAction(action);
        audit.setDescription(description);
        audit.setTimestamp(LocalDateTime.now());

        audit.setDeviceName("Unknown Device");
        audit.setIpAddress("127.0.0.1");

        auditLogRepository.save(audit);
    }

    private AuditLogResponse mapToResponse(AuditLog audit) {

        AuditLogResponse response = new AuditLogResponse();

        response.setId(audit.getId());
        response.setAction(audit.getAction());
        response.setDescription(audit.getDescription());
        response.setTimestamp(audit.getTimestamp());
        response.setIpAddress(audit.getIpAddress());
        response.setDeviceName(audit.getDeviceName());

        return response;
    }

}
