package com.restApi.social_media_app.JpaRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.restApi.social_media_app.entities.Posts;

public interface PostRepo extends JpaRepository<Posts, Integer> {
	
	@Query("select p from Posts p where p.users.userId=:userId")
	public  List<Posts> findPostsByUsersUserId(Integer userId);
	
	@Query("select p from Posts p where LOWER(p.caption) LIKE LOWER(CONCAT('%', :query, '%'))")
	public List<Posts> searchPosts(String query);
}
