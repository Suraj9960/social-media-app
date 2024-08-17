package com.restApi.social_media_app.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Comments;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.exceptionHandler.ApiResponse;
import com.restApi.social_media_app.security.JwtUtil;
import com.restApi.social_media_app.serviceDaoImpl.CommentDaoImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments/")
public class CommentController {
	
	@Autowired
	private CommentDaoImpl commentDaoImpl;
	
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
	public ResponseEntity<Object> getAllComents(){
		
		List<Comments> comments = commentDaoImpl.getAllComments();
		
		return new ResponseEntity<Object>(comments , HttpStatus.OK);
	}
	
	@GetMapping("getById/{commentId}")
	public ResponseEntity<Comments> findByComments(@PathVariable Integer commentId){
		
		Comments comments = commentDaoImpl.findByCommentId(commentId);
		
		return new ResponseEntity<Comments>(comments , HttpStatus.OK);
	}
	
	@PostMapping("post/{postId}")
	public ResponseEntity<Comments> createComments(@Valid @RequestBody Comments comments ,@PathVariable Integer postId ,@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		Comments comments2 = commentDaoImpl.createComment(comments, user.getUserId(), postId);
		
		return new ResponseEntity<Comments>(comments2 , HttpStatus.OK);
	}
	
	
	@PutMapping("update/{commentId}")
	public ResponseEntity<Comments> updateComments(@Valid @RequestBody Comments comments ,@PathVariable Integer commentId ,@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		Comments comments2 = commentDaoImpl.updateComments(comments, commentId, user.getUserId());
		
		return new ResponseEntity<Comments>(comments2 , HttpStatus.OK);
		
	}
	
	@DeleteMapping("deleteComment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComments(@PathVariable Integer commentId,@RequestHeader("Authorization") String token ){
		
		Users user = getUserFromJwt(token);
		
		commentDaoImpl.deleteComment(commentId, user.getUserId());
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment Deleted Successfully !!",true),HttpStatus.OK);
	}
	
	@GetMapping("getByUser")
	public ResponseEntity<List<Comments>> getCommentByUser(@RequestHeader("Authorization") String token){
		
		Users user = getUserFromJwt(token);
		
		List<Comments> comments = commentDaoImpl.getByUserId(user.getUserId());
		
		return new ResponseEntity<List<Comments>>(comments , HttpStatus.OK);
		
	}
	

}
