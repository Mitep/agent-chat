package service.interfaces;

import java.util.Collection;

import javax.ejb.Local;

import model.User;

@Local
public interface UserServiceLocal {

	public boolean createUser(String str);
	public User getUser(String id);
	public Collection<User> readAll();
	public boolean deleteUser(String id);
}
