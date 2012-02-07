package com.huanke.api;

public class ProductImage {
	private String label;
	private String position;
	private String url;
	private String file;
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ProductImage() {
		super();
	}

	public ProductImage(String label, String position, String url) {
		super();
		this.label = label;
		this.position = position;
		this.url = url;
	}

	@Override
	public String toString() {
		return "ProductImage [label=" + label + ", position=" + position
				+ ", url=" + url + ", file=" + file + "]";
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
