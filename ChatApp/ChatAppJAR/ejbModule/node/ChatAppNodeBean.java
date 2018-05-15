package node;

import javax.websocket.Session;

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

	// key - hostname, value - ip_addr:port
	private HashMap<String, String> activeNodes;
	
	private String hostname;
	
	public String getHostname() {
		return hostname;
	}

	private void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public ChatAppNodeBean() {
		activeNodes = new HashMap<String, String>();
	}
	
	@PostConstruct
	public void initNode() {
		if(amIMaster()) {
			activeNodes.put("master", getMyIp());
			setHostname("master");
		} else {
			//salji zahteve i kaci se na nodove
			
		}
	}
	
	private String getMasterIp(){
		//vraca ip adresu master nodea iz configuracionog fajla
		return "localhost:8080";
	}
	
	private String getMyIp(){
		//vraca adresu na kojoj se nalazi aplikacija
		return "localhost:8080";
	}
	
	public boolean amIMaster(){
		return getMasterIp().equals(getMyIp());
	}
	
}
