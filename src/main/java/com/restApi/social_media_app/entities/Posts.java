package com.restApi.social_media_app.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    @NotBlank(message = "Please, enter caption !! ")
    private String caption;
    @NotBlank(message = "Please, upload image !! ")
    private String image;
    @NotBlank(message = "Please, enter some content !! ")
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private Users users;

    @ManyToMany
    private List<Users> likedByUsers = new ArrayList<>();
}
