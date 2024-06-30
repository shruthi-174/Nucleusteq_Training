package com.shruthi.food.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IngredientsItem {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
}
