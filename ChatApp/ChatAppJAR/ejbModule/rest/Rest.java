package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
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
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		// drugi url
//        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/message/all");
//        Response response = target.request().get();
//        System.out.println(response.readEntity(String.class));
	}

	@Override
	public ArrayList<String> groupUsers(String groupId) throws Exception {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/group/members/" + groupId);
        Response response = target.request().get();
        ArrayList<String> ret = (ArrayList<String>) response.getEntity();
		return ret;
	}
	
}
