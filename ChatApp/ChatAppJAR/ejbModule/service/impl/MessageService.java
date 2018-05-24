package service.impl;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.json.JSONObject;

import node.ChatAppNodeLocal;
import rest.RestLocal;
import service.interfaces.MessageServiceLocal;
import util.LookupConst;

@Stateless
public class MessageService implements MessageServiceLocal {

	private Context context;
	
	public MessageService() throws NamingException {
		context = new InitialContext();
	}
	
	@Override
	public void processMessage(String content) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		String host = node.isUserOnline(msg.getString("receiver"));
		if(host != null) {
			if(host.equals(node.getHost())) {
				forwardMessage(content, false);
			} else {
				// rest forward message
			}
		}
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		//mozda bude trebalo nesto drugo da se salje
		rl.saveMsg(content);
	}
	
	@Override
	public void forwardMessage(String content, boolean groupMessage) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		msg.remove("type");
		
		if(groupMessage)
			msg.put("type", "receive_group_message");
		else
			msg.put("type", "receive_message");
		
		node.getUserSession(msg.getString("receiver")).getAsyncRemote().sendText(msg.toString());
	}

	@Override
	public void processGroupMessage(String content) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		String groupId = msg.getString("receiver");
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		ArrayList<String> groupUsers = (ArrayList<String>) rl.groupUsers(groupId);
		
		for(String user : groupUsers) {
			String host = node.isUserOnline(user);
			if(host != null) {
				if(host.equals(node.getHost())) {
					forwardMessage(content, true);
				} else {
					// rest forward message
				}
			}
		}
		
		//mozda bude trebalo nesto drugo da se salje
		rl.saveMsg(content);	
	}
	
}
