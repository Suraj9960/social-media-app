package com.restApi.social_media_app.serviceDaoImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.restApi.social_media_app.JpaRepo.PostRepo;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.serviceDao.PostDao;

@Service
public class PostDaoImpl implements PostDao {

	@Autowired
	private PostRepo repo;

	@Autowired
	private UserRepo repo1;

	@Override
	public List<Posts> getAllPosts() {
		List<Posts> posts = repo.findAll();
//		List<PostDto> psDtos = posts.stream().map( post -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		if (posts.isEmpty()) {
			throw new NoSuchElementException("List Is Empty For Now .....");
		}

		return posts;
	}

	@Override
	public Posts createPosts(Posts posts, Integer userId) {

		Users users = repo1.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not found with id: " + userId));

		Posts newPosts = new Posts();

		newPosts.setCaption(posts.getCaption());
		newPosts.setUsers(users);
		newPosts.setContent(posts.getContent());
		newPosts.setCreatedAt(LocalDateTime.now());
		repo.save(newPosts);
		return newPosts;
	}

	@Override
	public Posts updatePosts(Posts posts, Integer postId) {
		Posts posts2 = repo.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("User Not found with id: " + postId));

		posts2.setCaption(posts.getCaption());
		posts2.setContent(posts.getContent());
		posts2.setCreatedAt(LocalDateTime.now());

		Posts updatePosts = repo.save(posts2);

		return updatePosts;
	}

	@Override
	public void deletePosts(Integer postId, Integer userId) {

		Posts posts = repo.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Posts Not found with id: " + postId));

		Users users = repo1.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not found with id: " + userId));

		if (!posts.getUsers().getUserId().equals(users.getUserId())) {
			throw new NoSuchElementException("User with id: " + userId + " is not authorized to delete this post.");
		}

		List<Users> user = repo1.findAll();
		for (Users u : user) {
			u.getSavedPosts().remove(posts);
			repo1.save(u);
		}

		repo.delete(posts);
	}

	@Override
	public Posts findByPostId(Integer postId) {
		Posts posts2 = repo.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Post Not found with id: " + postId));

		return posts2;
	}

	@Override
	public List<Posts> findByUser(Integer userId) {
		List<Posts> posts = repo.findPostsByUsersUserId(userId);
		if (posts.isEmpty()) {
			throw new NoSuchElementException("List Is Empty For Now .....");
		}
		return posts;

	}

	@Override
	public Posts savedPost(Integer postId, Integer userId) {
		Posts posts = repo.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Post Not found with id: " + postId));
		Users users = repo1.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not found with id: " + userId));

		if (users.getSavedPosts() == null) {
			users.setSavedPosts(new ArrayList<>());
		}

		if (users.getSavedPosts().contains(posts)) {
			users.getSavedPosts().remove(posts);
		}else {
			users.getSavedPosts().add(posts);
		}

		repo1.save(users);
		Posts posts2 = repo.save(posts);
		return posts2;
	}

	@Override
	public Posts likedPosts(Integer postId, Integer userId) {
		Posts posts = repo.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Post Not found with id: " + postId));
		Users users = repo1.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not found with id: " + userId));

		if (posts.getLikedByUsers() == null) {
			posts.setLikedByUsers(new ArrayList<>());
		}

		if (posts.getLikedByUsers().contains(users)) {
			posts.getLikedByUsers().remove(users); // If user already liked the post, unlike it
		} else {
			posts.getLikedByUsers().add(users); // Otherwise, like the post
		}

		Posts updatedPosts = repo.save(posts);
		return updatedPosts;
	}

	@Override
	public List<Posts> searchPosts(String query) {

		List<Posts> posts = repo.searchPosts(query);

		return posts;
	}

	@Override
	public List<Users> getAllLikedUsers(Integer postId) {
		Posts posts = repo.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Post Not found with id: " + postId));

		List<Users> users = posts.getLikedByUsers();

		return users;
	}

	@Override
	public String uploadImage(MultipartFile file) throws IOException {

		String uPLOAD_DTRString = "D:\\My Spring Boot Projects\\social_media_app\\src\\main\\resources\\static\\image";

		InputStream iStream = file.getInputStream();
		byte data[] = new byte[iStream.available()];
		iStream.read(data);

		FileOutputStream fileOutputStream = new FileOutputStream(
				uPLOAD_DTRString + File.separator + file.getOriginalFilename());
		fileOutputStream.write(data);

		fileOutputStream.flush();
		fileOutputStream.close();

		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(file.getOriginalFilename())
				.toUriString();

	}

}
