package com.codewithvaishnavi.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithvaishnavi.store.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String user);
}
