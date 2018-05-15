package jms;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

@Stateless
public class ChatMsgSenderBean implements ChatMsgSender {

	@Override
	public void sendMsg(String msgContent, String type) {
		// TODO Auto-generated method stub
		try {
			final Properties env = new Properties();
			
			Context context = new InitialContext(env);
			ConnectionFactory cf = (ConnectionFactory) context
					.lookup("java:jboss/exported/jms/RemoteConnectionFactory");
	
			final Queue queue = (Queue) context.lookup("java:jboss/exported/jms/queue/userAppQueue");
			context.close();
			Connection connection = cf.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			connection.start();;

		    TextMessage msg = session.createTextMessage(msgContent);
		    // The sent timestamp acts as the message's ID
		    long sent = System.currentTimeMillis();
		    msg.setLongProperty("sent", sent);
		    msg.setStringProperty("type", type);
			MessageProducer producer = session.createProducer(queue);
			producer.send(msg);
		
			producer.close();
	
			connection.stop();
			connection.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
