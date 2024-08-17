package com.restApi.social_media_app.controllers;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restApi.social_media_app.JpaRepo.ReelsRepo;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Reels;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.exceptionHandler.ApiResponse;
import com.restApi.social_media_app.security.JwtUtil;
import com.restApi.social_media_app.serviceDaoImpl.ReelsDaoImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reels/")
public class ReelsController {
	
	@Autowired
	private ReelsRepo reelsRepo;
	
	@Autowired
	private ReelsDaoImpl reelsDaoImpl;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepo repo;
	
	
	public Users getUserFromJwt(String token) {
		
		 String email =  jwtUtil.getEmailFromToken(token);
		 
		Users users =  repo.findByEmail(email);
		return users;
	}
	

	
	@PostMapping("createReel")
	public ResponseEntity<Reels> createReels(@Valid @RequestBody Reels reels , @RequestHeader("Authorization") String token){
		
		Users users = getUserFromJwt(token);
		
		Reels newReels  = reelsDaoImpl.createReels(reels, users);
		
		return new ResponseEntity<>(newReels,HttpStatus.OK);
		
	}
	
	@DeleteMapping("deleteReel/{reelId}")
	public ResponseEntity<ApiResponse>deleteReel(@PathVariable Integer reelId ,@RequestHeader("Authorization") String token ){
		
		Users users = getUserFromJwt(token);
		
		reelsDaoImpl.deleteReel(reelId, users);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Reel is Deleted Successfully.. ! ",true) ,HttpStatus.OK);
	}
	
	@PutMapping("saveReel/{reelId}")
	public ResponseEntity<Reels> saveReels(@PathVariable Integer reelId ,@RequestHeader("Authorization") String token){
		Users users =  getUserFromJwt(token);
		
		Reels reels =  reelsDaoImpl.savedReels(reelId, users);
		
		return new ResponseEntity<Reels>( reels, HttpStatus.OK);
	}
	
	
	@PutMapping("likeReel/{reelId}")
	public ResponseEntity<Reels> likeReels(@PathVariable Integer reelId ,@RequestHeader("Authorization") String token){
		Users users =  getUserFromJwt(token);
		
		Reels reels =  reelsDaoImpl.likedReels(reelId, users);
		
		return new ResponseEntity<Reels>( reels, HttpStatus.OK);
	}
	
	
	@PostMapping("upload/reel/{reelId}")
	public ResponseEntity<ApiResponse> uploadVideo(@RequestParam("video")MultipartFile video ,@PathVariable Integer reelId ,
			@RequestHeader("Authorization") String token) throws IOException {
		
		Users users = getUserFromJwt(token);
		
		Reels reels = reelsRepo.findById(reelId).orElseThrow(() -> new NoSuchElementException("Reel is not found with Id: "+reelId));
		
		if( !reels.getUsers().equals(users)) {
			return new ResponseEntity<>(new ApiResponse("Unauthorized to upload video for this reel", false), HttpStatus.FORBIDDEN);
		}
		
		  String path = reelsDaoImpl.uploadVideo(video);
	        reels.setVideoUrl(path);

	        // Save the post with the new image path
	        reelsRepo.save(reels);
		
		return new ResponseEntity<>(new ApiResponse("Video Uploaded Successfully !! ",true), HttpStatus.OK);
	}

}
