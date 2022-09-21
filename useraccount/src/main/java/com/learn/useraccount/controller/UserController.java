package com.learn.useraccount.controller;

import com.learn.useraccount.model.*;
import com.learn.useraccount.service.ApiFeignClient;
import com.learn.useraccount.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private User user;

    @Autowired
    private ApiFeignClient apiFeignClient;


//    @GetMapping("/info")
//    public ResponseEntity<User> info(){
//        return new ResponseEntity<>(new User("id","dhar","23","332","ewe","232"),HttpStatus.OK);
//    }

    @GetMapping("/unauth")
    public ResponseEntity<List<UserRecom>> getAllUsers(){
        
        return new ResponseEntity<>(apiFeignClient.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/userImage/{userEmail}")
    public ResponseEntity<?> getUserProfileImage(@PathVariable String userEmail){
        byte[] userImage = userService.getUserImage(userEmail);
        System.out.println("user image "+ userImage);
        return new ResponseEntity<>(userImage,HttpStatus.OK);
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<List<UserRecom>> getAllUsers(@PathVariable String movieId){
        return new ResponseEntity<>(apiFeignClient.getAllComments(movieId),HttpStatus.OK);
    }

    @GetMapping("movies/trending/{userEmail}")
    public ResponseEntity<List<TMDBMovies>> getTrending(@PathVariable String userEmail){
        return new ResponseEntity<>(apiFeignClient.getTrending(userEmail),HttpStatus.OK);
    }

    @GetMapping(value = "/user/{email}")
    public ResponseEntity<?> getUserDetails(@PathVariable String email) {
        User userData = userService.getUserData(email);
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + userData.getImage());
        return new ResponseEntity<>(userService.getUserData(email),header,HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> userRegister(@RequestParam MultipartFile image,@RequestParam String name,@RequestParam String age,@RequestParam String phone,@RequestParam String password,@RequestParam String email ,@RequestParam String address) throws IOException {

//        System.out.println(image.getBytes());
//        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
//        String path = "C:\\Users\\dharmendra.sharma\\Downloads\\Final Project\\MovieCruiser-backend\\MovieCruiser\\useraccount\\src\\main\\resources\\Image";
//        System.out.println(image+"----"+name+"---------"+age);
//        Files.copy(image.getInputStream(), Paths.get(path+ File.separator+ image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        InputStream in = image.getInputStream();
        byte[] byteImage = new byte[in.available()];
        in.read(byteImage);
        System.out.println("ByteImage "+ byteImage);

        user.setPhone(phone);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setImage(byteImage);
        user.setAddress(address);
        return new ResponseEntity<>(userService.userRegister(user), HttpStatus.CREATED);
    }

    @PostMapping(value = "/updateUserProfile")
    public ResponseEntity<?> updateUserProfile(@RequestParam(required = false) MultipartFile image,@RequestParam String name,@RequestParam String age,@RequestParam String password,@RequestParam String phone,@RequestParam String email ,@RequestParam String address) throws IOException {

//        System.out.println(image.getBytes());
//        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
//        String path = "C:\\Users\\dharmendra.sharma\\Downloads\\Final Project\\MovieCruiser-backend\\MovieCruiser\\useraccount\\src\\main\\resources\\Image";
//        System.out.println(image+"----"+name+"---------"+age);
//        Files.copy(image.getInputStream(), Paths.get(path+ File.separator+ image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        if (image!=null){
            InputStream in = image.getInputStream();
            byte[] byteImage = new byte[in.available()];
            in.read(byteImage);
            System.out.println("ByteImage "+ byteImage);
            user.setImage(byteImage);
        }else {
            user.setImage(null);
        }


        user.setPhone(phone);
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setPassword(password);
        user.setAddress(address);
        return new ResponseEntity<>(userService.updateProfile(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserCredentials userCredentials){
        Map<String, String> token = userService.authenticate(userCredentials);
        return new ResponseEntity<>(token,HttpStatus.ACCEPTED);
    }
}
