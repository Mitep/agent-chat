package rest;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

@Local
public interface RestLocal {

	public String showUserMessages(String content) throws Exception;
	
	public void saveMsg(String content) throws Exception;
	
	public List<String> groupUsers(String groupId) throws Exception;
	
	public String registerNode(String ipAddress);
	
	public void deregisterNode(String alias);
	
	public HashMap<String, String> getOnlineUsers();
	
	public HashMap<String, String> getOnlineNodes();
	
}
