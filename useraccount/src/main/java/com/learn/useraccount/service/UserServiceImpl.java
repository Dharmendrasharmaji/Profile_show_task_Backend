package com.learn.useraccount.service;


import com.learn.useraccount.exceptions.UserAlreadyExistsException;
import com.learn.useraccount.exceptions.UserNotFoundException;
import com.learn.useraccount.model.User;
import com.learn.useraccount.model.UserCredentials;
import com.learn.useraccount.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Service
public class UserServiceImpl implements UserService{

    private UserRepo userRepo;
    private JWTGeneratorService jwtGeneratorService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, JWTGeneratorService jwtGeneratorService) {
        this.userRepo = userRepo;
        this.jwtGeneratorService = jwtGeneratorService;
    }



    @Override
    public User userRegister(User user) {
        if(userRepo.findByEmail(user.getEmail().toLowerCase()).isPresent()){
            throw new UserAlreadyExistsException("Email already exists");
        }
        String uuid = UUID.randomUUID().toString();
        user.setId(uuid);
        User savedUser = userRepo.save(user);
        return savedUser;
    }

    @Override
    public Map<String, String> authenticate(UserCredentials userCredentials) {
        userCredentials.setEmail(userCredentials.getEmail().toLowerCase());
        Optional<User> userCheck = userRepo.findByEmail(userCredentials.getEmail());
        if (userCheck.isEmpty()){
            throw new UserNotFoundException("Email not found.");
        }
        if (!userCheck.get().getPassword().equals(userCredentials.getPassword())){
            throw new UserNotFoundException("Password mismatch.");
        }

        String token = jwtGeneratorService.generateToken(userCredentials.getEmail());

        return Map.of("token",token);
    }

    @Override
    public User getUserData(String email) {
        if (email==null){
            throw new UserNotFoundException("Email is not provided");
        }
        else if(userRepo.findByEmail(email.toLowerCase()).isEmpty()){
            throw new UserNotFoundException("Email doesn't exist");
        }



        return userRepo.findByEmail(email.toLowerCase()).get();
    }

    @Override
    public byte[] getUserImage(String email) {
        if (email==null){
            throw new UserNotFoundException("Email is not provided");
        }
        else if(userRepo.findByEmail(email.toLowerCase()).isEmpty()){
            throw new UserNotFoundException("Email doesn't exist");
        }
        User user = userRepo.findByEmail(email.toLowerCase()).get();
        byte[] image = user.getImage();
        return image;
    }

    @Override
    public User updateProfile(User user) {
        User savedUser=null;
        User userDB = userRepo.findByEmail(user.getEmail().toLowerCase()).get();
        if(userRepo.findByEmail(user.getEmail().toLowerCase()).isPresent()) {
            user.setId(userDB.getId());
            if (user.getImage()==null){
                user.setImage(userDB.getImage());
            }
            savedUser = userRepo.save(user);
        }
        return savedUser;
    }
}
