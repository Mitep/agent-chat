package com.userapp.model;

import java.util.ArrayList;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * @author Nikola
 *
 */
public class User {

	@Id
	private ObjectId id;
	private String username;
	private String password;
	private String name;
	private String surname;
	private ArrayList<ObjectId> friendships;
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public ArrayList<ObjectId> getFriendships() {
		return friendships;
	}

	public void setFriendships(ArrayList<ObjectId> friendships) {
		this.friendships = friendships;
	}

	public User() {
	}

	public User(ObjectId id, String username, String password, String name, String surname,
			ArrayList<ObjectId> friendships) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.friendships = friendships;
	}

}
