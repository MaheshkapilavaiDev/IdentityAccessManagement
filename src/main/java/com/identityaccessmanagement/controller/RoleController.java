package com.identityaccessmanagement.controller;

import com.identityaccessmanagement.dto.RoleRequest;
import com.identityaccessmanagement.dto.RoleResponse;
import com.identityaccessmanagement.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping
	public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {

		RoleResponse response = roleService.createRole(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {

		RoleResponse response = roleService.updateRole(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable Long id) {

		String message = roleService.deleteRole(id);
		return ResponseEntity.ok(message);
	}

	@GetMapping
	public ResponseEntity<List<RoleResponse>> getAllRoles() {

		List<RoleResponse> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {

		RoleResponse role = roleService.getRoleById(id);
		return ResponseEntity.ok(role);
	}

}
