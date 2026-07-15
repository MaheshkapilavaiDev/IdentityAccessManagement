package com.identityaccessmanagement.controller;


import com.identityaccessmanagement.dto.UserSessionResponse;
import com.identityaccessmanagement.service.SessionService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

	@Autowired
    private  SessionService sessionService;

    // Get Active Sessions
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserSessionResponse>> getSessions(
            @PathVariable Long userId) {

        return ResponseEntity.ok(sessionService.getUserSessions(userId));
    }

    // Logout Current Session
    public String logout(String token) {

        sessionService.logoutSession(token);

        return "Logout successful";
    }

    // Logout All Sessions
    @PostMapping("/logout-all/{userId}")
    public ResponseEntity<String> logoutAll(
            @PathVariable Long userId) {

        return ResponseEntity.ok(sessionService.logoutAllSessions(userId));
    }

}
