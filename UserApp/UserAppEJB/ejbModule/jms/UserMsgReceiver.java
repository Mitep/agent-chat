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

import model.User;
import service.interfaces.FriendshipServiceLocal;
import service.interfaces.GroupServiceLocal;
import service.interfaces.MessageServiceLocal;
import service.interfaces.UserServiceLocal;

@ApplicationScoped
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/userAppQueue") })
public class UserMsgReceiver implements MessageListener {

	public static final String LOOKUP = "java:app/UserAppEJB/";
	public static final String GROUP_SERVICE = "GroupService!service.interfaces.GroupServiceLocal";
	public static final String USER_SERVICE = "UserService!service.interfaces.UserServiceLocal";
	public static final String FRIENDSHIP_SERVICE = "FriendshipService!service.interfaces.FriendshipServiceLocal";
	public static final String MESSAGE_SERVICE = "MessageService!service.interfaces.MessageServiceLocal";
	Logger log = Logger.getLogger("UserAppReceiver");

	@Override
	public void onMessage(Message msg) {
		log.info("Primio poruku preko UserQueue-a");
		log.info("--------------**************--------------");
		// sadrzaj poruke ovde
		log.info("--------------**************--------------");
		// ovo je samo testiranje
//		Group g = new Group();
//		g.setName("grupaaaaaa");
//		model.Message m = new model.Message();
//		m.setContent("Tepicu macane, nisi ti za bacanje!");
//		m.setSender(new ObjectId("5af33e3fc424068e1559ee12"));
//		m.setReceiver(new ObjectId("5af33e3ec424068e1559ee10"));
//		m.setTimestamp(1525898183);
//		m.setType(model.Message.PRIVATE_MSG);
//
//		Friendship f = new Friendship();
//		f.setStatus(Friendship.PENDING);
//		f.setUserId(new ObjectId("5af33e3fc424068e1559ee12"));
//		f.setUserId2(new ObjectId("5af33e3ec424068e1559ee10"));
//
//		User u = new User();
//		u.setUsername("mitep");
//		u.setPassword("123");
//		u.setSurname("tepic");
//		u.setName("milos");
//
//		User u2 = new User();
//		u2.setUsername("mitep");

		try {
			Context context = new InitialContext();
			GroupServiceLocal gf = (GroupServiceLocal) context.lookup(LOOKUP + GROUP_SERVICE);
			gf.deleteGroup("5af80d29fb97111e146ace71");
//			log.info("Grupa ima: " + gf.readAll().size());
//			Group grupa = gf.getGroup("5af80d29fb97111e146ace71");
//			grupa.setName("Za*ebani momci");
//			gf.updateGroup(grupa);
//			log.info("Group get test: " + grupa.getName());
			MessageServiceLocal cf = (MessageServiceLocal) context.lookup(LOOKUP + MESSAGE_SERVICE);
			cf.deleteMessage("5af80d28fb97111e146ace6f");
//			log.info("Poruka ima: " + cf.readAll().size());
//			model.Message m = cf.getMessage("5af80d28fb97111e146ace6f");
//			m.setContent("Dobra fora...");
//			cf.updateMessage(m);
//			log.info("Message get test: " + m.getContent());
//			Gson gs = new Gson();
//			cf.createMessage(gs.toJson(m));
//			gf.createGroup(gs.toJson(g));

			FriendshipServiceLocal fs = (FriendshipServiceLocal) context.lookup(LOOKUP + FRIENDSHIP_SERVICE);
//			Friendship fr = new Friendship(new ObjectId("5af80d29fb97111e146ace75"),
//					new ObjectId("5af33e3ec424068e1559ee10"), new ObjectId("5af33e3fc424068e1559ee12"),
//					Friendship.FRIENDS);
			fs.deleteFriendship("5af80d29fb97111e146ace75");
//			fs.updateFriendship(fr);
//			log.info("Prijateljstava ima: " + fs.readAll().size());
//			Friendship f = fs.getFriendship("5af80d29fb97111e146ace75");
//			log.info("Friendship get test: " + f.getStatus());
//			fs.createFriendship(gs.toJson(f));
			UserServiceLocal uf = (UserServiceLocal) context.lookup(LOOKUP + USER_SERVICE);
			User u = uf.getUserByUsername("M1T3P");
			uf.deleteUser(u.getId().toHexString());
//			log.info("Korisnika ima: " + uf.readAll().size());
//			User u  = uf.getUser("5af80d29fb97111e146ace77");
//			u.setUsername("M1T3P");
//			uf.updateUser(u);
//			log.info("Friendship get test: " + u.getUsername());
//			uf.createUser(gs.toJson(u));
//			log.info("Kreirao prvog korisnika, testiranje duplikata");
//			uf.createUser(gs.toJson(u2));
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (NullPointerException ne) {
			ne.getMessage();
			ne.printStackTrace();
		}
		log.info("UserMsgReceiver -> kraj onMessage metode");
	}

}
