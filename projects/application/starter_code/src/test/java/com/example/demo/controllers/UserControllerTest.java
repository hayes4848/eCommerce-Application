package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("Test");
        req.setPassword("password");
        req.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(req);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
    }

    @Test
    public void find_user_by_username(){

        User user = new User();
        user.setUsername("Andy");

        when(userRepository.findByUsername("Andy")).thenReturn(user);
        final ResponseEntity<User> response = userController.findByUserName("Andy");
        System.out.println(response);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Andy", response.getBody().getUsername());
    }

    @Test
    public void find_user_by_id(){

        User user = new User();
        user.setUsername("Andy");
        user.setId(1);

        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        final ResponseEntity<User> response = userController.findById(user.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Andy", response.getBody().getUsername());
    }
}
