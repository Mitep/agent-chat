package model;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.utils.IndexType;

/**
 * @author Nikola
 *
 */
@Entity
@Indexes({ @Index(fields = { @Field("username") }, options = @IndexOptions(unique = true, name = "PK_USER")),
		@Index(fields = {
				@Field(value = "$**", type = IndexType.TEXT) }, options = @IndexOptions(name = "SEARCH_INDEX")) })
public class User {

	@Id
	private ObjectId id;
	private String username;
	private String password;
	private String name;
	private String surname;
	private ArrayList<ObjectId> groups;
	private ArrayList<String> friends;
	private ArrayList<String> sentRequests;
	private ArrayList<String> receivedRequests;

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

	public ArrayList<ObjectId> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<ObjectId> groups) {
		this.groups = groups;
	}

	public ArrayList<String> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}

	public ArrayList<String> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(ArrayList<String> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public ArrayList<String> getReceivedRequests() {
		return receivedRequests;
	}

	public void setReceivedRequests(ArrayList<String> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}

	public User() {
		this.friends = new ArrayList<>();
		this.groups = new ArrayList<>();
		this.receivedRequests = new ArrayList<>();
		this.sentRequests = new ArrayList<>();
	}

	public User(String username, String password, String name, String surname) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
	}

	public User(String username, String password, String name, String surname, ArrayList<ObjectId> groups) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.groups = groups;
	}

}
