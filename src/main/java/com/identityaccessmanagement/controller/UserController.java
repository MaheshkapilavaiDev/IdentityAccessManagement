package com.identityaccessmanagement.controller;

import com.identityaccessmanagement.dto.UserResponse;
import com.identityaccessmanagement.dto.UserUpdateRequest;
import com.identityaccessmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	@Autowired
    private  UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.deleteUser(id));
    }

    // Assign Role
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }

    // Remove Role
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserResponse> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }

    // Get User Roles
    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<String>> getUserRoles(
            @PathVariable Long userId) {

        return ResponseEntity.ok(userService.getUserRoles(userId));
    }
}
