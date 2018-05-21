package service.impl;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import service.interfaces.MessageServiceLocal;

@Stateless
public class MessageService implements MessageServiceLocal {

	private Context context;
	
	public MessageService() throws NamingException {
		context = new InitialContext();
	}
	
	@Override
	public void processMessage(String content) throws Exception {
		// TODO Auto-generated method stub
		
		// proveriti jel receiver online
		// ako je online proslediti mu poruku i upisati u bazu
		// ako nije online samo je upisati
		// a
		
	}

	@Override
	public void sendMessage(String content) {
		// TODO Auto-generated method stub
		
	}

}
