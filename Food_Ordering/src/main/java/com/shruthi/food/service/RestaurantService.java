package com.shruthi.food.service;

import java.util.List;

import com.shruthi.food.entity.Restaurant;
import com.shruthi.food.entity.User;
import com.shruthi.food.entity.UserFavoriteRestaurant;
import com.shruthi.food.request.CreateRestaurantRequest;

public interface RestaurantService {
	
	public Restaurant createRestaurant(CreateRestaurantRequest req, User user);
	
	public Restaurant updateRestaurant(Long restaurantId,CreateRestaurantRequest updateResataurant) throws Exception ;
	
	public void deleteRestaurant(Long restaurantId) throws Exception;
	
	public List<Restaurant> getAllRestaurant();
	
	public List<Restaurant> searchRestaurant(String keyword);

	public Restaurant findRestaurantById(Long id)throws Exception;
	
	public Restaurant findRestaurantByUserId(Long id)throws Exception;
	
	public UserFavoriteRestaurant addToFavourites(Long restaurntId,User user)throws Exception;
	
	public Restaurant updateRestaurantStatus(Long id) throws Exception;

}
