package com.example.aplicacion.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.aplicacion.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public Optional<User> findByUsername(String username);
}
