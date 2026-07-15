package com.identityaccessmanagement.service;

import com.identityaccessmanagement.dto.PermissionResponse;
import com.identityaccessmanagement.dto.RoleRequest;
import com.identityaccessmanagement.dto.RoleResponse;
import com.identityaccessmanagement.entity.Permission;
import com.identityaccessmanagement.entity.Role;
import com.identityaccessmanagement.repository.PermissionRepository;
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
	
	@Autowired
	private  PermissionRepository permissionRepository;

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

	    List<PermissionResponse> permissions = role.getPermissions()
	            .stream()
	            .map(permission -> {

	                PermissionResponse permissionResponse = new PermissionResponse();

	                permissionResponse.setId(permission.getId());
	                permissionResponse.setName(permission.getName());
	                permissionResponse.setDescription(permission.getDescription());

	                return permissionResponse;

	            }).toList();

	    response.setPermissions(permissions);

	    return response;
	}
	
	public RoleResponse assignPermission(Long roleId, Long permissionId) {

	    Role role = roleRepository.findById(roleId)
	            .orElseThrow(() -> new RuntimeException("Role not found"));

	    Permission permission = permissionRepository.findById(permissionId)
	            .orElseThrow(() -> new RuntimeException("Permission not found"));

	    role.getPermissions().add(permission);

	    Role updatedRole = roleRepository.save(role);

	    return mapToResponse(updatedRole);
	}
	
	public List<PermissionResponse> getPermissionsByRole(Long roleId) {

	    Role role = roleRepository.findById(roleId)
	            .orElseThrow(() -> new RuntimeException("Role not found"));

	    return role.getPermissions()
	            .stream()
	            .map(permission -> {

	                PermissionResponse response = new PermissionResponse();

	                response.setId(permission.getId());
	                response.setName(permission.getName());
	                response.setDescription(permission.getDescription());

	                return response;

	            }).toList();
	}
	
	public RoleResponse removePermission(Long roleId, Long permissionId) {

	    Role role = roleRepository.findById(roleId)
	            .orElseThrow(() -> new RuntimeException("Role not found"));

	    Permission permission = permissionRepository.findById(permissionId)
	            .orElseThrow(() -> new RuntimeException("Permission not found"));

	    role.getPermissions().remove(permission);

	    Role updatedRole = roleRepository.save(role);

	    return mapToResponse(updatedRole);
	}
}
