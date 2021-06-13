package com.learn.useraccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.useraccount.exceptions.UserAlreadyExistsException;
import com.learn.useraccount.model.User;
import com.learn.useraccount.service.ApiFeignClient;
import com.learn.useraccount.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import org.junit.jupiter.api.Assertions.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers=UserController.class)
public class UserAccountControllerTest {
    public static final String USERS_REGISTER_URL = "/api/v1";

    private User userOne;
    private List<User> userList;

    @MockBean
    private UserServiceImpl service;

    @MockBean
    private ApiFeignClient apiFeignClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        userOne = new User("1", "naira", "25","9988776655","naira@email.com", "testpass");

    }


    @Test
    public void givenNewUserDetailsWhenUserDoesNotExistThenShouldReturnCreatedStatus() throws Exception {
        when(service.userRegister(any(User.class))).thenReturn(userOne);

        mockMvc.perform(post(USERS_REGISTER_URL+"/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userOne))
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userOne)))
                       .andExpect(jsonPath("$.email").value("naira@email.com"))
                      .andDo(MockMvcResultHandlers.print());

        verify(service).userRegister(any(User.class));
    }


    @Test
    public void givenNewUserDetailsWhenUserDoesExistsThenShouldReturnConflictStatus() throws Exception {
        when(service.userRegister(any(User.class))).thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post(USERS_REGISTER_URL+"/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userOne))
        ).andExpect(status().isConflict());
        verify(service).userRegister(any(User.class));
    }
    @Test
    public void givenGetAllUsersThenShouldReturnOkStatus()throws Exception{

        when(apiFeignClient.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(get(USERS_REGISTER_URL+"/unauth")
                            .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
        verify(apiFeignClient).getAllUsers();
    }


}
