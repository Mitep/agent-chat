package service.interfaces;

import javax.ejb.Local;

@Local
public interface LoginServiceLocal {

	public boolean validUser(String username, String password);

}
