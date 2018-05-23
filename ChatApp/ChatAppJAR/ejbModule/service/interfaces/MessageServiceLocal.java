package service.interfaces;

import javax.ejb.Local;

@Local
public interface MessageServiceLocal {

	public void processMessage(String content) throws Exception;
	
	public void processGroupMessage(String content) throws Exception;
	
	public void forwardMessage(String content) throws Exception;
	
	public void forwardGroupMessage(String content) throws Exception;
	
	public void showMessages(String content) throws Exception;
	
	public void showGroupMessages(String content) throws Exception;
	
}
