package com.identityaccessmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identityaccessmanagement.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{
	

    Optional<Session> findByAccessToken(String accessToken);

}
