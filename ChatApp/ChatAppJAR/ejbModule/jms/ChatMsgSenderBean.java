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
	public void sendMsg(String msgContent) {
		// TODO Auto-generated method stub
		System.out.println("ovde smo bratko. nemas frke za sad.");
		try {
			final Properties env = new Properties();
			
			Context context = new InitialContext(env);
			ConnectionFactory cf = (ConnectionFactory) context
					.lookup("java:jboss/exported/jms/RemoteConnectionFactory");
			System.out.println("da li ovde baca xml?");
			final Queue queue = (Queue) context.lookup("java:jboss/exported/jms/queue/userAppQueue");
			context.close();
			Connection connection = cf.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			connection.start();

			//MessageConsumer consumer = session.createConsumer(queue);
			//consumer.setMessageListener(this);

		    TextMessage msg = session.createTextMessage(msgContent);
		    // The sent timestamp acts as the message's ID
		    long sent = System.currentTimeMillis();
		    msg.setLongProperty("sent", sent);
		    
			MessageProducer producer = session.createProducer(queue);
			producer.send(msg);
			//Thread.sleep(1000);
			producer.close();
			//consumer.close();
			connection.stop();
			connection.close();
			System.out.println("hip hop");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
