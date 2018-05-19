package service.impl;

import java.util.HashMap;

import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.Session;

import org.json.JSONObject;

import jms.ChatMsgSenderLocal;
import node.ChatAppNodeLocal;
import service.interfaces.LoginServiceLocal;
import util.LookupConst;
import websocket.WSLocal;

@Singleton
public class LoginService implements LoginServiceLocal {

	private HashMap<String, Session> loginAttempt;
	
	private Context context;
	
	public LoginService() throws NamingException {
		loginAttempt = new HashMap<String, Session>();
		context = new InitialContext();
	}
	
	@Override
	public void loginUser(String userParams, Session session) throws NamingException {
		JSONObject obj = new JSONObject(userParams);
		String username = obj.getString("username");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		loginAttempt.put(username, session);
		
		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);				
			try {
				msgSender.sendMsg(userParams , "login");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} else {
			//salje se rest zahtev
		}
	}

	@Override
	public void response(String response) throws NamingException {
		JSONObject obj = new JSONObject(response);
		String status = obj.getString("status");
		String username = obj.getString("username");
		
		if(status.equals("login_success")) {
			ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
			node.addOnlineUserThisNode(username, loginAttempt.get(username));
			loginAttempt.remove(username);
			
			WSLocal webSocket = (WSLocal) context.lookup(LookupConst.CHAT_WEB_SOCKET);
			webSocket.sendMsg(username, new JSONObject(response).toString());
		} else {
			loginAttempt.get(username).getAsyncRemote().sendText(response);
			loginAttempt.remove(username);
		}
		
	}

}
