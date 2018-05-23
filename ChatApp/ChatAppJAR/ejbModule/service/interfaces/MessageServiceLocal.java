package service.interfaces;

import javax.ejb.Local;

@Local
public interface MessageServiceLocal {

	public void processMessage(String content) throws Exception;
	
	public void processGroupMessage(String content) throws Exception;
	
	public void forwardMessage(String content) throws Exception;
	
}
