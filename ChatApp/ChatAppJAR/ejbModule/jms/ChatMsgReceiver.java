package jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import websocket.WSLocal;

@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationType", 
                propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination",
                propertyValue="queue/chatAppQueue")                
        })
public class ChatMsgReceiver implements MessageListener {


	@Override
	public void onMessage(Message msg) {
		// TODO Auto-generated method stub
		try {
			System.out.println(msg.getBody(String.class));
			Context context = new InitialContext();
			WSLocal ws = (WSLocal) context.lookup("java:module/WSBean!websocket.WSLocal");
			ws.sendMsg(msg.getBody(String.class), "REGISTERED!");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
