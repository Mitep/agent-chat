package rest;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

@Stateless
public class Rest implements RestLocal {
	
	@Override
	public String showUserMessages(String username) {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/user/messages/" + username);
        Response response = target.request().get();
        
        return response.readEntity(String.class);
    }

	@Override
	public void saveMsg(String content) throws Exception {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/message");
        target.request().post(Entity.entity(content, MediaType.APPLICATION_JSON));
  	}

	@Override
	public ArrayList<String> groupUsers(String groupId) throws Exception {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/group/members/" + groupId);
        Response response = target.request().get();
        ArrayList<String> ret = (ArrayList<String>) response.getEntity();
		return ret;
	}

	@Override
	public String registerNode(String ipAddress) {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/ChatAppWAR/chatapp/node/register/" + ipAddress);
        Response response = target.request().get();
        return response.readEntity(String.class);
	}

	@Override
	public void deregisterNode(String alias) {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/ChatAppWAR/chatapp/node/deregister/" + alias);
        Response response = target.request().get();
	}

	@Override
	public HashMap<String, String> getOnlineUsers() {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/ChatAppWAR/chatapp/node/onlineUsers");
        Response response = target.request().get();
		HashMap<String, String> ret = (HashMap<String, String>) response.getEntity();
		return ret;
	}

	@Override
	public HashMap<String, String> getOnlineNodes() {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/ChatAppWAR/chatapp/node/onlineNodes");
        Response response = target.request().get();
		HashMap<String, String> ret = (HashMap<String, String>) response.getEntity();
		return ret;
	}
	
}
