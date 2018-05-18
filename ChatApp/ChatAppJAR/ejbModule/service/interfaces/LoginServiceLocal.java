package service.interfaces;

import javax.ejb.Local;
import javax.naming.NamingException;
import javax.websocket.Session;

@Local
public interface LoginServiceLocal {

	public void loginUser(String userLogin, Session session) throws NamingException;
	
	public void response(String response) throws NamingException;
	
}
