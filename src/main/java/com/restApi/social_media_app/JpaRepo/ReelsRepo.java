package com.restApi.social_media_app.JpaRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.restApi.social_media_app.entities.Reels;

public interface ReelsRepo extends JpaRepository<Reels, Integer> {
	
	@Query("select r from Reels r where r.users.userId=:userId")
	public  List<Reels> findReelsByUsersUserId(Integer userId);

}
