package node;

import javax.ejb.Local;
import javax.websocket.Session;

@Local
public interface ChatAppNodeLocal {
	
	public boolean isThisMaster();
	
	public void addOnlineUserThisNode(String username, Session session);
	
	public Session getUserSession(String username);
	
}
