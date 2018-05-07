package com.userapp.ejb;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
	public String sayHello();
}
