package beans;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
	public String sayHello();
	public void dbTest();
}
