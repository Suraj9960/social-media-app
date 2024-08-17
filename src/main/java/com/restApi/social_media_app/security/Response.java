package com.restApi.social_media_app.security;



import com.restApi.social_media_app.entities.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
	
	private String token;
	
	private Users users;

}
