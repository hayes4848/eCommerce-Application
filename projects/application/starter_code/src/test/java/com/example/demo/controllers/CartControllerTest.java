package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
    }

    @Test
    public void add_to_cart(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("Andy");
        request.setItemId(1);
        request.setQuantity(2);

        User user = new User();
        user.setUsername("Andy");
        user.setId(1);

        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId((long) 1);
        item.setName("foo");
        item.setDescription("bar");
        item.setPrice(new BigDecimal(2.99));

        cart.addItem(item);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepository.findById((long) 1)).thenReturn(java.util.Optional.of(item));
        when(cartRepository.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foo", response.getBody().getItems().get(0).getName());
    }

    @Test
    public void add_to_cart_invalid_user_id(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("Ricky");
        request.setItemId(1);
        request.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_no_item(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("Andy");
        request.setItemId(1);
        request.setQuantity(2);

        User user = new User();
        user.setUsername("Andy");
        user.setId(1);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void remove_from_cart(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("Andy");
        request.setItemId(1);
        request.setQuantity(2);

        User user = new User();
        user.setUsername("Andy");
        user.setId(1);

        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId((long) 1);
        item.setName("foo");
        item.setDescription("bar");
        item.setPrice(new BigDecimal(2.99));

//        cart.addItem(item);

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(itemRepository.findById((long) 1)).thenReturn(java.util.Optional.of(item));
        when(cartRepository.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, cart.getItems().size());
    }
}
