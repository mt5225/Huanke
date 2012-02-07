package com.huanke.api;

public class Product {
	
	private String product_id;
	private String sku;
	private String name;
	private String short_description;
    private String description;
    
    public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeta_description() {
		return meta_description;
	}

	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
	}

	private String meta_description;
    
    

	public String getProduct_id() {
		return product_id;
	}

	
	@Override
	public String toString() {
		return "Product [product_id=" + product_id + ", sku=" + sku + ", name="
				+ name + ", short_description=" + short_description
				+ ", description=" + description + ", meta_description="
				+ meta_description + "]";
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product() {
	}

	public Product(String product_id, String sku, String name) {
		this.product_id = product_id;
		this.name = name;
		this.sku = sku;
	}

}
