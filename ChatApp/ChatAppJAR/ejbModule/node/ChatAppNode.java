package node;

import javax.websocket.Session;

import rest.RestLocal;
import service.interfaces.NodeServiceLocal;
import util.LookupConst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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
			try {
				Context contex = new InitialContext();
				RestLocal rl = (RestLocal) contex.lookup(LookupConst.REST);
				
				String host = rl.registerNode(getNodeAddress());
				setHostname(host);
								
				nodes = rl.getOnlineNodes();
				onlineUsersApp = rl.getOnlineUsers();
				
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}
	
	@PreDestroy
	public void removeNode() {
		//saljemo zahtev za uklanjanje iz klastera
		if(!isThisMaster()){
			//pozivamo node service za uklanjanje iz klastera
			try {
			
				Context contex = new InitialContext();
				RestLocal rl = (RestLocal) contex.lookup(LookupConst.REST);
				
				rl.deregisterNode(getHost());
			
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
		return getMasterAddress().equals(getNodeAddress());
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

	@Override
	public String isUserOnline(String username) {
		if(onlineUsersApp.containsKey(username))
			return onlineUsersApp.get(username);
		else
			return null;
	}

	@Override
	public ArrayList<String> getOnlineUsersAsList() {
		ArrayList<String> onlineUsers = new ArrayList<String>();
		for(String user : onlineUsersApp.keySet())
			onlineUsers.add(user);
		return onlineUsers;
	}

	@Override
	public String getNodeAddress() {
		// ko zna koja je i sta je
		
		return "localhost:8080";
	}

	@Override
	public String getMasterAddress() {
		
		String masterIp = null;
		
		try {
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println(new java.io.File("").getAbsolutePath());
			BufferedReader br = new BufferedReader(new FileReader("master_ip.txt"));
			masterIp = br.readLine();
			
			System.out.println("master ip address: " + masterIp);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
			br.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return masterIp;
	}

	@Override
	public void addOnlineNode(String alias, String ipAddr) {
		nodes.put(alias, ipAddr);
	}

	@Override
	public void deregisterNode(String alias) {
		nodes.remove(alias);
	}

	@Override
	public HashMap<String, String> getAllOnlineUsers() {
		return onlineUsersApp;
	}

}
