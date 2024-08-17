package com.restApi.social_media_app.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Reels;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.exceptionHandler.ApiResponse;
import com.restApi.social_media_app.security.JwtUtil;
import com.restApi.social_media_app.serviceDaoImpl.UserDaoImpl;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users/")
public class UserController {
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@Autowired
	private UserRepo repo;
	
	
	public Users getUserFromJwt(String token) {
		
		 String email =  jwtUtil.getEmailFromToken(token);
		 
		Users users =  repo.findByEmail(email);
		return users;
	}
	
	
	@GetMapping
	public List<Users> getAllUsers(){
		
	    List<Users> users = userDaoImpl.getAllUsers();
		
		return users;
	}
	
	@GetMapping("getUser")
	public ResponseEntity<Users> getById(@RequestHeader("Authorization") String token){
		
		Users user =  getUserFromJwt(token);
		
		Users users = userDaoImpl.findById(user.getUserId());
		return new ResponseEntity<Users>(users,HttpStatus.OK);
	}
	
	@DeleteMapping("logout")
	public ResponseEntity<ApiResponse> deleteById(@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		userDaoImpl.deleteUser(user.getUserId());
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully!!",true),HttpStatus.OK);
	}
	
	@PutMapping("update")
	public ResponseEntity<Users> updateUsers(@Valid @RequestBody Users users,@RequestHeader("Authorization") String token){
		Users user = getUserFromJwt(token);
		Users us = userDaoImpl.updateUsers(users, user.getUserId());
		return new ResponseEntity<Users>(us , HttpStatus.OK);
		
	}
	
	@PutMapping("followTo/{userId2}")
	public ResponseEntity<Users> followUsers(@RequestHeader("Authorization") String token, @PathVariable Integer userId2){
		
		Users user = getUserFromJwt(token);
		
		Users users = userDaoImpl.followUsers(user.getUserId(), userId2);
		return new ResponseEntity<Users>(users, HttpStatus.OK);
		
	}
	
	@GetMapping("searchUser")
	public List<Users> serchUsers(@RequestParam("query") String query){
		List<Users> users = repo.searchUsers(query);
		
	return users;
	}
	
	@GetMapping("getSavedPosts")
	public ResponseEntity<Object> getAllSavedPosts(@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		List<Posts> posts = userDaoImpl.getAllSavedPostsByUser(user.getUserId());
		
		return new ResponseEntity<Object>(posts,HttpStatus.OK);
	}
	
	
	
	
	@GetMapping("userProfile")
	public Users getUserFromToken( @RequestHeader("Authorization") String token) {
		String email =  jwtUtil.getEmailFromToken(token);
		
		 Users users =  repo.findByEmail(email);
		return  users;
	}
	
	
	@GetMapping("getAllPosts")
	public ResponseEntity<Object> getAllPosts(@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		List<Posts> posts = userDaoImpl.getAllPostsCreatedByUser(user.getUserId());
		
		return new ResponseEntity<Object>(posts,HttpStatus.OK);
	}
	
	
	
	@GetMapping("getAllReels")
	public ResponseEntity<Object> getAllReels(@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		List<Reels> reels = userDaoImpl.getAllReelCreatedByUser(user.getUserId());
		
		return new ResponseEntity<Object>(reels,HttpStatus.OK);
	}
	
	@GetMapping("getSavedReels")
	public ResponseEntity<Object> getAllSavedReels(@RequestHeader("Authorization") String token){
		
		Users users = getUserFromJwt(token);
		
		List<Reels> reels =  userDaoImpl.getAllSavedReelsByUser(users.getUserId());
		
		return new ResponseEntity<Object>(reels , HttpStatus.OK);
		
	}
	
	
}

