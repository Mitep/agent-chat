package service.impl;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.json.JSONObject;

import jms.ChatMsgSenderLocal;
import node.ChatAppNodeLocal;
import service.interfaces.UserServiceLocal;
import util.LookupConst;

@Stateless
public class UserService implements UserServiceLocal {

	private Context context;
	
	public UserService() throws NamingException {
		context = new InitialContext();
	}
	
	@Override
	public void addUser(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "friend_add");
		} else {
			// rest zahtev 2
		}
	}

	@Override
	public void removeUser(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "friend_remove");
		} else {
			// rest zahtev 2
		}
	}

	@Override
	public void acceptUser(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "friend_accept");
		} else {
			// rest zahtev 2
		}
	}

	@Override
	public void rejectUser(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "friend_reject");
		} else {
			// rest zahtev 2
		}
	}


	@Override
	public void addUserResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		String receiver = obj.getString("receiver");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		node.getUserSession(receiver).getAsyncRemote().sendText(content);
	}


	@Override
	public void removeUserResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		String receiver = obj.getString("receiver");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		node.getUserSession(receiver).getAsyncRemote().sendText(content);	
	}


	@Override
	public void acceptUserResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		String receiver = obj.getString("receiver");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		node.getUserSession(receiver).getAsyncRemote().sendText(content);
			
	}


	@Override
	public void rejectUserResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		String receiver = obj.getString("receiver");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		node.getUserSession(receiver).getAsyncRemote().sendText(content);	
	}

	@Override
	public void forwardResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		String receiver = obj.getString("receiver");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		node.getUserSession(receiver).getAsyncRemote().sendText(content);
			
	}

}
