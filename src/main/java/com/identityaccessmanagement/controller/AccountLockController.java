package com.identityaccessmanagement.controller;

import com.identityaccessmanagement.service.AccountLockService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountLockController {

	@Autowired
    private  AccountLockService accountLockService;

    @PutMapping("/lock/{userId}")
    public ResponseEntity<String> lockUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                accountLockService.lockUser(userId));
    }

    @PutMapping("/unlock/{userId}")
    public ResponseEntity<String> unlockUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                accountLockService.unlockUser(userId));
    }
}
