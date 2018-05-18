package service.impl;

import java.util.HashMap;

import javax.ejb.Stateless;

import service.interfaces.NodeServiceLocal;

@Stateless
public class NodeService implements NodeServiceLocal {

	@Override
	public HashMap<String, String> registerNode(String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deregisterNode(String alias) {
		// TODO Auto-generated method stub
		
	}

}
