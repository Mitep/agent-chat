package service.interfaces;

import javax.ejb.Local;
import javax.websocket.Session;

@Local
public interface LoginServiceLocal {

	public void loginUser(String userLogin, Session session) throws Exception;
	
	public void masterResponse(String response) throws Exception;
	
	public void slaveResponse(String response) throws Exception;
	
}
