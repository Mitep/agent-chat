package util;

public class LookupConst {

	// za jar
	public static final String CHAT_APP_NODE_LOCAL = "java:module/ChatAppNode!node.ChatAppNodeLocal";
	public static final String CHAT_APP_NODE_SERVICE = "java:module/NodeService!service.interfaces.NodeServiceLocal";
	
	public static final String CHAT_JMS_SENDER = "java:module/ChatMsgSender!jms.ChatMsgSenderLocal";
	
	public static final String CHAT_WEB_SOCKET = "java:module/WSBean!websocket.WSLocal";
	
	public static final String CHAT_REGISTER_SERVICE = "java:module/RegisterService!service.interfaces.RegisterServiceLocal";
	
	public static final String CHAT_LOGIN_SERVICE = "java:module/LoginService!service.interfaces.LoginServiceLocal";
	public static final String CHAT_LOGOUT_SERVICE = "java:module/LogoutService!service.interfaces.LogoutServiceLocal";
	
	public static final String USER_SEARCH_SERVICE = "java:module/UserSearchService!service.interfaces.UserSearchServiceLocal";
	
	public static final String MESSAGE_SERVICE = "java:module/MessageService!service.interfaces.MessageServiceLocal";
	
	public static final String REST = "java:module/Rest!rest.RestLocal";
	
	// za war
	public static final String CHAT_WEB_SOCKET_WAR = "java:app/ChatAppJAR/WSBean!websocket.WSLocal";
	
	public static final String CHAT_APP_SERVICE_WAR = "java:app/ChatAppJAR/NodeService!service.interfaces.NodeServiceLocal";
	
	

}
