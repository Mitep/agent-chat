package service.interfaces;

import javax.ejb.Local;
import javax.naming.NamingException;
import javax.websocket.Session;

@Local
public interface LoginServiceLocal {

	public void loginUser(String userLogin, Session session) throws NamingException, Exception;
	
	public void masterResponse(String response) throws NamingException;
	
	public void slaveResponse(String response) throws NamingException;
	
}
