package node;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ChatAppNodeBean implements ChatAppNode {

	// key - hostname, value - ip_addr
	private HashMap<String, String> activeNodes;

	private ArrayList<String> activeUsers;
	
	public ChatAppNodeBean() {
		//activeNodes = new HashMap<String, String>();
		
	}
	@PostConstruct
	public void initNode() {
		//if(!getMasterIp().equals(getMyIp())) {
			//salji zahteve i kaci se na nodove
		//}
	}
	
	public String getMasterIp(){
		//vraca ip adresu master nodea iz configuracionog fajla
	
		return null;
	}
	
	public String getMyIp(){
		//vraca adresu na kojoj se nalazi aplikacija
		
		return null;
	}
	
	
}
