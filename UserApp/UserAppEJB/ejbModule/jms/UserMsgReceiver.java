package jms;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.gson.Gson;

import model.Group;
import service.interfaces.GroupServiceLocal;

@ApplicationScoped
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/userAppQueue") })
public class UserMsgReceiver implements MessageListener {

	public static final String LOOKUP = "java:app/UserAppEJB/";
	public static final String GROUP_SERVICE = "GroupService!service.GroupServiceLocal";
	public static final String USER_SERVICE = "UserService!service.UserServiceLocal";
	public static final String FRIENDSHIP_SERVICE = "FriendshipService!service.FriendshipServiceLocal";
	public static final String MESSAGE_SERVICE = "MessageService!service.MessageServiceLocal";
	Logger log = Logger.getLogger("UserAppReceiver");

	@Override
	public void onMessage(Message msg) {
		log.info("Primio poruku preko UserQueue-a");
		log.info("--------------**************--------------");
		// sadrzaj poruke ovde
		log.info("--------------**************--------------");
		// ovo je samo testiranje
		Group g = new Group();
		g.setName("grupaaaaaa");
		try {
			Context context = new InitialContext();
			GroupServiceLocal cf = (GroupServiceLocal) context.lookup(LOOKUP + GROUP_SERVICE);
			Gson gs = new Gson();
			cf.createGroup(gs.toJson(g));
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
