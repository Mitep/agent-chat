package util;

public class LookupConst {

	// za jar
	public static final String CHAT_APP_NODE_LOCAL = "java:module/ChatAppNode!node.ChatAppNodeLocal";
	public static final String CHAT_JMS_SENDER = "java:module/ChatMsgSender!jms.ChatMsgSenderLocal";
	public static final String CHAT_WEB_SOCKET = "java:module/WSBean!websocket.WSLocal";
	public static final String CHAT_REGISTER_SERVICE = "java:module/RegisterService!service.interfaces.RegisterServiceLocal";
	public static final String CHAT_LOGIN_SERVICE = "java:module/LoginService!service.interfaces.LoginServiceLocal";
	
	// za war
	public static final String CHAT_WEB_SOCKET_WAR = "java:app/ChatAppJAR/WSBean!websocket.WSLocal";

}
