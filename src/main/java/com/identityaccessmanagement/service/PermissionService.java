package com.identityaccessmanagement.service;

import com.identityaccessmanagement.dto.PermissionRequest;
import com.identityaccessmanagement.dto.PermissionResponse;
import com.identityaccessmanagement.entity.Permission;
import com.identityaccessmanagement.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	public PermissionResponse createPermission(PermissionRequest request) {

		if (permissionRepository.existsByName(request.getName())) {
			throw new RuntimeException("Permission already exists");
		}

		Permission permission = new Permission();
		permission.setName(request.getName());
		permission.setDescription(request.getDescription());

		Permission savedPermission = permissionRepository.save(permission);

		return mapToResponse(savedPermission);
	}

	// Update Permission
	public PermissionResponse updatePermission(Long id, PermissionRequest request) {

		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		permission.setName(request.getName());
		permission.setDescription(request.getDescription());

		Permission updatedPermission = permissionRepository.save(permission);

		return mapToResponse(updatedPermission);
	}

	public String deletePermission(Long id) {

		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		permissionRepository.delete(permission);

		return "Permission deleted successfully";
	}

	public List<PermissionResponse> getAllPermissions() {

		return permissionRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	public PermissionResponse getPermissionById(Long id) {

		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		return mapToResponse(permission);
	}

	private PermissionResponse mapToResponse(Permission permission) {

		PermissionResponse response = new PermissionResponse();

		response.setId(permission.getId());
		response.setName(permission.getName());
		response.setDescription(permission.getDescription());

		return response;
	}
}
