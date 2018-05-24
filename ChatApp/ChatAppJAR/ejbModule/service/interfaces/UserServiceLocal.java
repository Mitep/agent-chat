package service.interfaces;

import javax.ejb.Local;

@Local
public interface UserServiceLocal {

	public void addUser(String content) throws Exception;
	
	public void removeUser(String content) throws Exception;
	
	public void acceptUser(String content) throws Exception;
	
	public void rejectUser(String content) throws Exception;
	
	public void addUserResponse(String content) throws Exception;
	
	public void removeUserResponse(String content) throws Exception;
	
	public void acceptUserResponse(String content) throws Exception;
	
	public void rejectUserResponse(String content) throws Exception;
	
	public void forwardResponse(String content) throws Exception;
	
}
