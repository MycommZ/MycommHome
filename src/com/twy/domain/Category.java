package com.twy.domain;

import java.io.Serializable;

public class Category implements Serializable{
	private String id;//����
	private String name;//Ψһ����Ϊ��
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
