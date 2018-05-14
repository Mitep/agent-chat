package jms;

import javax.ejb.Remote;

@Remote
public interface ChatMsgSender {

	public void sendMsg(String msgContent, String type);
}
