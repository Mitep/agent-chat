package node;

import java.util.HashMap;

import javax.ejb.Local;
import javax.websocket.Session;

@Local
public interface ChatAppNodeLocal {
	
	public boolean isThisMaster();
	
	public String getHost();
	
	public void addOnlineUserThisNode(String username, Session session);
	
	public void removeOnlineUserThisNode(String username);
	
	public void addOnlineUserApp(String username, String host);
	
	public void removeOnlineUserApp(String username);
	
	public Session getUserSession(String username);
	
	public HashMap<String, Session> getAllUserSessions();
	
	public HashMap<String, String> getAllNodes();
	
}
