package com.learn.userRecomService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TMDBMovies {
    private String id;
    private String title;
    private String popularity;
    private String overview;
    private String poster_path;
    private String isLiked;
    private String likeCount;
    private String isFav;
    private String isRecommended;

}
