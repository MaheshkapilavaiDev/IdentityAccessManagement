package com.identityaccessmanagement.service;

import com.identityaccessmanagement.dto.UserSessionResponse;
import com.identityaccessmanagement.entity.User;
import com.identityaccessmanagement.entity.UserSession;
import com.identityaccessmanagement.exception.ResourceNotFoundException;
import com.identityaccessmanagement.repository.UserRepository;
import com.identityaccessmanagement.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

	@Autowired
    private  UserSessionRepository sessionRepository;
	
	@Autowired
    private  UserRepository userRepository;

    public UserSession createSession(User user, String token) {

        UserSession session = new UserSession();

        session.setUser(user);
        session.setToken(token);
        session.setLoginTime(LocalDateTime.now());
        session.setActive(true);

        // Optional
        session.setDeviceName("Unknown Device");
        session.setIpAddress("127.0.0.1");

        return sessionRepository.save(session);
    }

    public List<UserSessionResponse> getUserSessions(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return sessionRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Logout Current Session
    public String logoutSession(String token) {

        UserSession session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setActive(false);
        session.setLogoutTime(LocalDateTime.now());

        sessionRepository.save(session);

        return "Logged out successfully";
    }

    public String logoutAllSessions(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserSession> sessions = sessionRepository.findByUser(user);

        for (UserSession session : sessions) {

            session.setActive(false);
            session.setLogoutTime(LocalDateTime.now());

            sessionRepository.save(session);
        }

        return "All sessions logged out successfully";
    }

    public void revokeSession(String token) {

        UserSession session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setActive(false);
        session.setLogoutTime(LocalDateTime.now());

        sessionRepository.save(session);
    }

    // Entity -> DTO
    private UserSessionResponse mapToResponse(UserSession session) {

        UserSessionResponse response = new UserSessionResponse();

        response.setId(session.getId());
        response.setDeviceName(session.getDeviceName());
        response.setIpAddress(session.getIpAddress());
        response.setLoginTime(session.getLoginTime());
        response.setLogoutTime(session.getLogoutTime());
        response.setActive(session.getActive());

        return response;
    }

}
