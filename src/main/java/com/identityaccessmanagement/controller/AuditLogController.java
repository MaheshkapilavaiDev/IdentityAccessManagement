package com.identityaccessmanagement.controller;

import com.identityaccessmanagement.dto.AuditLogResponse;
import com.identityaccessmanagement.service.AuditLogService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

	@Autowired
    private  AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {

        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AuditLogResponse>> getUserAuditLogs(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                auditLogService.getUserAuditLogs(userId));
    }

}