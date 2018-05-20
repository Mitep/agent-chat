package node;

import javax.websocket.Session;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ChatAppNode implements ChatAppNodeLocal {

	// key - alias, value - ip_addr:port
	private HashMap<String, String> nodes;
	
	// sesije online korisnika na ovom nodu
	private HashMap<String, Session> onlineUsersThisNode;
	
	// hostovi online korisnika u celoj aplikaciji
	private HashMap<String, String> onlineUsersApp;
	
	private String hostname;

	public ChatAppNode() {
		nodes = new HashMap<String, String>();
		onlineUsersThisNode = new HashMap<String, Session>();
		onlineUsersApp = new HashMap<String, String>();
	}
	
	@PostConstruct
	public void initNode() {
		if(isThisMaster()){
			setHostname("master");
		} else {
			//pozivamo node service za dodavanje u klaster
		}
	}
	
	@PreDestroy
	public void removeNode() {
		//saljemo zahtev za uklanjanje iz klastera
		if(!isThisMaster()){
			//pozivamo node service za uklanjanje iz klastera
		}
	}
	
	private void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	private String getHostname() {
		return hostname;
	}
	
	public void addNode(String alias, String address) {
		nodes.put(alias, address);
	}
	
	public void addNodes(HashMap<String, String> newNodes) {
		nodes.putAll(newNodes);
	}
	
	public void removeNode(String alias) {
		nodes.remove(alias);
	}
	
	public HashMap<String, String> getNodes() {
		return nodes;
	}
	
	public void addOnlineUsersAppAll(HashMap<String, String> users) {
		onlineUsersApp.putAll(users);
	}
	
	@Override
	public void addOnlineUserThisNode(String username, Session session) {
		onlineUsersThisNode.put(username, session);
	}

	@Override
	public boolean isThisMaster() {
		//proveravamo jel ovo master da bi znali da li da saljemo zahtev za registrovanje u klaster
		//treba napraviti metodu koja ispituje
		return true;
	}

	@Override
	public Session getUserSession(String username) {
		return onlineUsersThisNode.get(username);
	}

	@Override
	public String getHost() {
		return getHostname();
	}

	@Override
	public void addOnlineUserApp(String username, String host) {
		onlineUsersApp.put(username, host);
	}

	@Override
	public HashMap<String, Session> getAllUserSessions() {
		return onlineUsersThisNode;
	}

	@Override
	public HashMap<String, String> getAllNodes() {
		return nodes;
	}

	@Override
	public void removeOnlineUserThisNode(String username) {
		onlineUsersThisNode.remove(username);
	}

	@Override
	public void removeOnlineUserApp(String username) {
		onlineUsersApp.remove(username);
	}

	@Override
	public String getSessionUsername(Session s) {
		for(String user : onlineUsersThisNode.keySet()) {
			if(s == getUserSession(user))
				return user;
		}
		
		return null;
	}

}
