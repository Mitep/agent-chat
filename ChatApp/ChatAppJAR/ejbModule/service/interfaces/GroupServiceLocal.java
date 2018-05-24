package service.interfaces;

import javax.ejb.Local;

@Local
public interface GroupServiceLocal {

	public void createGroup(String content) throws Exception;
	
	public void removeGroup(String content) throws Exception;
	
	public void addUserToGroup(String content) throws Exception;
	
	public void removeUserFromGroup(String content) throws Exception;
	
	public void createGroupResponse(String content) throws Exception;
	
	public void removeGroupResponse(String content) throws Exception;
	
	public void addUserToGroupResponse(String content) throws Exception;
	
	public void removeUserFromGroupResponse(String content) throws Exception;
	
	
}
