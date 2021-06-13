package com.learn.useraccount.service;

import com.learn.useraccount.model.TMDBMovies;
import com.learn.useraccount.model.User;
import com.learn.useraccount.model.UserRecom;
import org.apache.catalina.LifecycleState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-recommendation")
public interface ApiFeignClient {

    @GetMapping("api/v2/movies")
    public List<UserRecom> getAllUsers();

    @GetMapping("api/v2/movies/trending/{userEmail}")
    public List<TMDBMovies> getTrending(@PathVariable String userEmail);

    @GetMapping("api/v2/movies/{movieId}")
    public List<UserRecom> getAllComments(@PathVariable String movieId);
}
