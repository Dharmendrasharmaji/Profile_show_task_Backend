package com.learn.useraccount.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecom {

    private String id;
    private String title;
    private String rating;
    private String description;
    private String comment;
    private String movieId;
    private String isLiked;
    private String isFavourite;
    private String isRecommended;
    private String posterUrl;
    private String userEmail;
}
