package service.impl;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.json.JSONObject;

import jms.ChatMsgSenderLocal;
import node.ChatAppNodeLocal;
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
		node.getUserSession(receiver).getAsyncRemote().sendText(content);	
	}


	@Override
	public void removeGroupResponse(String content) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addUserToGroupResponse(String content) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeUserFromGroupResponse(String content) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
