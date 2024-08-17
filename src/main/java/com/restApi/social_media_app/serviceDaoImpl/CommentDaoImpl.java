package com.restApi.social_media_app.serviceDaoImpl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restApi.social_media_app.JpaRepo.CommentRepo;
import com.restApi.social_media_app.JpaRepo.PostRepo;
import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Comments;
import com.restApi.social_media_app.entities.Posts;
import com.restApi.social_media_app.entities.Users;
import com.restApi.social_media_app.serviceDao.CommentDao;

@Service
public class CommentDaoImpl implements CommentDao {

	@Autowired
	private CommentRepo repo;

	@Autowired
	private PostRepo repo1;

	@Autowired
	private UserRepo repo2;

	@Override
	public Comments createComment(Comments comments, Integer userId, Integer postId) {

		Users users = repo2.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id: " + userId));
		Posts posts = repo1.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Post Not Found With Id: " + postId));

		Comments saveComments = new Comments();
		saveComments.setMessage(comments.getMessage());
		saveComments.setPosts(posts);
		saveComments.setUsers(users);

		return repo.save(saveComments);
	}

	@Override
	public List<Comments> getAllComments() {
		return repo.findAll();
	}

	@Override
	public Comments findByCommentId(Integer commentId) {
		Comments comments = repo.findById(commentId)
				.orElseThrow(() -> new NoSuchElementException("Comment Not Found With Id: " + commentId));

		return comments;
	}

	@Override
	public Comments updateComments(Comments comments, Integer commentId, Integer userId) {
		Comments comments2 = findByCommentId(commentId);

		Users users = repo2.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id: " + userId));
		if (!comments2.getUsers().equals(users)) {
			throw new NoSuchElementException(
					"User with Id - " + userId + " not allowed to Comment on  this post , sorry!!");
		} else {
			comments2.setMessage(comments.getMessage());
		}
		return repo.save(comments2);
	}

	@Override
	public void deleteComment(Integer commentId, Integer userId) {
		Comments comments2 = findByCommentId(commentId);

		if (!comments2.getUsers().getUserId().equals(userId)) {
			throw new NoSuchElementException("User with id" + userId + " not allowed to delete the comment!!");
		}

		repo.delete(comments2);
	}

	@Override
	public List<Comments> getByUserId(Integer userId) {
		Users users = repo2.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User Not Found With Id: " + userId));

		List<Comments> comments = repo.findByUsers(users);

		return comments;
	}

	@Override
	public List<Comments> getByPostId(Integer postId) {
		Posts posts = repo1.findById(postId)
				.orElseThrow(() -> new NoSuchElementException("Post Not Found With Id: " + postId));

		List<Comments> comments = repo.findByPosts(posts);

		return comments;
	}

}
