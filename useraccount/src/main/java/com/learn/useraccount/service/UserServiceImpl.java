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
            throw new UserAlreadyExistsException("User with given email is already present.");
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
            throw new UserNotFoundException("User with given email doesn't exist.");
        }
        if (!userCheck.get().getPassword().equals(userCredentials.getPassword())){
            throw new UserNotFoundException("Passoword mismatch");
        }

        String token = jwtGeneratorService.generateToken(userCredentials.getEmail());

        return Map.of("token",token);
    }
}
