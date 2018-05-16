package dtos;

/**
 * @author Nikola
 *
 */
public class FriendshipDTO {

	private String id;
	private String userId;
	private String userId2;
	private char status;

	public FriendshipDTO() {
		super();
	}

	public FriendshipDTO(String id, String userId, String userId2, char status) {
		super();
		this.id = id;
		this.userId = userId;
		this.userId2 = userId2;
		this.status = status;
	}

	public FriendshipDTO(String userId, String userId2, char status) {
		super();
		this.userId = userId;
		this.userId2 = userId2;
		this.status = status;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
