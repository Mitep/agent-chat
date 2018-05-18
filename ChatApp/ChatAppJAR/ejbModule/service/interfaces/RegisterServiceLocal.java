package service.interfaces;

import javax.ejb.Local;
import javax.naming.NamingException;
import javax.websocket.Session;

@Local
public interface RegisterServiceLocal {

	public void registerUser(String userData, Session session) throws NamingException;
	
	public void response(String response) throws NamingException;

}
