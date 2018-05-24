package service.impl;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import node.ChatAppNodeLocal;
import service.interfaces.NodeServiceLocal;
import util.LookupConst;

@Stateless
public class NodeService implements NodeServiceLocal {

	@Override
	public String registerNode(String ipAddress) throws Exception {
		Context context = new InitialContext();
		ChatAppNodeLocal canl = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		HashMap<String, String> nodes = canl.getAllNodes();
		String slaveName = "slave_" + nodes.size();
		canl.addOnlineNode(slaveName, ipAddress);
		return slaveName;
	}

	@Override
	public void deregisterNode(String alias) throws Exception {
		Context context = new InitialContext();
		ChatAppNodeLocal canl = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		canl.deregisterNode(alias);
	}

	@Override
	public HashMap<String, String> getOnlineUsers() throws Exception {
		Context context = new InitialContext();
		ChatAppNodeLocal canl = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		return canl.getAllOnlineUsers();
	}

	@Override
	public HashMap<String, String> getOnlineNodes() throws Exception {
		Context context = new InitialContext();
		ChatAppNodeLocal canl = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		return canl.getAllNodes();
	}

}
