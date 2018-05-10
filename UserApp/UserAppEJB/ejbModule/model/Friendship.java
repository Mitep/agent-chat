/**
 * 
 */
package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/**
 * @author Nikola
 *
 */
public class Friendship {


	public static final char FRIENDS = 'F';
	public static final char PENDING = 'P';

	@Id
	private ObjectId id;
	private ObjectId userId;
	private ObjectId userId2;
	private char status;

	public Friendship() {
	}

	public Friendship(ObjectId id, ObjectId userId, ObjectId userId2, char status) {
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

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public ObjectId getUserId2() {
		return userId2;
	}

	public void setUserId2(ObjectId userId2) {
		this.userId2 = userId2;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
}

}
