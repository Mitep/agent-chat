package dtos;

/**
 * @author Nikola
 *
 */
public class MessageDTO {

	private String id;
	private int type;
	private String sender;
	private String receiver;
	private long timestamp;
	private String content;

	public MessageDTO() {
		super();
	}

	public MessageDTO(String id, int type, String sender, String receiver, long timestamp, String content) {
		super();
		this.id = id;
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.timestamp = timestamp;
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
