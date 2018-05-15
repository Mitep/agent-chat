package service.impl;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import service.interfaces.LoginServiceLocal;
import service.interfaces.UserServiceLocal;

@Stateless
public class LoginService implements LoginServiceLocal {

	public static final String LOOKUP = "java:app/UserAppEJB/";
	public static final String USER_SERVICE = "UserService!service.interfaces.UserServiceLocal";
	
	@Override
	public boolean validUser(String username, String password) {
		// TODO Auto-generated method stub
		try {
			InitialContext context = new InitialContext();
			
			UserServiceLocal usl = (UserServiceLocal) context.lookup(LOOKUP + USER_SERVICE);
			
			return usl.validateUser(username, password);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		
		return false;
	}


}
