package websocket;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import util.LookupConst;

@ServerEndpoint("/websocket/echo")
public class WS {

	@OnOpen
	public void onOpen(Session session) throws NamingException {
		Context context = new InitialContext();
		WSLocal ws = (WSLocal) context.lookup(LookupConst.CHAT_WEB_SOCKET_WAR);
		ws.onOpen(session);
	}
	
	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last) throws NamingException, JsonParseException, JsonMappingException, IOException {		
		Context context = new InitialContext();
		WSLocal ws = (WSLocal) context.lookup(LookupConst.CHAT_WEB_SOCKET_WAR);
		ws.echoTextMessage(session, msg, last);
	}

	@OnClose
	public void close(Session session) throws NamingException {
		Context context = new InitialContext();
		WSLocal ws = (WSLocal) context.lookup(LookupConst.CHAT_WEB_SOCKET_WAR);
		ws.close(session);
	}
	
	@OnError
	public void error(Session session, Throwable t) throws NamingException {
		Context context = new InitialContext();
		WSLocal ws = (WSLocal) context.lookup(LookupConst.CHAT_WEB_SOCKET_WAR);
		ws.error(session, t);
	}
	
}