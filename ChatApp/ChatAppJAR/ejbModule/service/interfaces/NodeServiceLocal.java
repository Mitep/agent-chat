package service.interfaces;

import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface NodeServiceLocal {

	public String registerNode(String ipAddress) throws Exception;
	
	public void deregisterNode(String alias) throws Exception;
	
	public HashMap<String, String> getOnlineUsers() throws Exception;
	
	public HashMap<String, String> getOnlineNodes() throws Exception;
	
	
}
