package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_item_by_id(){

        Item item = new Item();
        item.setDescription("foo");
        item.setPrice(new BigDecimal(3.45));
        item.setName("bar");

        when(itemRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(Long.valueOf(1));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foo", response.getBody().getDescription());
        assertEquals("bar", response.getBody().getName());
    }

    @Test
    public void cannot_find_item_by_id(){
        ResponseEntity<Item> response = itemController.getItemById(Long.valueOf(1));
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name(){

        List<Item> items = new ArrayList<Item>();

        Item item = new Item();
        item.setDescription("foo");
        item.setPrice(new BigDecimal(3.45));
        item.setName("bar");

        items.add(item);

        when(itemRepository.findByName("bar")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("bar");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foo", response.getBody().get(0).getDescription());
        assertEquals("bar", response.getBody().get(0).getName());
    }

    @Test
    public void get_all_items(){

        List<Item> items = new ArrayList<Item>();

        Item item = new Item();
        item.setDescription("foo");
        item.setPrice(new BigDecimal(3.45));
        item.setName("bar");

        items.add(item);

        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foo", response.getBody().get(0).getDescription());
        assertEquals("bar", response.getBody().get(0).getName());
    }
}
