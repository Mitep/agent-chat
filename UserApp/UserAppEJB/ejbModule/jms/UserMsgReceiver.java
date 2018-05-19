package jms;

import java.util.List;
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

import dtos.JmsDTO;
import dtos.UserSearchDTO;
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
	public static final String SENDER_BEAN = "java:module/UserMsgSenderBean!jms.UserMsgSender";
	Logger log = Logger.getLogger("UserAppReceiver");

	@Override
	public void onMessage(Message msg) {
		log.info("Primio poruku preko UserQueue-a");
		try {
			String msgType = msg.getStringProperty("type");
			String msgContent = msg.getBody(String.class);

			switch (msgType) {

			case "login": {
				log.info("---------------PRIJAVA---------------");
				log.info(msgContent);
				Gson g = new Gson();
				User u = g.fromJson(msgContent, User.class);

				String username = u.getUsername();
				String password = u.getPassword();

				Context context = new InitialContext();
				LoginServiceLocal lsl = (LoginServiceLocal) context.lookup(LOOKUP + LOGIN_SERVICE);
				String retMsg;
				String retType = "login";
				JmsDTO dto = new JmsDTO();
				if (lsl.validUser(username, password)) {
					// vrati korisniku da mu je logovanje uspesno
					dto = new JmsDTO("login", username, "success", "Uspesno ste se prijavili!");
					log.info("Prijava uspesna.");
				} else {
					// vrati korisniku da mu je logovanje neuspesno
					dto = new JmsDTO("login", username, "fail", "Neuspesna prijava.");
					log.info("Prijava neuspesna.");
				}
				UserMsgSender msgSender = (UserMsgSender) context.lookup(SENDER_BEAN);
				retMsg = g.toJson(dto);
				msgSender.sendMsg(retMsg, retType);
			}
				break;

			case "register": {
				log.info("---------------REGISTRACIJA---------------");

				log.info(msgContent);

				Context ctx = new InitialContext();
				UserServiceLocal uf = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				User retVal = uf.createUser(msgContent);
				
				String retMsg;
				String retType = "register";
				JmsDTO dto;
				if (retVal != null) {
					dto = new JmsDTO(retType, retVal.getUsername(), "success", "Registracija uspesna.");
				} else {
					User u = new Gson().fromJson(msgContent, User.class);
					dto = new JmsDTO(retType, u.getUsername(), "fail", "Registracija neuspesna.");
				}
				retMsg = new Gson().toJson(dto);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(retMsg, retType);
			}
				break;

			case "user_search": {
				log.info("---------------PRETRAGA---------------");
				log.info(msgContent);
				
				Context ctx = new InitialContext();
				UserServiceLocal uf = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				UserSearchDTO src = new Gson().fromJson(msgContent, UserSearchDTO.class);
				List<User> retVal = uf.findUsers(src.getUsername(), src.getName(), src.getSurname());
				
				String retMsg;
				if(retVal != null) {
					retMsg = new Gson().toJson(retVal);
				}else {
					retMsg = "";
				}
				
				String retType = "user_search";
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(retMsg, retType);
			}
				break;

			case "friendship": {

			}

			case "group": {

			}
			
			case "logout": {
				
			}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		log.info("UserMsgReceiver -> kraj onMessage metode");
	}

}
