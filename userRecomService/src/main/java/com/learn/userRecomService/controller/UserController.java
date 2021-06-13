package com.learn.userRecomService.controller;

import com.learn.userRecomService.model.TMDBData;
import com.learn.userRecomService.model.TMDBMovies;
import com.learn.userRecomService.model.UserRecom;
import com.learn.userRecomService.service.ApiFeingClient;
import com.learn.userRecomService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2")
public class UserController {

    @Autowired
    private ApiFeingClient apiFeingClient;
    
    @Autowired
    private UserService userService;

    @GetMapping("/movies")
    public ResponseEntity<?> userRecommend(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/movies/auth")
    public ResponseEntity<?> getAuthUser(@RequestHeader String email){
        return new ResponseEntity<>(userService.getAuthUser(email),HttpStatus.OK);
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<?> getComments(@PathVariable String movieId){
        return new ResponseEntity<>(userService.getAllComments(movieId),HttpStatus.OK);
    }

    @PostMapping("/movies/addFav")
    public ResponseEntity<?> setFav(@RequestHeader String email,@RequestBody UserRecom userRecom){
        userService.setFav(email,userRecom);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/movies/updateLike")
    public ResponseEntity<?> setLike(@RequestHeader String email,@RequestBody UserRecom userRecom){
        userService.setLike(email,userRecom);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/movies/getrandom")
    public ResponseEntity<?> getRandomMovies(@RequestHeader String email){
        TMDBData randomData = apiFeingClient.getRandomMovies();
        System.out.println("in controller");
        return new ResponseEntity<>(userService.getRandomMovies(email,randomData),HttpStatus.OK);
    }

    @PostMapping("/movies/comment")
    public ResponseEntity<?> getComments(@RequestHeader String email,@RequestBody UserRecom userRecom){
        userService.setComment(email,userRecom);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/movies")
    public ResponseEntity<?> saveUser(@RequestBody UserRecom userRecom){
        return new ResponseEntity<>(userService.saveUser(userRecom),HttpStatus.CREATED);
    }

    @DeleteMapping("/movies/delete/{movieId}")
    public ResponseEntity<?> deleteFav(@RequestHeader String email,@PathVariable String movieId){
        userService.deleteFav(email,movieId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/movies/trending/{userEmail}")
    public ResponseEntity<?> getTrending(@PathVariable String userEmail){
        return new ResponseEntity<>(userService.getTrending(userEmail),HttpStatus.OK);
    }
}
