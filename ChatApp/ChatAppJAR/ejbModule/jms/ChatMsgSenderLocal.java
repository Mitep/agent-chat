package jms;

import javax.ejb.Local;

@Local
public interface ChatMsgSenderLocal {

	public void sendMsg(String msgContent, String type) throws Exception;
}
