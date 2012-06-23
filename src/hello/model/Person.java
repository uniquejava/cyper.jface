package hello.model;

import java.io.Serializable;

public class Person implements Serializable{
	private int id;
	private String name;
	private String gender;
	private String color;
	
	public Person(int id, String name, String gender, String color) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.color = color;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
