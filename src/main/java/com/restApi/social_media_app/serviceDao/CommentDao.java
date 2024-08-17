package com.restApi.social_media_app.serviceDao;

import java.util.List;

import com.restApi.social_media_app.entities.Comments;

public interface CommentDao {
	
	public Comments createComment(Comments comments , Integer userId , Integer postId);
	
	public List<Comments> getAllComments();
	
	public Comments findByCommentId(Integer commentId);
	
	public Comments updateComments(Comments comments , Integer commentId , Integer userId);
	
	public void deleteComment(Integer commentId , Integer userId);
	
	public List<Comments> getByUserId(Integer userId);
	
	public List<Comments >getByPostId(Integer postId);

}
