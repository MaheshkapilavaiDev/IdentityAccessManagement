package com.identityaccessmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class PermissionRequest {
	
	@NotBlank(message = "Permission name is required")
    private String name;

    private String description;

}
