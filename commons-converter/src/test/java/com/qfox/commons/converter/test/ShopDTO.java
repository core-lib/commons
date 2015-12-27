package com.qfox.commons.converter.test;


public class ShopDTO extends Entity<Long> {
	private String name;
	private BrandDTO brand;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BrandDTO getBrand() {
		return brand;
	}

	public void setBrand(BrandDTO brand) {
		this.brand = brand;
	}

}
