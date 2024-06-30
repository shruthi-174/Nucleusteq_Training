package com.shruthi.food.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RestuarantDTO{
	private Long id;
	
	private String title;
	
	@Column(length=1000)
	private List<String> images;
	
	private String description;
	
}
