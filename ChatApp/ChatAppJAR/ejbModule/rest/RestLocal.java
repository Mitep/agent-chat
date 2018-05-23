package rest;

import java.util.ArrayList;

import javax.ejb.Local;

@Local
public interface RestLocal {

	public String showUserMessages(String content) throws Exception;
	
	public String showUserGroupMessages(String content) throws Exception;
	
	public void saveMsg(String content) throws Exception;
	
	public ArrayList<String> groupUsers(String groupId) throws Exception;
	
}
