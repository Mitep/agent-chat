package service.impl;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.json.JSONArray;
import org.json.JSONObject;

import jms.ChatMsgSenderLocal;
import node.ChatAppNodeLocal;
import rest.RestLocal;
import service.interfaces.GroupServiceLocal;
import util.LookupConst;

@Stateless
public class GroupService implements GroupServiceLocal {

	private Context context;
	
	public GroupService() throws Exception {
		context = new InitialContext();
	}
	
	
	@Override
	public void createGroup(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "group_new");
		} else {
			// rest zahtev 2
		}
	}

	@Override
	public void removeGroup(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "group_delete");
		} else {
			// rest zahtev 2
		}
	}

	@Override
	public void addUserToGroup(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "group_add_user");
		} else {
			// rest zahtev 2
		}
		
	}

	@Override
	public void removeUserFromGroup(String content) throws Exception {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);

		if(node.isThisMaster()) {
			ChatMsgSenderLocal msgSender = (ChatMsgSenderLocal) context.lookup(LookupConst.CHAT_JMS_SENDER);
			msgSender.sendMsg(content, "group_remove_user");
		} else {
			// rest zahtev 2
		}
	}


	@Override
	public void createGroupResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		String receiver = obj.getString("admin");
		
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		if(node.isUserOnline(receiver) != null) {
			node.getUserSession(receiver).getAsyncRemote().sendText(content);
		}
	}


	@Override
	public void removeGroupResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		JSONArray ret = new JSONArray(obj.getJSONArray("users"));
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		for(int i = 0; i < ret.length(); i++) {
			if(node.isUserOnline(ret.getString(i)) != null)
				node.getUserSession(ret.getString(i)).getAsyncRemote().sendText(content);
		}
	}


	@Override
	public void addUserToGroupResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		ArrayList<String> gUsers = rl.groupUsers(obj.getString("id"));
		
		for(String u : gUsers) {
			if(node.isUserOnline(u) != null)
				node.getUserSession(u).getAsyncRemote().sendText(content);
		}
	}


	@Override
	public void removeUserFromGroupResponse(String content) throws Exception {
		JSONObject obj = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		ArrayList<String> gUsers = rl.groupUsers(obj.getString("id"));
		
		for(String u : gUsers) {
			if(node.isUserOnline(u) != null)
				node.getUserSession(u).getAsyncRemote().sendText(content);
		}
	}
	

}
