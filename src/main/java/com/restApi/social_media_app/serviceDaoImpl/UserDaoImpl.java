package com.restApi.social_media_app.serviceDaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restApi.social_media_app.JpaRepo.PostRepo;
import com.restApi.social_media_app.JpaRepo.ReelsRepo;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Reels;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.serviceDao.UserDao;

@Service
public class UserDaoImpl implements UserDao {

	@Autowired
	private UserRepo repo;

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ReelsRepo reelsRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Users> getAllUsers() {
		List<Users> users = repo.findAll();
		if (users == null) {
			System.out.println("List is Empty!!");
		}

		return users;
	}

	@Override
	public Users registerUser(Users users) {
		Users newUsers = new Users();
		newUsers.setFirstName(users.getFirstName());
		newUsers.setLastName(users.getLastName());
		newUsers.setEmail(users.getEmail());
		newUsers.setPassword(passwordEncoder.encode(users.getPassword()));
		newUsers.setGender(users.getGender());
		newUsers.setRole("USER");

		repo.save(newUsers);

		return newUsers;
	}

	@Override
	public Users updateUsers(Users users, Integer userId) {
		Users users2 = repo.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found WIth Id: " + userId));
		if (users2 == null) {
			System.out.println("User is not found for Id: " + userId);
		}

		users2.setFirstName(users.getFirstName());
		users2.setEmail(users.getEmail());
		users2.setGender(users.getGender());
		users2.setPassword(passwordEncoder.encode(users.getPassword()));
		users2.setLastName(users.getLastName());

		Users updatedUsers = repo.save(users2);

		return updatedUsers;
	}

	@Override
	public void deleteUser(Integer userId) {
		Users users = repo.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found with Id: " + userId));

		repo.delete(users);
	}

	@Override
	public Users findById(Integer userId) {
		Users users = repo.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found WIth Id: " + userId));

		if (users == null) {
			System.out.println("User is not found for Id: " + userId);
		}

		return users;
	}

	@Override
	public List<Users> searchUser(String query) {
		List<Users> users = repo.searchUsers(query);
		return users;
	}

	public Users followUsers(Integer userId1, Integer userId2) {
		Users user1 = repo.findById(userId1)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id: " + userId1));
		Users user2 = repo.findById(userId2)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id: " + userId2));

		// Ensure that followers and following lists are initialized
		if (user1.getFollowing() == null) {
			user1.setFollowing(new ArrayList<>());
		}
		if (user2.getFollowers() == null) {
			user2.setFollowers(new ArrayList<>());
		}

		// Toggle user1 in user2's followers and user2 in user1's following
		if (user2.getFollowers().contains(user1.getUserId())) {
			user2.getFollowers().remove(user1.getUserId());
		} else {
			user2.getFollowers().add(user1.getUserId());
		}
		if (user1.getFollowing().contains(user2.getUserId())) {
			user1.getFollowing().remove(user2.getUserId());
		} else {
			user1.getFollowing().add(user2.getUserId());
		}

		// Save changes
		repo.save(user1);
		repo.save(user2);

		return user1;

	}

	public List<Posts> getAllSavedPostsByUser(Integer userId) {

		Users users = repo.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id - " + userId));

		List<Posts> savedPost = users.getSavedPosts();

		return savedPost;
	}

	@Override
	public List<Posts> getAllPostsCreatedByUser(Integer userId) {

		List<Posts> posts = postRepo.findPostsByUsersUserId(userId);

		return posts;
	}

	@Override
	public List<Reels> getAllReelCreatedByUser(Integer userId) {

		List<Reels> reels = reelsRepo.findReelsByUsersUserId(userId);

		return reels;
	}

	@Override
	public List<Reels> getAllSavedReelsByUser(Integer userId) {

		Users users = repo.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id - " + userId));

		List<Reels> savedReels = users.getSavedReelsList();

		return savedReels;
	}
}
