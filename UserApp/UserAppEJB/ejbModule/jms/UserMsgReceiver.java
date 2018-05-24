package jms;

import java.util.ArrayList;
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

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import dtos.UserSearchDTO;
import model.Group;
import model.User;
import node.UserAppNodeLocal;
import service.interfaces.GroupServiceLocal;
import service.interfaces.LoginServiceLocal;
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
	public static final String LOGIN_SERVICE = "LoginService!service.interfaces.LoginServiceLocal";
	public static final String SENDER_BEAN = "java:module/UserMsgSenderBean!jms.UserMsgSender";
	public static final String SENDER_GLOBAL = "java:global/UserAppEAR/UserAppEJB/UserMsgSenderBean!jms.UserMsgSender";

	public static final String USER_APP_NODE = "java:module/UserAppNode!node.UserAppNodeLocal";
	public static final String USER_SERVICE_LOCAL = "java:module/UserService!service.interfaces.UserServiceLocal";
	public static final String FRIENDSHIP_SERVICE_LOCAL = "java:module/FriendshipService!service.interfaces.FriendshipServiceLocal";
	public static final String LOGIN_SERVICE_LOCAL = "java:module/LoginService!service.interfaces.LoginServiceLocal";
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

				JSONObject o = new JSONObject(msgContent);
				String host = o.getString("host");
				String username = o.getString("username");
				String password = o.getString("password");

				Context context = new InitialContext();
				LoginServiceLocal lsl = (LoginServiceLocal) context.lookup(LOOKUP + LOGIN_SERVICE);
				String retType = "login";

				JSONObject response = new JSONObject();

				if (lsl.validUser(username, password)) {
					// vrati korisniku da mu je logovanje uspesno
					response.put("info", "Uspesno ste se prijavili!");
					response.put("status", "success");
					log.info("Prijava uspesna.");

					UserAppNodeLocal uanl = (UserAppNodeLocal) context.lookup(USER_APP_NODE);
					uanl.addUser(username, host);
					UserServiceLocal usl = (UserServiceLocal) context.lookup(LOOKUP + USER_SERVICE);

					User user = usl.getUserByUsername(username);

					JSONObject jsonUser = new JSONObject(user);
					jsonUser.remove("password");
					response.put("data", jsonUser);
				} else {
					// vrati korisniku da mu je logovanje neuspesno
					response.put("info", "Neuspesna prijava.");
					response.put("status", "fail");

					log.info("Prijava neuspesna.");
				}
				response.put("type", "login");
				response.put("username", username);

				UserMsgSender msgSender = (UserMsgSender) context.lookup(SENDER_BEAN);
				response.put("host", host);
				msgSender.sendMsg(response.toString(), retType);
			}
				break;

			case "register": {
				log.info("---------------REGISTRACIJA---------------");

				log.info(msgContent);

				Context ctx = new InitialContext();
				UserServiceLocal uf = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				User retVal = uf.createUser(msgContent);

				JSONObject response = new JSONObject();
				response.put("type", "register");
				if (retVal != null) {
					response.put("username", retVal.getUsername());
					response.put("status", "success");
					response.put("info", "Registracija uspesna.");
				} else {
					response.put("username", new JSONObject(msgContent).get("username"));
					response.put("status", "fail");
					response.put("info", "Registracija neuspesna.");
				}
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "register");
			}
				break;

			case "user_search": {
				log.info("---------------PRETRAGA---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				UserServiceLocal uf = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				UserSearchDTO src = new Gson().fromJson(msgContent, UserSearchDTO.class);

				List<UserSearchDTO> retVal = uf.findUsersDTO(src.getUsername(), src.getName(), src.getSurname());

				JSONObject requestMsg = new JSONObject(msgContent);

				JSONObject responseMsg = new JSONObject();
				responseMsg.put("searcher", requestMsg.getString("searcher"));
				responseMsg.put("type", "user_search");
				responseMsg.put("data", new JSONArray(retVal));

				String retType = "user_search";
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(responseMsg.toString(), retType);
			}
				break;

			case "friend_add": {
				log.info("---------------DODAVANJE PRIJATELJA---------------");
				log.info(msgContent);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String sender = data.getString("sender");
				String receiver = data.getString("receiver");
				Context ctx = new InitialContext();
				UserServiceLocal service = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				JSONObject response = new JSONObject();

				if (service.sendFriendRequest(sender, receiver)) {
					response.put("status", "success");
					response.put("info", "Uspesno dodan zahtev za prijateljstvo.");
				} else {
					response.put("status", "fail");
					response.put("info", "Doslo je do greske.");
				}

				response.put("sender", sender);
				response.put("receiver", receiver);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "friend_add");
			}
				break;

			case "friend_remove": {
				log.info("---------------BRISANJE PRIJATELJA---------------");
				log.info(msgContent);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String sender = data.getString("sender");
				String receiver = data.getString("receiver");
				Context ctx = new InitialContext();
				UserServiceLocal service = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				JSONObject response = new JSONObject();

				if (service.removeFriend(sender, receiver)) {
					response.put("status", "success");
					response.put("info", "Korisnik je obrisan iz liste prijatelja.");

				} else {
					response.put("status", "fail");
					response.put("info", "Doslo je do greske.");
				}

				response.put("sender", sender);
				response.put("receiver", receiver);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "friend_remove");
			}
				break;

			case "friend_accept": {
				log.info("---------------PRIHVATANJE ZAHTEVA---------------");
				log.info(msgContent);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String sender = data.getString("sender");
				String receiver = data.getString("receiver");
				Context ctx = new InitialContext();
				UserServiceLocal service = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				JSONObject response = new JSONObject();

				if (service.acceptFriendRequest(sender, receiver)) {
					response.put("status", "success");
					response.put("info", "Zahtev je prihvacen.");
				} else {
					response.put("status", "fail");
					response.put("info", "Doslo je do greske.");
				}

				response.put("sender", sender);
				response.put("receiver", receiver);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "friend_accept");
			}
				break;

			case "friend_reject": {
				log.info("---------------ODBIJANJE ZAHTEVA---------------");
				log.info(msgContent);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String sender = data.getString("sender");
				String receiver = data.getString("receiver");
				Context ctx = new InitialContext();
				UserServiceLocal service = (UserServiceLocal) ctx.lookup(LOOKUP + USER_SERVICE);
				JSONObject response = new JSONObject();

				if (service.rejectFriendRequest(sender, receiver)) {
					response.put("status", "success");
					response.put("info", "Zahtev je uspesno odbijen.");
				} else {
					response.put("status", "fail");
					response.put("info", "Doslo je do greske.");
				}

				response.put("sender", sender);
				response.put("receiver", receiver);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "friend_reject");
			}
				break;

			case "group_new": {
				log.info("---------------KREIRANJE GRUPE---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				GroupServiceLocal gsl = (GroupServiceLocal) ctx.lookup(LOOKUP + GROUP_SERVICE);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String name = data.getString("name");
				String admin = data.getString("admin");
				ArrayList<String> korisnici = new ArrayList<>();
				korisnici.add(admin);
				Group g = new Group(name, admin, korisnici);
				Group nova_grupa = gsl.createGroup(new Gson().toJson(g));

				JSONObject response = new JSONObject();

				if (nova_grupa != null) {
					response.put("status", "success");
					response.put("info", "Grupa je uspesno kreirana.");
				} else {
					response.put("status", "fail");
					response.put("info", "Doslo je do greske prilikom kreiranja grupe.");
				}

				response.put("data", nova_grupa);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "group_new");
			}
				break;

			case "group_delete": {
				log.info("---------------BRISANJE GRUPE---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				GroupServiceLocal gsl = (GroupServiceLocal) ctx.lookup(LOOKUP + GROUP_SERVICE);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String id = data.getString("id");

				JSONObject response = new JSONObject();
				List<String> users = gsl.deleteGroup(id);
				response.put("users", users);
				if (users != null) {
					response.put("status", "success");
					response.put("info", "Grupa je uspesno obrisana.");
				} else {
					response.put("status", "fail");
					response.put("info", "Doslo je do greske prilikom brisanja grupe.");
				}

				// response.put("data", nova_grupa);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "group_delete");
			}
				break;

			case "group_add_user": {
				log.info("---------------DODAVANJE KORISNIKA U GRUPU---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				GroupServiceLocal gsl = (GroupServiceLocal) ctx.lookup(LOOKUP + GROUP_SERVICE);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String id = data.getString("id");
				String username = data.getString("username");

				JSONObject response = new JSONObject();
				if (gsl.addMember(id, username)) {
					response.put("status", "success");
					response.put("info", "Korisnik je uspesno dodan u grupu.");
				} else {
					response.put("status", "fail");
					response.put("info", "Korisnik nije dodan u grupu.");
				}

				// response.put("data", nova_grupa);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "group_add_user");
			}
				break;

			case "group_remove_user": {
				log.info("---------------BRISANJE KORISNIKA IZ GRUPU---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				GroupServiceLocal gsl = (GroupServiceLocal) ctx.lookup(LOOKUP + GROUP_SERVICE);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String id = data.getString("id");
				String username = data.getString("username");

				JSONObject response = new JSONObject();
				if (gsl.removeMember(id, username)) {
					response.put("status", "success");
					response.put("info", "Korisnik je uspesno obrisan iz grupe.");
				} else {
					response.put("status", "fail");
					response.put("info", "Korisnik nije obrisan iz grupe.");
				}

				// response.put("data", nova_grupa);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				msgSender.sendMsg(response.toString(), "group_remove_user");
			}
				break;

			case "logout": {
				log.info("---------------ODJAVA---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				UserAppNodeLocal node = (UserAppNodeLocal) ctx.lookup(USER_APP_NODE);
				JSONObject requestMsg = new JSONObject(msgContent);
				String username = requestMsg.getString("username");
				String host = requestMsg.getString("host");

				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);
				JSONObject response = new JSONObject();

				response.put("host", host);
				response.put("type", "logout");
				response.put("username", username);
				if (node.removeUser(username)) {
					log.info("Odjava uspesna");
					response.put("status", "success");
					response.put("info", "Uspesno ste se odjavili.");
				} else {
					log.info("Odjava nije uspela.");
					response.put("status", "fail");
					response.put("info", "Odjava nije uspela.");
				}
				msgSender.sendMsg(response.toString(), "logout");
			}
				break;

			case "show_messages": {
				log.info("---------------PRIKAZIVANJE PORUKA---------------");
				log.info(msgContent);

				Context ctx = new InitialContext();
				JSONObject requestMsg = new JSONObject(msgContent);
				String username = requestMsg.getString("username");
				String host = requestMsg.getString("host");
				MessageServiceLocal msl = (MessageServiceLocal) ctx.lookup(LOOKUP + MESSAGE_SERVICE);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);

				JSONObject response = new JSONObject();
				response.put("type", "show_messages");
				response.put("host", host);
				List<model.Message> poruke = msl.getRelatedMessages(username);
				response.put("data", new JSONObject(poruke));
				msgSender.sendMsg(response.toString(), "show_messages");
			}
				break;

			case "send_message": {
				log.info("---------------SLANJE PORUKE---------------");
				log.info(msgContent);

				JSONObject msgObj = new JSONObject(msgContent);
				JSONObject data = msgObj.getJSONObject("data");
				String host = msgObj.getString("host");

				Context ctx = new InitialContext();
				MessageServiceLocal msl = (MessageServiceLocal) ctx.lookup(LOOKUP + MESSAGE_SERVICE);
				UserMsgSender msgSender = (UserMsgSender) ctx.lookup(SENDER_BEAN);

				JSONObject response = new JSONObject();
				response.put("type", "send_message");
				if (msl.createMessage(data.toString())) {
					log.info("Slanje poruke uspesno.");
					response.put("status", "success");
					response.put("info", "Slanje poruke uspesno.");
				} else {
					log.info("Slanje poruke nije uspelo.");
					response.put("status", "fail");
					response.put("info", "Slanje poruke nije uspelo.");
				}
				response.put("host", host);
				msgSender.sendMsg(response.toString(), "send_message");

			}
				break;

			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		log.info("UserMsgReceiver -> kraj onMessage metode");
	}

}
