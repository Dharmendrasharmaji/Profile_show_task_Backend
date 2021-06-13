package com.learn.userRecomService.repository;

import com.learn.userRecomService.model.UserRecom;
import org.apache.catalina.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserRecom,String> {

    Optional<List<UserRecom>> findByUserEmailAndIsFavourite(String email,String isFav);

    Optional<List<UserRecom>> findByMovieId(String movieId);

    Optional<List<UserRecom>> findByUserEmailAndMovieId(String userEmail,String movieId);

    Optional<List<UserRecom>> findByUserEmailAndIsLiked(String userEmail,String isLiked);

    Optional<List<UserRecom>> findByIsLikedAndMovieId(String isLiked,String movieId);
}
