package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

@IntegrationTest
public class UserTest {
    private static UserRepository userRepository;
    private long initialNumberOfUsers;

    @BeforeAll
    public static void setup(@Autowired UserRepository userRepository) {
        UserTest.userRepository = userRepository;
    }

    @BeforeEach
    void beforeEachSetup() {
        initialNumberOfUsers = UserTest.userRepository.count();
    }

    @Test
    void testValidUserSaved() {
        // given
        User user = userWithoutPassword().password("passwordP1&")
                                         .build();

        // when
        userRepository.save(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers + 1, userRepository.count());
    }

    private User.UserBuilder userWithoutPassword() {
        return User.builder()
                   .username("username")
                   .email("email");
    }

    @Test
    void testPasswordLessThan8CharactersIsInvalid() {
        // given
        User user = userWithoutPassword().password("pP1&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    void tryToSaveInvalidUser(User user) {
        try {
            // when
            userRepository.save(user);
        } catch (TransactionSystemException expected) {
        }
    }

    @Test
    void testPasswordWithNoDigitIsInvalid() {
        // given
        User user = userWithoutPassword().password("passwordP&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    @Test
    void testPasswordWithNoUppercaseCharacterIsInvalid() {
        // given
        User user = userWithoutPassword().password("password1&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    @Test
    void testPasswordWithNoSpecialCharacterIsInvalid() {
        // given
        User user = userWithoutPassword().password("passwordP1")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }
}