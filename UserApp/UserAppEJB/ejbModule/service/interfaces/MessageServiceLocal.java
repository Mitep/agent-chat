package service.interfaces;

import java.util.Collection;

import javax.ejb.Local;

import model.Message;

@Local
public interface MessageServiceLocal {

	public boolean createMessage(String str);
	public Message getMessage(String id);
	public Collection<Message> readAll();
	public boolean deleteMessage(String id);
}
