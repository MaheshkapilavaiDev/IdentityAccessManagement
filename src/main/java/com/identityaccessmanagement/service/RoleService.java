package com.identityaccessmanagement.service;

import com.identityaccessmanagement.dto.RoleRequest;
import com.identityaccessmanagement.dto.RoleResponse;
import com.identityaccessmanagement.entity.Role;
import com.identityaccessmanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public RoleResponse createRole(RoleRequest request) {

		if (roleRepository.existsByName(request.getName())) {
			throw new RuntimeException("Role already exists");
		}

		Role role = new Role();
		role.setName(request.getName());
		role.setDescription(request.getDescription());

		Role savedRole = roleRepository.save(role);

		return mapToResponse(savedRole);
	}

	public RoleResponse updateRole(Long id, RoleRequest request) {

		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		role.setName(request.getName());
		role.setDescription(request.getDescription());

		Role updatedRole = roleRepository.save(role);

		return mapToResponse(updatedRole);
	}

	public String deleteRole(Long id) {

		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		roleRepository.delete(role);

		return "Role deleted successfully";
	}

	public List<RoleResponse> getAllRoles() {

		return roleRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	public RoleResponse getRoleById(Long id) {

		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		return mapToResponse(role);
	}

	// Convert Entity to DTO
	private RoleResponse mapToResponse(Role role) {

		RoleResponse response = new RoleResponse();
		response.setId(role.getId());
		response.setName(role.getName());
		response.setDescription(role.getDescription());

		return response;
	}
}
