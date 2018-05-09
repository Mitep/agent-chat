package com.userapp.model;

import javax.persistence.Id;

import org.bson.types.ObjectId;

/**
 * @author Nikola
 *
 */
public class Message {

	@Id
	private ObjectId id;
	private int sender;
	private int receiver;
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

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
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

	public Message(ObjectId id, int sender, int receiver, long timestamp, String content) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.timestamp = timestamp;
		this.content = content;
	}

}
