package com.example.aplicacion.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.aplicacion.entity.Role;

@Repository
public interface RoleRepository  extends CrudRepository<Role, Long>{

	 public Role findByName (String name);
	
}
