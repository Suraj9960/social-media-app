package com.restApi.social_media_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.restApi.social_media_app.JpaRepo.UserRepo;
import com.restApi.social_media_app.entities.Users;

@Component
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user  = repo.findByEmail(username);
		
		if(user != null) {
			UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
			              .password(user.getPassword())
			              .roles(user.getRole())
			              .build();
			return userDetails;
		}
		
		throw new UsernameNotFoundException("User not found : "+username);
	}

}
