/**
 * 
 */
package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Nikola
 *
 */
@Entity
public class Friendship {

	public static final char FRIENDS = 'F';
	public static final char PENDING = 'P';

	@Id
	private ObjectId id;
	private String userId;
	private String userId2;
	private char status;

	public Friendship() {
	}

	public Friendship(String userId, String userId2, char status) {
		super();
		this.userId = userId;
		this.userId2 = userId2;
		this.status = status;
	}

	public Friendship(String id, String userId, String userId2, char status) {
		super();
		this.id = new ObjectId(id);
		this.userId = userId;
		this.userId2 = userId2;
		this.status = status;
	}

	public Friendship(ObjectId id, String userId, String userId2, char status) {
		super();
		this.id = id;
		this.userId = userId;
		this.userId2 = userId2;
		this.status = status;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId2() {
		return userId2;
	}

	public void setUserId2(String userId2) {
		this.userId2 = userId2;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

}
