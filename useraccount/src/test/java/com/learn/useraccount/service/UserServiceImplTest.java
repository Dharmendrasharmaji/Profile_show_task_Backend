package com.learn.useraccount.service;

import com.learn.useraccount.exceptions.UserAlreadyExistsException;
import com.learn.useraccount.exceptions.UserNotFoundException;
import com.learn.useraccount.model.User;
import com.learn.useraccount.model.UserCredentials;
import com.learn.useraccount.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest
{
    private User userOne;
    private UserCredentials credentialsOne;

    @Mock
    private UserRepo repository;

    @Mock
    private JWTGeneratorService jwtGeneratorService;

    @InjectMocks
    private UserServiceImpl service;


    @BeforeEach
    public void setup(){
        userOne = new User("1", "naira","20" ,"2345678988","test@email.com", "testpass");
        credentialsOne = new UserCredentials("somemail@test.com", "passOne");
    }

    @Test
    public void givenUserDetailsWhenUserDoesNotExistThenReturnSaveUser() throws UserAlreadyExistsException {
        //Configured the behaviour of Mock object
        when(repository.findByEmail(userOne.getEmail())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(userOne);

        //Call to Service method, whcih will in turn invoke methods on Mock objects
        User user = service.userRegister(userOne);
        assertAll(
                ()->{assertNotNull(user);},
                ()->{assertTrue(user.getEmail().equals("test@email.com"));},
                ()->{assertTrue(user.getName().equals("naira"));}
        );

        //Verified when Mock calls were made by Service or not
        verify(repository,atLeastOnce()).findByEmail(anyString());
        verify(repository,times(1)).save(any(User.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void givenUserDetailsWhenUserExistThenThrowException(){
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(userOne));
        assertThrows(UserAlreadyExistsException.class, () -> service.userRegister(userOne));
        verify(repository).findByEmail("test@email.com");
    }


    @Test
    public void givenUserCredentialsWhenValidThenReturnTrue(){
        //test setup using mock
        when(jwtGeneratorService.generateToken(anyString())).thenReturn("token");
        when(repository.findByEmail(anyString()))
                .thenReturn(
                        Optional.of(new User("1","riya","25" ,"98765432", "riya@test.com", "passOne")));
        //actual test
        assertTrue(service.authenticate(credentialsOne).containsValue("token"));
        verify(repository).findByEmail(anyString());
    }

    @Test
    public void givenUserCredentialsWhenDoesNotExistThenThrowException(){
        //test setup using mock
        when(repository.findByEmail(anyString()))
                .thenReturn(
                        Optional.empty());
        //actual test

        assertThrows(UserNotFoundException.class, () -> service.authenticate(credentialsOne));
        verify(repository).findByEmail(anyString());
    }

}
