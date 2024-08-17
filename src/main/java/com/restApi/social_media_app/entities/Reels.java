package com.restApi.social_media_app.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Reels {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reelId;
	@NotBlank(message = "Enter the title !! ")
	private String title;
	@NotBlank(message = "Please, upload video !! ")
	private String videoUrl;
	
	@ManyToOne
	private Users users;
	
	@ManyToMany
	private List<Users> likedReelsList = new ArrayList<>();

}
