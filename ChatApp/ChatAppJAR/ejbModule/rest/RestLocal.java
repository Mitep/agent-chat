package rest;

import java.util.List;

import javax.ejb.Local;

@Local
public interface RestLocal {

	public String showUserMessages(String content) throws Exception;
	
	public void saveMsg(String content) throws Exception;
	
	public List<String> groupUsers(String groupId) throws Exception;
	
}
