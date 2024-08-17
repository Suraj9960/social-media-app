package com.restApi.social_media_app.controllers;


import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restApi.social_media_app.JpaRepo.PostRepo;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.exceptionHandler.ApiResponse;
import com.restApi.social_media_app.security.JwtUtil;
import com.restApi.social_media_app.serviceDaoImpl.PostDaoImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/posts/")
public class PostController {
	
	@Autowired
	private PostDaoImpl postDaoImpl;
	
	@Autowired
	private PostRepo postRepo;
	
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepo repo;
	
	
	public Users getUserFromJwt(String token) {
		
		 String email =  jwtUtil.getEmailFromToken(token);
		 
		Users users =  repo.findByEmail(email);
		return users;
	}
	
	
	@GetMapping
	public ResponseEntity<List<Posts>> getAllPosts(){
		List<Posts> posts = postDaoImpl.getAllPosts();
		return new ResponseEntity<List<Posts>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("{postId}/getPost")
	public ResponseEntity<Posts> getPostById(@PathVariable Integer postId){
		Posts podPosts = postDaoImpl.findByPostId(postId);
		return new ResponseEntity<Posts>(podPosts,HttpStatus.OK);
	}
	
	@PostMapping("createPost")
	public ResponseEntity<Posts> createPosts(@Valid @RequestBody Posts posts ,@RequestHeader("Authorization") String token){
		
		Users users = getUserFromJwt(token);
		
		Posts posts2 = postDaoImpl.createPosts(posts, users.getUserId());
		
		return new  ResponseEntity<Posts>(posts2 ,HttpStatus.OK);
		
	}
	
	@PutMapping("update/{postId}")
	public ResponseEntity<Posts> updatePosts(@Valid @RequestBody Posts posts , @PathVariable Integer postId){
		
		Posts posts2 = postDaoImpl.updatePosts(posts, postId);
		
		return new ResponseEntity<Posts>(posts2 , HttpStatus.OK);
	}
	
	@DeleteMapping("/deletePost/{postId}")
	public ResponseEntity<ApiResponse> deletePosts(@PathVariable Integer postId ,@RequestHeader("Authorization") String token ){
		
		Users users = getUserFromJwt(token);
		
		postDaoImpl.deletePosts(postId, users.getUserId());
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Successfully !! ",true),HttpStatus.OK);
		
	}
	
	@GetMapping("getByUser")
	public ResponseEntity<List<Posts>> findByUsers(@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		List<Posts> posts = postDaoImpl.findByUser(user.getUserId());
		
		return new ResponseEntity<List<Posts>>(posts ,HttpStatus.OK);
	}
	
	
	@PutMapping("savePost/{postId}")
	public ResponseEntity<Posts> savedPosts(@PathVariable Integer postId ,@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		Posts posts = postDaoImpl.savedPost(postId, user.getUserId());
		
		return new ResponseEntity<Posts>(posts ,HttpStatus.OK);
	}
	
	@PutMapping("likePost/{postId}")
	public ResponseEntity<Posts> likedPosts(@PathVariable Integer postId ,@RequestHeader("Authorization") String token){
		
		Users users = getUserFromJwt(token);
		
		Posts posts = postDaoImpl.likedPosts(postId, users.getUserId());
		
		return new ResponseEntity<Posts>(posts ,HttpStatus.OK);
	}
	
	@GetMapping("serachPost/{query}")
	public ResponseEntity<List<Posts>> searchPosts(@PathVariable String query){
		List<Posts> posts = postDaoImpl.searchPosts(query);
		
		return new ResponseEntity<List<Posts>>(posts ,HttpStatus.OK);
	}
	
	@GetMapping("getLikedUsers/{postId}")
	public ResponseEntity<Object> getAllSavedPosts(@PathVariable Integer postId){
		
		List<Users> posts = postDaoImpl.getAllLikedUsers(postId);
		
		return new ResponseEntity<Object>(posts,HttpStatus.OK);
	}
	
	@PostMapping("upload/post/{postId}")
	public ResponseEntity<ApiResponse> uploadImage(@RequestParam("image")MultipartFile image ,@PathVariable Integer postId ,
			@RequestHeader("Authorization") String token) throws IOException {
		
		Users users = getUserFromJwt(token);
		
		Posts posts =  postDaoImpl.findByPostId(postId);
		
		if( !posts.getUsers().equals(users)) {
			return new ResponseEntity<>(new ApiResponse("Unauthorized to upload image for this post", false), HttpStatus.FORBIDDEN);
		}
		
		  String path = postDaoImpl.uploadImage(image);
	        posts.setImage(path);

	        // Save the post with the new image path
	        postRepo.save(posts);
		
		return new ResponseEntity<>(new ApiResponse("Image Uplloaded Successfully !! ",true), HttpStatus.OK);
	}
	
	
}
