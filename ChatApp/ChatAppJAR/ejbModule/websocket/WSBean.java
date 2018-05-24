package websocket;

import java.io.IOException;
import java.util.ArrayList;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import node.ChatAppNodeLocal;
import service.interfaces.LoginServiceLocal;
import service.interfaces.LogoutServiceLocal;
import service.interfaces.MessageServiceLocal;
import service.interfaces.RegisterServiceLocal;
import service.interfaces.UserSearchServiceLocal;
import util.LookupConst;

@Startup
@Singleton
public class WSBean implements WSLocal {

	Logger log = Logger.getLogger("Websockets endpoint");

	static List<Session> sessions = new ArrayList<Session>();
	
	Context context;
	
	public WSBean() throws NamingException {
		context = new InitialContext();
	}
	
	@Override
	public void sendMsg(String user, String content) throws NamingException {
		ChatAppNodeLocal node = (ChatAppNodeLocal) context.lookup(LookupConst.CHAT_APP_NODE_LOCAL);
		node.getUserSession(user).getAsyncRemote().sendText(content);
	}

	@Override
	public void onOpen(Session session) {
		if (!sessions.contains(session)) {
			sessions.add(session);
			log.info("Dodao sesiju: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", ukupno sesija: " + sessions.size());
		}
	}

	@Override
	public void echoTextMessage(Session session, String msg, boolean last) throws NamingException, JsonParseException, JsonMappingException, IOException, Exception {
		if(session.isOpen()){
			JSONObject obj = new JSONObject(msg);
			String type = obj.getString("type");
			String data = obj.getJSONObject("data").toString();
			
			switch (type) {
	            case "register": {
					RegisterServiceLocal rsl = (RegisterServiceLocal) context.lookup(LookupConst.CHAT_REGISTER_SERVICE);
					rsl.registerUser(data, session);
	            };
	            	break;
	            case "login": {
	            	LoginServiceLocal lsl = (LoginServiceLocal) context.lookup(LookupConst.CHAT_LOGIN_SERVICE);
					lsl.loginUser(data, session);
	            };
	            	break;
	            case "logout": {
	            	LogoutServiceLocal lsl = (LogoutServiceLocal) context.lookup(LookupConst.CHAT_LOGOUT_SERVICE);
	            	lsl.logoutUser(data);
	            };
	            	break;
	            case "user_search": {
	            	UserSearchServiceLocal ussl = (UserSearchServiceLocal) context.lookup(LookupConst.USER_SEARCH_SERVICE);
	            	ussl.searchUser(data, session);
	            };
	            	break;
				case "send_message": {
					MessageServiceLocal msl = (MessageServiceLocal) context.lookup(LookupConst.MESSAGE_SERVICE);
					msl.processMessage(data);
				};
	            	break;
				case "send_group_message": {
					MessageServiceLocal msl = (MessageServiceLocal) context.lookup(LookupConst.MESSAGE_SERVICE);
					msl.processGroupMessage(data);
				};
	            	break;
	            default:
	            	break;
			}				
			
		}
		
	}

	@Override
	public void close(Session session) {
		// logout servis treba ovde da se doda
		LogoutServiceLocal lsl;
		try {
			lsl = (LogoutServiceLocal) context.lookup(LookupConst.CHAT_LOGOUT_SERVICE);
			lsl.autoLogoutUser(session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sessions.remove(session);
		log.info("Zatvorio: " + session.getId() + " u endpoint-u: " + this.hashCode());
	}

	@Override
	public void error(Session session, Throwable t) {
		sessions.remove(session);
		log.log(Level.SEVERE, "Greska u sesiji: " + session.getId() + " u endpoint-u: " + this.hashCode() + ", tekst: " + t.getMessage());
		t.printStackTrace();
	}

}
