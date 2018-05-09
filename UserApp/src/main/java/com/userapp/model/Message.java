package com.userapp.model;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * @author Nikola
 *
 */
public class Message {

	public static final int PRIVATE_MSG = 0;
	public static final int GROUP_MSG = 1;
	
	@Id
	private ObjectId id;
	private int type;
	private ObjectId sender;
	private ObjectId receiver;
	private long timestamp;
	private String content;

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

	public ObjectId getSender() {
		return sender;
	}

	public void setSender(ObjectId sender) {
		this.sender = sender;
	}

	public ObjectId getReceiver() {
		return receiver;
	}

	public void setReceiver(ObjectId receiver) {
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

	public Message(ObjectId id, ObjectId sender, ObjectId receiver, long timestamp, String content) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.timestamp = timestamp;
		this.content = content;
	}

	public Message(ObjectId id, int type, ObjectId sender, ObjectId receiver, long timestamp, String content) {
		super();
		this.id = id;
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.timestamp = timestamp;
		this.content = content;
	}

}
