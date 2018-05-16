package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.Session;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.LoginRequest;
import dto.RegisterRequest;
import jms.ChatMsgSender;

@Startup
@Singleton
public class WSBean implements WSLocal{
	
	Logger log = Logger.getLogger("Websockets endpoint");

	static List<Session> sessions = new ArrayList<Session>();
	
	//key - username
	private HashMap<String, Session> activeUsers;
	
	private HashMap<String, Session> loginAttempt;
	private HashMap<String, Session> registerAttempt;
	
	public WSBean(){
		activeUsers = new HashMap<String, Session>();
		loginAttempt = new HashMap<String, Session>();
		registerAttempt = new HashMap<String, Session>();
	}
	@Override
	public void sendMsg(String user, String content) {
		// TODO Auto-generated method stub
		//activeUsers.get(user).getAsyncRemote().sendText(content);
		System.out.println(content);
		Session s = activeUsers.get(user);
		s.getAsyncRemote().sendText(content);
		
	}

	@Override
	public void onOpen(Session session) {
		// TODO Auto-generated method stub
		if (!sessions.contains(session)) {
			sessions.add(session);
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: " + sessions.size());
		}
	}

	@Override
	public void echoTextMessage(Session session, String msg, boolean last) {
		// TODO Auto-generated method stub
		try {
			if (session.isOpen()) {
				
				JSONObject obj = new JSONObject(msg);
				String type = (String)obj.get("type");
				String data = obj.getJSONObject("data").toString();

				ObjectMapper mapper = new ObjectMapper();

				if(type.equals("register")){	
					log.info(type);
					log.info(data);
					RegisterRequest rr = mapper.readValue(data, RegisterRequest.class);
			
//					log.info("--------------**************--------------");
//					log.info("timestamp: " + rr.getTimestamp());
//					
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTimeInMillis(rr.getTimestamp());
//
//					int year = calendar.get(Calendar.YEAR);
//					int month = calendar.get(Calendar.MONTH) + 1;
//					int day = calendar.get(Calendar.DAY_OF_MONTH);
//					int hour = calendar.get(Calendar.HOUR_OF_DAY);
//					int minutes = calendar.get(Calendar.MINUTE);
//					int seconds = calendar.get(Calendar.SECOND);
//					
//					log.info(day + "." + month + "." + year + " " + hour + ":" + minutes + ":" + seconds);
//					log.info("username: " + rr.getUsername());
//					log.info("first name: " + rr.getFirstName());
//					log.info("last name: " + rr.getLastName());
//					log.info("password: " + rr.getPassword());
//					log.info("--------------**************--------------");
					activeUsers.put(rr.getUsername(), session);
					Context context = new InitialContext();
					//java:app[/module name]/enterprise bean name[/interface name]						
					ChatMsgSender msgSender = (ChatMsgSender) context.lookup("java:module/ChatMsgSenderBean!jms.ChatMsgSender");				
					msgSender.sendMsg(data, "register");
					
				}else if(type.equals("login")){
					log.info(type);
					log.info(data);				
					LoginRequest lr = mapper.readValue(data, LoginRequest.class);
					
//					log.info("--------------**************--------------");
//					log.info("username: " + lr.getUsername());
//					log.info("password: " + lr.getPassword());
//					log.info("--------------**************--------------");
					
					loginAttempt.put(lr.getUsername(), session);
					Context context = new InitialContext();
					//java:app[/module name]/enterprise bean name[/interface name]				
					ChatMsgSender msgSender = (ChatMsgSender) context.lookup("java:app/ChatAppJAR/ChatMsgSenderBean!jms.ChatMsgSender");					
					msgSender.sendMsg(data, "login");
				}							
			
//			 	log.info("Websocket endpoint: " + this.hashCode() + " primio: " + msg + " u sesiji: " + session.getId());
//			 	log.info(msg);
//			 
				
//		        for (Session s : sessions) {	        	
//		        	s.getBasicRemote().sendText(msg, last);     				       
//		        }
			}
		} catch (IOException e) {
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void close(Session session) {
		// TODO Auto-generated method stub
		sessions.remove(session);
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());
	}

	@Override
	public void error(Session session, Throwable t) {
		// TODO Auto-generated method stub
		sessions.remove(session);
		log.log(Level.SEVERE, "Greska u sesiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: " + t.getMessage());
		t.printStackTrace();
	}

}
