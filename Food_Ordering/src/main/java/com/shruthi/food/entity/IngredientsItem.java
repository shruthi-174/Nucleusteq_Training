package com.shruthi.food.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class IngredientsItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@ManyToOne
	private  IngredientCategory category;
	
	@JsonIgnore
	@ManyToOne
	private Restaurant restaurant;
	
	private boolean isStock=true;

	public IngredientsItem(Long id, String name, IngredientCategory category, Restaurant restaurant, boolean isStock) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.restaurant = restaurant;
		this.isStock = isStock;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IngredientCategory getCategory() {
		return category;
	}

	public void setCategory(IngredientCategory category) {
		this.category = category;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public boolean isStock() {
		return isStock;
	}

	public void setStock(boolean isStock) {
		this.isStock = isStock;
	}
	
	
}
