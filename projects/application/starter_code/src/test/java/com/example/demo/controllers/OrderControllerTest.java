package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void get_history_for_user(){

        User user = new User();
        user.setUsername("Andy");
        user.setId(1);

        Item item = new Item();
        item.setDescription("foo");
        item.setPrice(new BigDecimal(2.99));

        List<Item> items = new ArrayList<Item>();
        items.add(item);

        UserOrder userOrder = new UserOrder();
        userOrder.setItems(items);

        List<UserOrder> orders = new ArrayList<UserOrder>();
        orders.add(userOrder);

        when(userRepository.findByUsername("Andy")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Andy");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foo", response.getBody().get(0).getItems().get(0).getDescription());
        assertEquals(new BigDecimal(2.99), response.getBody().get(0).getItems().get(0).getPrice());
    }

    @Test
    public void user_can_submit_order(){

        User user = new User();
        user.setUsername("Andy");
        user.setId(1);

        Item item = new Item();
        item.setDescription("foo");
        item.setPrice(new BigDecimal(2.99));

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);

        cart.addItem(item);
        cart.setTotal(new BigDecimal(2.99));

        List<Item> items = new ArrayList<Item>();
        items.add(item);

        UserOrder userOrder = new UserOrder();
        userOrder.setItems(items);
        userOrder.setUser(user);

        List<UserOrder> orders = new ArrayList<UserOrder>();
        orders.add(userOrder);

        when(userRepository.findByUsername("Andy")).thenReturn(user);
        when(orderRepository.save(userOrder)).thenReturn(userOrder);

        ResponseEntity<UserOrder> response = orderController.submit("Andy");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foo", response.getBody().getItems().get(0).getDescription());
        assertEquals(new BigDecimal(2.99), response.getBody().getItems().get(0).getPrice());
    }
}
