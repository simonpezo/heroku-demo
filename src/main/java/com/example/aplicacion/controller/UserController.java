package com.example.aplicacion.controller;



import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.aplicacion.Exception.CustomeFieldValidationException;
import com.example.aplicacion.Exception.UsernameOrIdNotFound;
import com.example.aplicacion.dto.ChangePasswordForm;
import com.example.aplicacion.entity.Role;
import com.example.aplicacion.entity.User;
import com.example.aplicacion.repository.RoleRepository;
import com.example.aplicacion.service.UserService;


@Controller
public class UserController {

	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping({"/","/login"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signup (Model model) {
		
		Role userRole= roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("userForm",new User());
		model.addAttribute("roles", roles);
		model.addAttribute("signup",true);
		
		return "user-form/user-signup";
		
	}
	
	@PostMapping("/signup")
	public String signupAction(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {

		Role userRole = roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("userForm", user);
		model.addAttribute("roles",roles);
		model.addAttribute("signup",true);

		if(result.hasErrors()) {
			return "user-form/user-signup";
		}else {
			try {
				userService.createUser(user);
			} catch (CustomeFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				return "user-form/user-signup";
			}catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
				return "user-form/user-signup";
			}
		}
		return index();
	}
	
	
	@GetMapping("/userForm")
	public String UserForm(Model model) {
		model.addAttribute("userForm", new User());
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("listTab","active");
		return "user-form/user-view";
	}
	
	@PostMapping("/userForm")
	public String createUser(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab","active");
		} else {
			try {
				userService.createUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab","active");
				
			} catch (CustomeFieldValidationException cfve) {
				result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
				
				model.addAttribute("userForm", user);
				model.addAttribute("formTab","active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
			}
			
			catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab","active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
			}
		}
		
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
		
	}
	
	@GetMapping("/editUser/{id}")
	public String getEditUserForm(Model model, @PathVariable(name ="id")Long id)throws Exception{
		User userToEdit = userService.getUserById(id);

		model.addAttribute("userForm", userToEdit);
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles",roleRepository.findAll());
		model.addAttribute("formTab","active");
		model.addAttribute("editMode","true");
		model.addAttribute("passwordForm",new ChangePasswordForm(id));

		return "user-form/user-view";
	}

	@PostMapping("/editUser")
	public String postEditUserForm(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab","active");
			model.addAttribute("editMode","true");
			model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
		}else {
			try {
				userService.updateUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab","active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage",e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab","active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles",roleRepository.findAll());
				model.addAttribute("editMode","true");
				model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles",roleRepository.findAll());
		return "user-form/user-view";

	}

	@GetMapping("/userForm/cancel")
	public String cancelEditUser(ModelMap model) {
		return "redirect:/userForm";
	}
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable(name="id") Long id) {
		try {
			userService.deleteUser(id);
		} catch (UsernameOrIdNotFound u) {
			model.addAttribute("listErrorMessage", u.getMessage());
		}
		return UserForm(model);
	}
	
	@PostMapping("/editUser/changePassword")
	public ResponseEntity postEditUserChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			if(errors.hasErrors()) {
				String result = errors.getAllErrors()
						.stream().map(x ->x.getDefaultMessage())
						.collect(Collectors.joining(""));
				throw new Exception(result);
			}
			userService.changePassword(form);
		}catch (Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}
	
	
}