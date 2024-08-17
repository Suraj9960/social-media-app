package com.restApi.social_media_app.JpaRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restApi.social_media_app.entities.Comments;
import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Users;

public interface CommentRepo extends JpaRepository<Comments, Integer> {
	
	public List<Comments> findByUsers(Users users);
	
	public List<Comments> findByPosts(Posts posts);

}
