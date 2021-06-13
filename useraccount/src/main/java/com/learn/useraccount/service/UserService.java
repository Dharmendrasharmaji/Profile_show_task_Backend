package com.learn.useraccount.service;

import com.learn.useraccount.model.User;
import com.learn.useraccount.model.UserCredentials;

import java.util.Map;

public interface UserService {


    User userRegister(User user);

    Map<String,String> authenticate(UserCredentials userCredentials);
}
