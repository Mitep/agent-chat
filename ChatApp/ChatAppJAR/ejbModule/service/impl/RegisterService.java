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
import service.interfaces.RegisterServiceLocal;
import util.LookupConst;
import websocket.WSLocal;

@Singleton
public class RegisterService implements RegisterServiceLocal {

	private HashMap<String, Session> registrationAttempt;
	
	private Context context;
	
	public RegisterService() throws NamingException {
		registrationAttempt = new HashMap<String, Session>();
		context = new InitialContext();
	}
	
	@Override
	public void registerUser(String newUser, Session session) throws NamingException {
		
		JSONObject obj = new JSONObject(newUser);
		String username = obj.getString("username");
		
		registrationAttempt.put(username, session);
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);				
			try {
				msgSender.sendMsg(newUser , "register");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} else {
			// salje se rest zahtev
			// kad stigne response od resta prosledimo ga u metodu ispod
		}
		
	}

	@Override
	public void response(String response) throws NamingException {
		JSONObject obj = new JSONObject(response);
		String status = obj.getString("status");
		String username = obj.getString("username");
		
		if(status.equals("registration_success")) {
			ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
			node.addOnlineUserThisNode(username, registrationAttempt.get(username));
			registrationAttempt.remove(username);
			
			WSLocal webSocket = (WSLocal) context.lookup(LookupConst.CHAT_WEB_SOCKET);
			webSocket.sendMsg(username, new JSONObject(response).toString());
		} else {
			registrationAttempt.get(username).getAsyncRemote().sendText(response);
			registrationAttempt.remove(username);
		}
		
	}

}
