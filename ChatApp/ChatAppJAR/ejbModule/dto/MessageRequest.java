package dto;

import java.util.ArrayList;

public class MessageRequest {
	 
	private String sender;
	
	private String receiver;
	
	private Long timestamp;
	
	private String content;
	
	public MessageRequest(){
		
	}
	
	private String getSender(){
		return sender;
	}
	
	private void setSender(String sender){
		this.sender = sender;
	}
	
	private String getReceiver(){
		return receiver;
	}
	
	private void setReceivers(String receiver){
		this.receiver = receiver;
	}
	
	private Long getTimestamp(){
		return timestamp;
	}
	
	private void setTimestamp(Long timestamp){
		this.timestamp = timestamp;
	}
	
	private String getContent(){
		return content;
	}
	
	private void setContent(String content){
		this.content = content;
	}
	
	
}
