package com.restApi.social_media_app.serviceDao;

import java.util.List;

import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Reels;
import com.restApi.social_media_app.entities.Users;


public interface UserDao {
	
	public List<Users> getAllUsers();
	
	public Users registerUser(Users users);
	
	public Users updateUsers(Users users , Integer userId);
	
	public void deleteUser(Integer userId);
	
	public Users findById(Integer userId);
	
	public List<Users> searchUser(String query);
	
	
	
	public Users followUsers(Integer userId1, Integer userId2);
	
	public List<Posts> getAllSavedPostsByUser(Integer userId);
	
	public List<Posts> getAllPostsCreatedByUser(Integer userId);
	
	public List<Reels> getAllReelCreatedByUser(Integer userId);
	
	public List<Reels> getAllSavedReelsByUser(Integer userId);

}
