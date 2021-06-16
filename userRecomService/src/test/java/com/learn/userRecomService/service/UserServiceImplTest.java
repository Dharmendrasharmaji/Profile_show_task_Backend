package com.learn.userRecomService.service;

import com.learn.userRecomService.model.TMDBMovies;
import com.learn.userRecomService.model.UserRecom;
import com.learn.userRecomService.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest
{
    private UserRecom userRecom;
    private List<UserRecom> userList;

    @Mock
    private UserRepo repository;



    @InjectMocks
    private UserServiceImpl service;


    @BeforeEach
    public void setup(){
        userList=new ArrayList();

        userRecom = new UserRecom("1", "Baahubali", "70.0", "Ruling", "Fantastic Movie", "123456",
                "abc", "false", "true", "true", "test@mail.com");
        userList.add(userRecom);

    }

    @Test
    public void givenUserDetailsWhenUserDoesNotExistThenReturnSaveUser() {
        //Configured the behaviour of Mock object

        when(repository.save(any(UserRecom.class))).thenReturn(userRecom);

        //Call to Service method, whcih will in turn invoke methods on Mock objects
        UserRecom user = service.saveUser(userRecom);
        assertAll(
                ()->{assertNotNull(user);},
                ()->{assertTrue(user.getUserEmail().equals("test@mail.com"));},
                ()->{assertTrue(user.getTitle().equals("Baahubali"));}
        );

        //Verified when Mock calls were made by Service or not

        verify(repository).save(any(UserRecom.class));
    }
    @Test
    public void givenUserEmailThenShouldReturnListOfFavouriteUser(){
        when(repository.findByUserEmailAndIsFavourite(anyString(),anyString())).
                thenReturn(Optional.of(userList));
        when(repository.findByIsLikedAndMovieId(anyString(),anyString())).thenReturn(Optional.of(userList));
        List<TMDBMovies> result=service.getAuthUser(userRecom.getUserEmail());
        assertTrue(!result.isEmpty());
        verify(repository).findByUserEmailAndIsFavourite(anyString(),anyString());

    }
}
