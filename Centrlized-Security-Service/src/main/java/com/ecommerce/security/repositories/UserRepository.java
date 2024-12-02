package com.ecommerce.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.security.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Find user by email (assuming this is a unique identifier)
	Optional<User> findByEmail(String email);

	// Find user by phone (assuming this is a unique identifier as well)
	Optional<User> findByPhone(String phone);

	// Custom method to find a user by email or phone
	Optional<User> findByEmailOrPhone(String email, String phone);

}
