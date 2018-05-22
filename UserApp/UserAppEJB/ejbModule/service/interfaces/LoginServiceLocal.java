package service.interfaces;

import javax.ejb.Local;

@Local
public interface LoginServiceLocal {
	
	public static String LOOKUP_GLOBAL = "java:global/UserAppEAR/UserAppEJB/LoginService!service.interfaces.LoginServiceLocal";

	public boolean validUser(String username, String password);

}
