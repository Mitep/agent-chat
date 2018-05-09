package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import dto.LoginRequest;
import dto.RegisterRequest;


@ServerEndpoint("/websocket/echo")
public class WS {

	Logger log = Logger.getLogger("Websockets endpoint");

	static List<Session> sessions = new ArrayList<Session>();
	
	public WS(){
		
	}
	
	@OnOpen
	public void onOpen(Session session) {
		if (!sessions.contains(session)) {
			sessions.add(session);
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: " + sessions.size());
		}
	}
	
	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) throws JsonParseException, JsonMappingException, IOException {		
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
					log.info("--------------**************--------------");
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
					log.info("username: " + rr.getUsername());
					log.info("first name: " + rr.getFirstName());
					log.info("last name: " + rr.getLastName());
					log.info("password: " + rr.getPassword());
					log.info("--------------**************--------------");
				} else if(type.equals("login")){
					log.info(type);
					log.info(data);				
					LoginRequest lr = mapper.readValue(data, LoginRequest.class);
					log.info("--------------**************--------------");
					log.info("username: " + lr.getUsername());
					log.info("password: " + lr.getPassword());
					log.info("--------------**************--------------");
				}				
				
			
//			 	log.info("Websocket endpoint: " + this.hashCode() + " primio: " + msg + " u sesiji: " + session.getId());
//			 	log.info(msg);
//			 
				
		        for (Session s : sessions) {	        	
		        	s.getBasicRemote().sendText(msg, last);     				       
		        }
			}
		} catch (IOException e) {
			try {
				session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		log.log(Level.SEVERE, "Greska u sesiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: " + t.getMessage());
		t.printStackTrace();
	}
}
