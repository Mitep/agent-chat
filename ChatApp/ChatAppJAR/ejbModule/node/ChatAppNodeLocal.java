package node;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Local;
import javax.websocket.Session;

@Local
public interface ChatAppNodeLocal {
	
	public boolean isThisMaster();
	
	public String getNodeAddress();
	
	public String getMasterAddress();
	
	public String getHost();
	
	public void addOnlineUserThisNode(String username, Session session);
	
	public void removeOnlineUserThisNode(String username);
	
	public void addOnlineUserApp(String username, String host);
	
	public void removeOnlineUserApp(String username);
	
	public Session getUserSession(String username);
	
	public HashMap<String, Session> getAllUserSessions();
	
	public HashMap<String, String> getAllOnlineUsers();
	
	public HashMap<String, String> getAllNodes();
	
	public String getSessionUsername(Session s);
	
	public String isUserOnline(String username);

	public ArrayList<String> getOnlineUsersAsList();
	
	public void addOnlineNode(String alias, String ipAddr);
	
	public void deregisterNode(String alias);
	
}
