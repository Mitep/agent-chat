package jms;

import javax.ejb.Remote;

@Remote
public interface UserMsgSender {

	public void sendMsg(String msgContent);
}
