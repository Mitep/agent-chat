package service.interfaces;

import java.util.Collection;

import javax.ejb.Local;

import model.Group;

@Local
public interface GroupServiceLocal {

	public boolean createGroup(String str);
	public Group getGroup(String id);
	public Collection<Group> readAll();
	public boolean deleteGroup(String id);
}
