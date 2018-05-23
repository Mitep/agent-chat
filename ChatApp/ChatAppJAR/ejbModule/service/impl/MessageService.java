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
				forwardMessage(content);
			} else {
				// rest forward message
			}
		}
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		//mozda bude trebalo nesto drugo da se salje
		rl.saveMsg(msg.toString());
	}
	
	@Override
	public void forwardMessage(String content) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		msg.remove("type");
		msg.put("type", "receive_message");
		node.getUserSession(msg.getString("receiver")).getAsyncRemote().sendText(msg.toString());
	}
	
	@Override
	public void showMessages(String content) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
				
		JSONObject response = new JSONObject();
		response.put("type", msg.getString("type"));
		response.put("my_username", msg.getString("my_username"));
		response.put("friends_username", msg.getString("friends_username"));
		
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		String messages = rl.showUserMessages(content);
		response.put("data", messages);
		
		node.getUserSession(msg.getString("my_username")).getAsyncRemote().sendText(response.toString());
	}

	@Override
	public void processGroupMessage(String content) throws Exception {
		JSONObject msg = new JSONObject(content);
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		
		String groupId = msg.getString("receiver");
		
		RestLocal rl = (RestLocal) context.lookup(LookupConst.REST);
		ArrayList<String> groupUsers = rl.groupUsers(msg.getString("my_group_neki_id"));
			
	}
	
}
