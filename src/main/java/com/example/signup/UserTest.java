package com.example.signup;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

    class UserTest {
        @Test
        void testConstructor() {
            String expectedUsername = "teszt.elek";
            String expectedRole = "admin";

            User user = new User(expectedUsername, expectedRole);

            String actualUsername = user.getUsername();
            String actualRole = user.getRole();

            Assertions.assertEquals(expectedUsername, actualUsername);
            Assertions.assertEquals(expectedRole, actualRole);
        }
        void testGetUsername() {
            String expectedUsername = "teszt.elek";
            User user = new User(expectedUsername, "admin");

            String actualUsername = user.getUsername();

            Assertions.assertEquals(expectedUsername, actualUsername);
        }

        @Test
        void testGetRole() {
            String expectedRole = "admin";
            User user = new User("teszt.elek", expectedRole);

            String actualRole = user.getRole();

            Assertions.assertEquals(expectedRole, actualRole);
        }
    }
