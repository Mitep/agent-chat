package rest;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

@Stateless
public class Rest implements RestLocal {
	
	
	@Override
	public String showUserMessages(String content) {
		ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/all");
        Response response = target.request().get();
        return response.readEntity(String.class);
	}

	@Override
	public String showUserGroupMessages(String content) throws Exception {
//		ResteasyClient client = new ResteasyClientBuilder().build();
//        // drugi url
//		ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/all");
//        Response response = target.request().get();
//        return response.readEntity(String.class);
		return null;
	}

	@Override
	public void saveMsg(String content) throws Exception {
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		// drugi url
//        ResteasyWebTarget target = client.target("http://localhost:8080/UserAppWAR/userapp/all");
//        Response response = target.request().get();
//        System.out.println(response.readEntity(String.class));
	}

	@Override
	public ArrayList<String> groupUsers(String groupId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
