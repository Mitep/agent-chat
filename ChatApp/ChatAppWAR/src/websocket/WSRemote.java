package websocket;

import javax.ejb.Local;
import javax.ejb.Remote;

@Remote
public interface WSRemote {

	public void sendMsg(String user, String content);
}
