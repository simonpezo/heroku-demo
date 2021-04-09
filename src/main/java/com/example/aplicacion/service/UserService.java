package com.example.aplicacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

import javax.validation.Valid;

import com.example.aplicacion.Exception.UsernameOrIdNotFound;
import com.example.aplicacion.dto.ChangePasswordForm;
import com.example.aplicacion.entity.User;
import com.example.aplicacion.repository.UserRepository;

public interface UserService {

	
		public Iterable<User> getAllUsers();

		public User createUser( User user) throws Exception;
		
		public User getUserById(Long id) throws Exception;
			
		public User updateUser(User user) throws Exception;
		
		public void deleteUser(Long id) throws UsernameOrIdNotFound;
		
		public User changePassword(ChangePasswordForm form) throws Exception;
	}
