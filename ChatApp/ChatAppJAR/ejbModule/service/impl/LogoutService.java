package service.impl;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.Session;

import org.json.JSONObject;

import jms.ChatMsgSenderLocal;
import node.ChatAppNodeLocal;
import service.interfaces.LogoutServiceLocal;
import util.LookupConst;

@Stateless
public class LogoutService implements LogoutServiceLocal {

	private Context context;
	
	public LogoutService() throws NamingException {
		context = new InitialContext();
	}
	
	@Override
	public void logoutUser(String userLogin) throws NamingException, Exception {
		JSONObject obj = new JSONObject(userLogin);
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		obj.put("host", node.getHost());
		
		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(obj.toString() , "logout");
		} else {
			// rest zahtev 2
			// slanje userappu zahtev za logovanje
		}
	}

	@Override
	public void masterResponse(String response) throws NamingException {
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
	public void slaveResponse(String response) throws NamingException {
		JSONObject obj = new JSONObject(response);
		String status = obj.getString("status");
		String username = obj.getString("username");
		String host = obj.getString("host");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		if(status.equals("success")) {
			node.removeOnlineUserApp(username);
		}
		
		if(host.equals(node.getHost())) {
			
			if(status.equals("success")) {
				HashMap<String, Session> thisNodeSessions = node.getAllUserSessions();
				for(String userSes : thisNodeSessions.keySet()) {
					thisNodeSessions.get(userSes).getAsyncRemote().sendText("{ 'type':'offline_user', 'username':'"+username+"' }");
				}
			} 
			node.getUserSession(username).getAsyncRemote().sendText(response);
			node.removeOnlineUserThisNode(username);
			
		} else {
			
			if(status.equals("success")) {
				HashMap<String, Session> thisNodeSessions = node.getAllUserSessions();
				for(String userSes : thisNodeSessions.keySet()) {
					thisNodeSessions.get(userSes).getAsyncRemote().sendText("{ 'type':'offline_user', 'username':'"+username+"' }");
				}
				
			} 
			
		}

	}

	@Override
	public void autoLogoutUser(Session session) throws Exception {		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		String username = node.getSessionUsername(session);
		
		if(username != null) {
			
			if(node.isThisMaster()) {
				ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
				msgSender.sendMsg("{ \"username\"="+username+" }", "logout");
			} else {
				// rest zahtev 2
				// slanje userappu zahtev za logovanje
			}
			
		}
		
	}

}
