package websocket;

import javax.ejb.Local;
import javax.websocket.Session;

@Local
public interface WSLocal {

	public void sendMsg(String user, String content);
	public void onOpen(Session session);
	public void echoTextMessage(Session session, String msg, boolean last);
	public void close(Session session);
	public void error(Session session, Throwable t);
}
