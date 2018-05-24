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
		String receiver = msg.getString("receiver");
		if(host != null) {
			if(host.equals(node.getHost())) {
				forwardMessage(receiver, content, false);
			} else {
				// rest forward message
			}
		}
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);

		msg.put("type", 0);
		rl.saveMsg(msg.toString());
	}
	
	@Override
	public void forwardMessage(String receiver, String content, boolean groupMessage) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		msg.remove("type");
		
		if(groupMessage)
			msg.put("type", "receive_group_message");
		else
			msg.put("type", "receive_message");
		
		node.getUserSession(receiver).getAsyncRemote().sendText(msg.toString());
	}

	@Override
	public void processGroupMessage(String content) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		String groupId = msg.getString("receiver");
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		ArrayList<String> groupUsers = rl.groupUsers(groupId);
		
		for(String user : groupUsers) {
			System.out.println("==============================================");

			System.out.println("user: " + user);
			System.out.println(msg.getString("sender"));
			System.out.println("==============================================");
			if(!user.equals(msg.getString("sender"))){
				String host = node.isUserOnline(user);
				if(host != null) {
					if(host.equals(node.getHost())) {
						forwardMessage(user, content, true);
					} else {
						// rest forward message
					}
				}
			}
		}
		
		msg.put("type", 1);
		rl.saveMsg(msg.toString());
	}
	
}
