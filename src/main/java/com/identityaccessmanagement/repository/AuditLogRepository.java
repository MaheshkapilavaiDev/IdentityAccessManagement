package com.identityaccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identityaccessmanagement.entity.AuditLog;
import com.identityaccessmanagement.entity.User;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{
	
    List<AuditLog> findByUser(User user);

}
