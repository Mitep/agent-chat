package service.interfaces;

import java.util.List;

import javax.ejb.Local;

import dtos.UserSearchDTO;
import model.User;

@Local
public interface UserServiceLocal {
	
	public static String LOOKUP_GLOBAL = "java:global/UserAppEAR/UserAppEJB/UserService!service.interfaces.UserServiceLocal";

	public User createUser(String str);

	public User getUser(String id);

	public List<User> readAll();

	public boolean deleteUser(String username);

	public boolean updateUser(User user);

	public User getUserByUsername(String username);

	public List<User> getUserByName(String name);

	public List<User> getUserBySurname(String surname);

	public boolean validateUser(String username, String password);

	public List<String> getUsersGroups(String username);

	public List<String> addGroup(String username, String groupId);

	public List<User> findUsers(String username, String name, String surname);

	public List<UserSearchDTO> findUsersDTO(String username, String name, String surname);

	public boolean addFriend(String user, String friend);

	public boolean removeFriend(String user, String friend);

	public boolean sendFriendRequest(String user, String friend);

	public boolean acceptFriendRequest(String user, String friend);

	public boolean rejectFriendRequest(String user, String friend);
}
