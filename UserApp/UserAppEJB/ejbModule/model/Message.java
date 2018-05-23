package model;

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
@Indexes({ @Index(fields = {
		@Field(value = "sender", type = IndexType.TEXT) }, options = @IndexOptions(name = "SENDER_INDEX")) })
public class Message {

	public static final int PRIVATE_MSG = 0;
	public static final int GROUP_MSG = 1;

	@Id
	private ObjectId id;
	private int type;
	private String sender;
	private String receiver;
	private String content;
	private long timestamp;

	public Message() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Message(ObjectId id, String sender, String receiver, String content) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}

	public Message(ObjectId id, int type, String sender, String receiver, String content) {
		super();
		this.id = id;
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}

	public Message(ObjectId id, int type, String sender, String receiver, String content, long timestamp) {
		super();
		this.id = id;
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
	}

}
