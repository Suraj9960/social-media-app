package com.restApi.social_media_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.security.JwtUtil;
import com.restApi.social_media_app.security.Response;
import com.restApi.social_media_app.security.UserService;
import com.restApi.social_media_app.serviceDaoImpl.UserDaoImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/public")
public class AuthController {

	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo repo;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/register")
	public ResponseEntity<Users> registerUser(@Valid @RequestBody Users users) {

		Users registeredUser = userDaoImpl.registerUser(users);

		return new ResponseEntity<Users>(registeredUser, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody Users users) throws Exception{
		
		this.authenticate(users.getEmail() , users.getPassword());
		
		String username = users.getEmail();
		
		UserDetails userDetails =  userService.loadUserByUsername(username);
		
		String token =  jwtUtil.generateToken(userDetails.getUsername());
		
		Response response = new Response();
		response.setToken(token);
		
		String email = userDetails.getUsername();
		
		Users users2 =  repo.findByEmail(email);
		
		response.setUsers(users2);
		
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
	}
	
	
	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {

			authenticationManager.authenticate(authenticationToken);

		} catch (BadCredentialsException e) {

			throw new BadCredentialsException("Bad Credential !! ");
		}

	}
}
