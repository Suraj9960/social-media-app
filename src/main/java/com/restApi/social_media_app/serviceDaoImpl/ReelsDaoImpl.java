package com.restApi.social_media_app.serviceDaoImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.restApi.social_media_app.JpaRepo.ReelsRepo;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Reels;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.serviceDao.ReelsDao;

@Service
public class ReelsDaoImpl implements ReelsDao {

	@Autowired
	private ReelsRepo reelsRepo;

	@Autowired
	private UserRepo repo;

	@Override
	public Reels createReels(Reels reels, Users users) {

		Reels reels2 = new Reels();

		reels2.setTitle(reels.getTitle());
		reels2.setVideoUrl(reels.getVideoUrl());
		reels2.setUsers(users);

		reelsRepo.save(reels2);

		return reels2;
	}

	@Override
	public void deleteReel(Integer reelId, Users users) {
		Reels reels = reelsRepo.findById(reelId)
				.orElseThrow(() -> new NoSuchElementException("Reel Not found with id: " + reelId));

		if (!reels.getUsers().equals(users)) {
			throw new NoSuchElementException("Sorry ! You Cannot Delete This Reel ..... !");
		}

		reelsRepo.delete(reels);
	}

	@Override
	public Reels savedReels(Integer reelId, Users users) {
		Reels reels = reelsRepo.findById(reelId)
				.orElseThrow(() -> new NoSuchElementException("Reel Not found with id: " + reelId));

		if (users.getSavedReelsList() == null) {
			users.setSavedReelsList(new ArrayList<>());
		}

		if (users.getSavedReelsList().contains(reels)) {
			users.getSavedReelsList().remove(reels);
		}else {
			users.getSavedReelsList().add(reels);
		}

		repo.save(users);
		Reels reels2 = reelsRepo.save(reels);

		return reels2;
	}

	@Override
	public Reels likedReels(Integer reelId, Users users) {
		Reels reels = reelsRepo.findById(reelId)
				.orElseThrow(() -> new NoSuchElementException("Reel Not found with id: " + reelId));

		if (reels.getLikedReelsList() == null) {
			reels.setLikedReelsList(new ArrayList<>());
		}

		if (reels.getLikedReelsList().contains(users)) {
			reels.getLikedReelsList().remove(users);
		}else {
			reels.getLikedReelsList().add(users);
		}

		Reels reels2 = reelsRepo.save(reels);

		return reels2;
	}
	
	@Override
	public String uploadVideo(MultipartFile file) throws IOException {

		String uPLOAD_DTRString = "D:\\My Spring Boot Projects\\social_media_app\\src\\main\\resources\\static\\video";

		InputStream iStream = file.getInputStream();
		byte data[] = new byte[iStream.available()];
		iStream.read(data);

		FileOutputStream fileOutputStream = new FileOutputStream(
				uPLOAD_DTRString + File.separator + file.getOriginalFilename());
		fileOutputStream.write(data);

		fileOutputStream.flush();
		fileOutputStream.close();

		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/video/").path(file.getOriginalFilename())
				.toUriString();

	}

}
