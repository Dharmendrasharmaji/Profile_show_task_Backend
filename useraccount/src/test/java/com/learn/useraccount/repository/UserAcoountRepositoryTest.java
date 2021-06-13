package com.learn.useraccount.repository;

import com.learn.useraccount.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserAcoountRepositoryTest {
    @Autowired
    private UserRepo repository;

    @BeforeEach
    public void setup(){
        User user1 = new User("0", "Charlie", "23","88764533","charlie@mail.com", "pass456");
        repository.save(user1);
    }





    @Test
    public void givenUserEmailWhenUserExistsThenReturnOptionalWithUser() {
        Optional<User> optionalUser = repository.findByEmail("charlie@mail.com");
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals("Charlie", user.getName());
    }

    @Test
    public void givenUserEmailWhenUserDoesntExistThenReturnEmptyOptional() {
        Optional<User> optionalUser = repository.findByEmail("john@mail.com");
        assertTrue(optionalUser.isEmpty());
    }


}
