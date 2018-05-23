package service.interfaces;

import java.util.List;

import javax.ejb.Local;

import model.Message;

@Local
public interface MessageServiceLocal {

	public static String LOOKUP_GLOBAL = "java:global/UserAppEAR/UserAppEJB/MessageService!service.interfaces.MessageServiceLocal";

	public boolean createMessage(String str);

	public Message getMessage(String id);

	public List<Message> readAll();

	public boolean deleteMessage(String id);

	public boolean updateMessage(Message msg);

	public List<Message> getMessagesBySender(String sender);

	public List<Message> getRelatedMessages(String user);
}
