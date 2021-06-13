package com.learn.userRecomService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UserRecom {

    @Id
    private String id;
    private String title;
    private String rating;
    private String description;
    private String comment;
    private String movieId;
    private String posterUrl;
    private String isRecommended;
    private String  isLiked;
    private String isFavourite;
    private String userEmail;
}
