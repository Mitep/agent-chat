package service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import jms.ChatMsgSenderLocal;
import node.ChatAppNodeLocal;
import rest.RestLocal;
import service.interfaces.LoginServiceLocal;
import util.LookupConst;

@Singleton
public class LoginService implements LoginServiceLocal {

	private HashMap<String, Session> loginAttempt;
	
	private Context context;
	
	public LoginService() throws NamingException {
		loginAttempt = new HashMap<String, Session>();
		context = new InitialContext();
	}
	
	@Override
	public void loginUser(String userParams, Session session) throws NamingException, Exception {
		JSONObject obj = new JSONObject(userParams);
		String username = obj.getString("username");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		loginAttempt.put(username, session);
		
		obj.put("host", node.getHost());
		
		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(obj.toString() , "login");
		} else {
			// rest zahtev 2
			// slanje userappu zahtev za logovanje
		}
	}

	@Override
	public void masterResponse(String response) throws Exception {
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		slaveResponse(response);
		
		for(String hostname : node.getAllNodes().keySet()) {
			if(!hostname.equals("master")) {
				// saljemo rest zahtev
				// login response
				// koji gadja slaveResponse
			}
		}
	
	}

	@Override
	public void slaveResponse(String response) throws Exception {
		JSONObject obj = new JSONObject(response);
		
		String status = obj.getString("status");
		String username = obj.getString("username");
		String host = obj.getString("host");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		if(status.equals("success")) {
			RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
			
			JSONArray msgs = new JSONArray(rl.showUserMessages(username));
			
			obj.put("messages", msgs);
			obj.put("online_users", node.getOnlineUsersAsList());
			
			System.out.println("LOBOGANAIANIAINA");
			System.out.println(obj.toString());
			
			node.addOnlineUserApp(username, host);
		}
		
		if(host.equals(node.getHost())) {
			
			if(status.equals("success")) {
				HashMap<String, Session> thisNodeSessions = node.getAllUserSessions();
				for(String userSes : thisNodeSessions.keySet()) {
					thisNodeSessions.get(userSes).getAsyncRemote().sendText("{ \"type\":\"online_user\", \"username\":\""+userSes+"\" }");
				}
				node.addOnlineUserThisNode(username, loginAttempt.get(username));
			} 
			
			loginAttempt.get(username).getAsyncRemote().sendText(obj.toString());
			loginAttempt.remove(username);
		
		} else {
			
			if(status.equals("success")) {
				HashMap<String, Session> thisNodeSessions = node.getAllUserSessions();
				for(String userSes : thisNodeSessions.keySet()) {
					thisNodeSessions.get(userSes).getAsyncRemote().sendText("{ \"type\":\"online_user\", \"username\":\""+userSes+"\" }");
				}
				
			} 
			
		}
		
	}

}
