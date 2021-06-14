package com.learn.userRecomService.service;

import com.learn.userRecomService.model.TMDBData;
import com.learn.userRecomService.model.TMDBMovies;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "themoviedb" ,url = "http://api.themoviedb.org/")
public interface ApiFeingClient {

    @GetMapping("/3/discover/movie?api_key=0c0b61ddfb12f5fa53e6d702c54139cc&language=hi-IN%C2%AEion=IN&sort_by=popularity.desc&page=1&primary_release_year=2018&with_original_language=hi")
    public TMDBData getRandomMovies();

    @GetMapping("/3/search/movie?api_key=f737cd2c2070147264ea7d0b50d3dbf1&query={title}")
    public TMDBData getSearchedMovie(@PathVariable String title);
}
