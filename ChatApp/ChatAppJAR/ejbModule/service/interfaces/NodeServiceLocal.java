package service.interfaces;

import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface NodeServiceLocal {

	public HashMap<String, String> registerNode(String ipAddress);
	
	public void deregisterNode(String alias);
	
	
}
