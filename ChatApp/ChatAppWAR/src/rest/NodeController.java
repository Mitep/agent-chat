package rest;

import java.util.HashMap;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import service.interfaces.NodeServiceLocal;
import util.LookupConst;

@Path("/node")
@Stateless
public class NodeController {

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "chat app rest working";
	}
	
	@GET
	@Path("/register/{ip}")
	@Produces(MediaType.APPLICATION_JSON)
	public String registerNode(@PathParam("ip") String ip) throws Exception {
		Context context = new InitialContext();
		NodeServiceLocal nsl = (NodeServiceLocal) context.lookup(LookupConst.CHAT_APP_SERVICE_WAR); 
		return nsl.registerNode(ip);
	}
	
	@GET
	@Path("/deregister/{ip}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deregisterNode(@PathParam("alias") String alias) throws Exception {
		Context context = new InitialContext();
		NodeServiceLocal nsl = (NodeServiceLocal) context.lookup(LookupConst.CHAT_APP_SERVICE_WAR); 
		nsl.deregisterNode(alias);
	}
	
	@GET
	@Path("/onlineUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, String> getOnlineUsers() throws Exception {
		Context context = new InitialContext();
		NodeServiceLocal nsl = (NodeServiceLocal) context.lookup(LookupConst.CHAT_APP_SERVICE_WAR); 
		return nsl.getOnlineUsers();
	}
	
	@GET
	@Path("/onlineNodes")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, String> getNodes() throws Exception {
		Context context = new InitialContext();
		NodeServiceLocal nsl = (NodeServiceLocal) context.lookup(LookupConst.CHAT_APP_SERVICE_WAR); 
		return nsl.getOnlineNodes();
	}
	
	
}
