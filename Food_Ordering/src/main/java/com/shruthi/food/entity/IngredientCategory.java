package com.shruthi.food.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class IngredientCategory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@JsonIgnore
	@ManyToOne
	private Restaurant restaurant;
	
	@OneToMany(mappedBy="category", cascade=CascadeType.ALL)
	private List<IngredientsItem> ingredientsItem=new ArrayList<>();

	public IngredientCategory(Long id, String name, Restaurant restaurant, List<IngredientsItem> ingredientsItem) {
		super();
		this.id = id;
		this.name = name;
		this.restaurant = restaurant;
		this.ingredientsItem = ingredientsItem;
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

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<IngredientsItem> getIngredientsItem() {
		return ingredientsItem;
	}

	public void setIngredientsItem(List<IngredientsItem> ingredientsItem) {
		this.ingredientsItem = ingredientsItem;
	}
	
	
}
