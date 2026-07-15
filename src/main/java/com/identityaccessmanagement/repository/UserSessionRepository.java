package com.identityaccessmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identityaccessmanagement.entity.User;
import com.identityaccessmanagement.entity.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
	
	 List<UserSession> findByUser(User user);

	    Optional<UserSession> findByToken(String token);

}
