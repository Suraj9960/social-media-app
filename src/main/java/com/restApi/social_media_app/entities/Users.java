package com.restApi.social_media_app.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @NotBlank(message = "Please, enter valid Firstname")
    private String firstName;
    @NotBlank(message = "Please, enter valid Lastname")
    private String lastName;
    @Email
    @NotBlank(message = "Please, enter valid Email")
    private String email;
    @NotBlank(message = "Please, enter valid Password")
    private String password;
    private String gender;
    private String role;


    private List<Integer> following = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Posts> savedPosts = new ArrayList<>();
    
    @OneToMany(mappedBy = "users")
    @JsonIgnore
    List<Posts> posts = new ArrayList<>();
      
    private List<Integer> followers = new ArrayList<>();
    
    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Reels> createdReels = new ArrayList<>();
    
    
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
	private List<Reels> savedReelsList = new ArrayList<>();
}
