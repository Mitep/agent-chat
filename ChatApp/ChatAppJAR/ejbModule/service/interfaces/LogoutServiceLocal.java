package service.interfaces;

import javax.ejb.Local;
import javax.naming.NamingException;

@Local
public interface LogoutServiceLocal {

	public void logoutUser(String userLogin) throws NamingException, Exception;
	
	public void masterResponse(String response) throws NamingException;
	
	public void slaveResponse(String response) throws NamingException;
	
	
}