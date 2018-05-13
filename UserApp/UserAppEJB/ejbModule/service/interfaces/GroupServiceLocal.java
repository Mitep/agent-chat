package service.interfaces;

import java.util.List;

import javax.ejb.Local;

import model.Group;

@Local
public interface GroupServiceLocal {

	public boolean createGroup(String str);
	public Group getGroup(String id);
	public List<Group> readAll();
	public boolean deleteGroup(String id);
	public boolean updateGroup(Group group);
}
