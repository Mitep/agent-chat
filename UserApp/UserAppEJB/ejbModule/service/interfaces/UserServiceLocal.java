package service.interfaces;

import java.util.List;

import javax.ejb.Local;

import model.User;

@Local
public interface UserServiceLocal {

	public boolean createUser(String str);
	public User getUser(String id);
	public List<User> readAll();
	public boolean deleteUser(String id);
	public boolean updateUser(User user);
	public User getUserByUsername(String username);
	public boolean validateUser(String username, String password);
}
