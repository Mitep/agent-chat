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
import service.interfaces.UserSearchServiceLocal;
import util.LookupConst;

@Stateless
public class UserSearchService implements UserSearchServiceLocal {
	
	private HashMap<String, Session> searchers;
	
	public UserSearchService() {
		searchers = new HashMap<String, Session>();
	}
	
	@Override
	public void searchUser(String searchRequest, Session session) throws Exception {
		JSONObject obj = new JSONObject(searchRequest);
		String searcher = obj.getString("searcher");
		
		Context context = new InitialContext();
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		searchers.put(searcher, session);
		
		if(node.isThisMaster()) {
			
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(searchRequest , "search");
			
		} else {
			// rest request 
			// kad stigne pozivamo response ispod
		}
	}

	@Override
	public void response(String response) throws NamingException {
		JSONObject obj = new JSONObject(response);
		String searcher = obj.getString("searcher");
		
		searchers.get(searcher).getAsyncRemote().sendText(response);
	}

}
