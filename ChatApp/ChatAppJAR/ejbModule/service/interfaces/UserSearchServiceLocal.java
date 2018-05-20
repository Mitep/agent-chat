package service.interfaces;

import javax.ejb.Local;
import javax.naming.NamingException;
import javax.websocket.Session;

@Local
public interface UserSearchServiceLocal {

	public void searchUser(String searchRequest, Session session) throws Exception;
	
	public void response(String response) throws NamingException;
	
}
