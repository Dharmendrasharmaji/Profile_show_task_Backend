package com.learn.userRecomService.service;

import com.learn.userRecomService.model.TMDBData;
import com.learn.userRecomService.model.TMDBMovies;
import com.learn.userRecomService.model.UserRecom;

import java.util.List;


public interface UserService {

    List<UserRecom> getAllUsers();

    UserRecom saveUser(UserRecom userRecom);

    List<UserRecom> getAuthUser(String email);

    List<UserRecom> getAllComments(String movieId);

    void setComment(String email, UserRecom userRecom);

    void setLike(String email, UserRecom userRecom);

    TMDBData getRandomMovies(String email,TMDBData tmdb);

    void setFav(String email, UserRecom userRecom);

    void deleteFav(String email,String movieId);

    List<TMDBMovies> getTrending(String userEmail);

    List<TMDBMovies> getSearchedMovie(TMDBData searchedMovie, String email,String title);
}
