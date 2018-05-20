package websocket;

import java.io.IOException;

import javax.ejb.Local;
import javax.naming.NamingException;
import javax.websocket.Session;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Local
public interface WSLocal {

	public void sendMsg(String user, String content) throws NamingException;
	
	public void onOpen(Session session);
	
	public void echoTextMessage(Session session, String msg, boolean last) throws NamingException, JsonParseException, JsonMappingException, IOException, Exception;
	
	public void close(Session session);
	
	public void error(Session session, Throwable t);

}

