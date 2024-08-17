package com.restApi.social_media_app.serviceDao;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.restApi.social_media_app.entities.Reels;
import com.restApi.social_media_app.entities.Users;

public interface ReelsDao {
	
	public Reels createReels(Reels reels , Users users);
	
	public void deleteReel(Integer reelId , Users users);
	
	public Reels savedReels(Integer reelId , Users users);
	
	public Reels likedReels(Integer reelId , Users users);

	public String uploadVideo(MultipartFile file) throws IOException;

}
