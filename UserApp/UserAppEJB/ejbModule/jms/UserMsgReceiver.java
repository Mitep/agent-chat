package jms;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.gson.Gson;

import model.User;
import service.interfaces.LoginServiceLocal;
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
	public static final String LOGIN_SERVICE = "LoginService!service.interfaces.LoginServiceLocal";
	Logger log = Logger.getLogger("UserAppReceiver");
	
	@Override
	public void onMessage(Message msg) {
		log.info("Primio poruku preko UserQueue-a");
		log.info("--------------**************--------------");
		// sadrzaj poruke ovde
		log.info("--------------**************--------------");

		try {
			String msgType = msg.getStringProperty("type");
			String msgContent = msg.getBody(String.class);
						
			if(msgType.equals("login")){				
				System.out.println("LOGOVANJE");
				log.info(msgContent);
				
				//JSONObject user = new JSONObject(msgContent);
				
				Gson g = new Gson();
				User u = g.fromJson(msgContent, User.class);
								
//				ObjectMapper mapper = new ObjectMapper();
//				LoginRequest lr = mapper.readValue(msgContent, LoginRequest.class);

				String username = u.getUsername();
				String password = u.getPassword();
				
				
				
				Context context = new InitialContext();
				LoginServiceLocal lsl = (LoginServiceLocal) context.lookup(LOOKUP + LOGIN_SERVICE);
				if(lsl.validUser(username, password)){
					//vrati korisniku da mu je logovanje uspesno
					System.out.println("solidna lozenga");
				} else {
					//vrati korisniku da mu je logovanje neuspesno
					System.out.println("prelosa lozenga");
				}
				
			}else if(msgType.equals("register")){				
				System.out.println("REGISTROVANJE");
				log.info(msgContent);
				
				Context context = new InitialContext();
				UserServiceLocal uf = (UserServiceLocal) context.lookup(LOOKUP + USER_SERVICE);
				uf.createUser(msgContent);
			}
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("UserMsgReceiver -> kraj onMessage metode");
	}

}
