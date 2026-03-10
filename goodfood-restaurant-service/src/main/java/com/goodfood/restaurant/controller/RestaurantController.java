package com.goodfood.restaurant.controller;

import com.goodfood.restaurant.entity.Restaurant;
import com.goodfood.restaurant.repository.RestaurantRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','RESTAURANT_OWNER')")
    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return repository.save(restaurant);
    }
}
