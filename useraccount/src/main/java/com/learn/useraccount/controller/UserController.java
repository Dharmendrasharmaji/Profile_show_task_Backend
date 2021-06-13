package com.learn.useraccount.controller;

import com.learn.useraccount.model.TMDBMovies;
import com.learn.useraccount.model.User;
import com.learn.useraccount.model.UserCredentials;
import com.learn.useraccount.model.UserRecom;
import com.learn.useraccount.service.ApiFeignClient;
import com.learn.useraccount.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApiFeignClient apiFeignClient;


    @GetMapping("/info")
    public ResponseEntity<User> info(){
        return new ResponseEntity<>(new User("id","dhar","23","332","ewe","232"),HttpStatus.OK);
    }

    @GetMapping("/unauth")
    public ResponseEntity<List<UserRecom>> getAllUsers(){
        
        return new ResponseEntity<>(apiFeignClient.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<List<UserRecom>> getAllUsers(@PathVariable String movieId){
        return new ResponseEntity<>(apiFeignClient.getAllComments(movieId),HttpStatus.OK);
    }

    @GetMapping("movies/trending/{userEmail}")
    public ResponseEntity<List<TMDBMovies>> getTrending(@PathVariable String userEmail){
        return new ResponseEntity<>(apiFeignClient.getTrending(userEmail),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody User user){
        return new ResponseEntity<>(userService.userRegister(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserCredentials userCredentials){
        Map<String, String> token = userService.authenticate(userCredentials);
        return new ResponseEntity<>(token,HttpStatus.ACCEPTED);
    }
}
