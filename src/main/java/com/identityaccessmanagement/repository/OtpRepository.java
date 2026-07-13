package com.identityaccessmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.identityaccessmanagement.entity.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long>{
	
	 Optional<Otp> findByCode(String code);
	

}
