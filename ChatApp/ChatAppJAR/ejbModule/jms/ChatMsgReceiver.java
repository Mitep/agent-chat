package jms;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import service.interfaces.LoginServiceLocal;
import service.interfaces.LogoutServiceLocal;
import service.interfaces.RegisterServiceLocal;
import service.interfaces.UserSearchServiceLocal;
import util.LookupConst;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="queue/chatAppQueue")                
        })
public class ChatMsgReceiver implements MessageListener {

	Logger log = Logger.getLogger("Websockets endpoint");

	
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
					lsl.masterResponse(msg.getBody(String.class));
	            };
	            	break;
	            case "logout": {
	            	LogoutServiceLocal lsl = (LogoutServiceLocal) context.lookup(LookupConst.CHAT_LOGOUT_SERVICE);
	            	lsl.masterResponse(msg.getBody(String.class));
	            };
	            	break;
	            case "user_search": {
	            	UserSearchServiceLocal ussl = (UserSearchServiceLocal) context.lookup(LookupConst.USER_SEARCH_SERVICE);
	            	ussl.response(msg.getBody(String.class));
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
