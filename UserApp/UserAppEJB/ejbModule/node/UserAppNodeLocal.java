package node;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface UserAppNodeLocal {

	public static final String LOOKUP_GLOBAL = "java:global/UserAppEAR/UserAppEJB/UserAppNode!node.UserAppNodeLocal";

	public boolean removeUser(String username);

	public void addUser(String username, String node);

	public Map<String, String> getActiveUsers();
}
