package com.restApi.social_media_app.serviceDao;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Users;


public interface PostDao {
	
	public List<Posts> getAllPosts();
	
	public Posts createPosts(Posts posts , Integer userId );
	
	public Posts updatePosts(Posts posts , Integer postId);
	
	public void deletePosts(Integer postId , Integer userId);
	
	public Posts findByPostId(Integer postId);
	
	public List<Posts> findByUser(Integer userId);
	
	public Posts savedPost(Integer postId , Integer userId);
	
	public Posts likedPosts(Integer postId , Integer userId);
	
	public List<Posts> searchPosts(String query);
	
	public List<Users> getAllLikedUsers(Integer postId);
	
	public String uploadImage(MultipartFile file) throws IOException;

}
