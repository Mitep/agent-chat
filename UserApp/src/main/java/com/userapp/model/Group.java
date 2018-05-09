package com.userapp.model;

import java.util.ArrayList;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * @author Nikola
 *
 */
public class Group {

	@Id
	private ObjectId id;
	private int name;
	private ArrayList<ObjectId> members;

	public Group() {
	}

	public Group(ObjectId id, int name, ArrayList<ObjectId> members) {
		super();
		this.id = id;
		this.name = name;
		this.members = members;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public ArrayList<ObjectId> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<ObjectId> members) {
		this.members = members;
	}

}
