package jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import service.interfaces.LoginServiceLocal;
import service.interfaces.RegisterServiceLocal;
import util.LookupConst;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="queue/chatAppQueue")                
        })
public class ChatMsgReceiver implements MessageListener {

	private Context context;
	
	public ChatMsgReceiver() throws NamingException {
		context = new InitialContext();
	}
	
	@Override
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		try {
			String msgType = msg.getStringProperty("type");
		    switch (msgType) {
	            case "register": {
	            	RegisterServiceLocal rsl = (RegisterServiceLocal) context.lookup(LookupConst.CHAT_REGISTER_SERVICE);
					rsl.response(msg.getBody(String.class));
	            };
	            	break;
	            case "login": {
	            	LoginServiceLocal lsl = (LoginServiceLocal) context.lookup(LookupConst.CHAT_LOGIN_SERVICE);
					lsl.response(msg.getBody(String.class));
	            };
	            	break;
	            case "logout": {
	            	
	            };
	            	break;
				default:
					break;
		    	}
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
