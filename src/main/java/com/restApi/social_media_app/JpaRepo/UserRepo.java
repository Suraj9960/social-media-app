package com.restApi.social_media_app.JpaRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restApi.social_media_app.entities.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {

	@Query("select u from Users u where LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.gender) LIKE LOWER(CONCAT('%', :query, '%'))")
	public List<Users> searchUsers(@Param("query") String query);
	
	public Users findByEmail(String email);

}
