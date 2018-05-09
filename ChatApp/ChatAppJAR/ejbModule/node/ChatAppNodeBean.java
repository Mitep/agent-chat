package node;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ChatAppNodeBean implements ChatAppNode {

	private HashMap<String, String> activeNodes;

	private ArrayList<String> activeUsers;
	
	public ChatAppNodeBean() throws SocketException {
		//proverimo da li je node master
		//ukoliko jeste
		if(isMasterNode()){
			
		} else {
		//ukoliko nije
			
		}
	}
	
	private boolean isMasterNode() throws SocketException{
		String addr = getIpAddr();
		
		return false;
	}
	
	private String getIpAddr() throws SocketException{
		try {
			InetAddress addr = InetAddress.getLocalHost();
			System.out.println(addr.toString());
			System.out.println(addr.getHostName());
			System.out.println(addr.getAddress().toString());
			System.out.println(addr.getHostAddress());
			System.out.println(addr);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	private String readMasterAddr(){
		//cita ip adresu master nodea iz konfiguracioniog fajla
		
		return null;
	}
	
	
	
}
