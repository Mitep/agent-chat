package service.interfaces;

import javax.ejb.Local;

@Local
public interface MessageServiceLocal {

	public void processMessage(String content) throws Exception;
	
	public void sendMessage(String content);
	
}
