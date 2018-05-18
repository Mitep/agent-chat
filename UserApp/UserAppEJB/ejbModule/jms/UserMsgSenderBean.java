package jms;

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
public class UserMsgSenderBean implements UserMsgSender {
	
	public static final String REMOTE_FACTORY = "java:jboss/exported/jms/RemoteConnectionFactory";
	public static final String CHATAPP_QUEUE = "java:jboss/exported/jms/queue/chatAppQueue";

	@Override
	public void sendMsg(String msgContent, String msgType) {
		try {
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context.lookup(REMOTE_FACTORY);
			final Queue queue = (Queue) context.lookup(CHATAPP_QUEUE);
			context.close();
			Connection connection = cf.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			connection.start();

			TextMessage msg = session.createTextMessage(msgContent);
			// The sent timestamp acts as the message's ID
			long sent = System.currentTimeMillis();
			msg.setLongProperty("sent", sent);
			msg.setStringProperty("type", msgType);
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
