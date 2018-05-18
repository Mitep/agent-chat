package service.interfaces;

import java.util.List;

import javax.ejb.Local;

import org.bson.types.ObjectId;

import model.Group;

@Local
public interface GroupServiceLocal {

	public boolean createGroup(String str);
	public Group getGroup(String id);
	public List<Group> readAll();
	public boolean deleteGroup(String id);
	public boolean updateGroup(Group group);
	public boolean addMember(String group, String member);
	public boolean removeMember(String group, String member);
	public List<ObjectId> getMessages(String group);
	public boolean addMessage(String group, String message);
	public boolean removeMessage(String group, String message);
}
