package node;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 * Session Bean implementation class UserAppNode
 */
@Singleton
@LocalBean
public class UserAppNode implements UserAppNodeLocal {

	HashMap<String, String> activeUsers;

	/**
	 * Default constructor.
	 */
	public UserAppNode() {
		activeUsers = new HashMap<>();
	}

	@Override
	public boolean removeUser(String username) {
		if (activeUsers.remove(username) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void addUser(String username, String node) {
		activeUsers.put(username, node);
	}

	@Override
	public Map<String, String> getActiveUsers() {
		return activeUsers;
	}

}
