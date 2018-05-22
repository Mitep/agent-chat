package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.json.JSONString;

import com.google.gson.Gson;

import dtos.JmsDTO;
import dtos.UserSearchDTO;
import jms.UserMsgReceiver;
import jms.UserMsgSender;
import model.User;
import node.UserAppNodeLocal;
import service.interfaces.LoginServiceLocal;
import service.interfaces.UserServiceLocal;

/**
 * @author Nikola
 *
 */
@Path("/user")
public class UserRestController {

	@EJB
	private LoginServiceLocal loginService;

	@EJB
	private UserServiceLocal userService;

	@GET
	@Path("/test")
	public String test() {
		return "user test ok!";
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByUsername(@PathParam("username") String username) {
		return userService.getUserByUsername(username);
	}

	@GET
	@Path("/name/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsersByName(@PathParam("name") String name) {
		return userService.getUserByName(name);
	}

	@GET
	@Path("/surname/{surname}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsersBySurname(@PathParam("surname") String surname) {
		return userService.getUserBySurname(surname);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User createUser(User u) {
		return userService.createUser(new Gson().toJson(u));
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> readAllUsers() {
		return userService.readAll();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateUser(User u) {
		return userService.updateUser(u);
	}

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUser(@FormParam("name") String name) {
		return userService.deleteUser(name);
	}

	@POST
	@Path("/addgroup")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<ObjectId> addUserToGroup(@FormParam("username") String username, @FormParam("groupId") String groupId) {
		return userService.addGroup(username, groupId);
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> search(UserSearchDTO dto) {
		return userService.findUsers(dto.getUsername(), dto.getName(), dto.getSurname());
	}

	@POST
	@Path("/login")
	//@Consumes(MediaType.)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject login(String js) {
//		try {
			JSONObject obj = new JSONObject(js);
			//Context context = new InitialContext();
			String retType = "login";
			String host = obj.getString("host");
			String username = obj.getString("username");
			String password = obj.getString("password");
			JmsDTO dto = new JmsDTO();
			JSONObject response = new JSONObject();
			if (loginService.validUser(username, password)) {
				response.put("type", "login");
				response.put("info", "Uspesno ste se prijavili!");
				response.put("status", "success");
				
				dto = new JmsDTO("login", username, "success", "Uspesno ste se prijavili!");
				
				//UserAppNodeLocal node = (UserAppNodeLocal) context.lookup(UserMsgReceiver.USER_SERVICE_LOCAL);
//				node.addUser(username, host);
				System.out.println("Prijava uspesna.");
			} else {
				response.put("type", "login");
				response.put("info", "Neuspesna prijava.");
				response.put("status", "fail");
				
				dto = new JmsDTO("login", username, "fail", "Neuspesna prijava.");
				
				System.out.println("Prijava neuspesna.");
			}

//			UserMsgSender msgSender = (UserMsgSender) context.lookup(UserMsgReceiver.SENDER_BEAN);
			//retMsg = new Gson().toJson(dto);
//			msgSender.sendMsg(response.toString(), retType);
			return response;
//		} catch (NamingException e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addFriendRequest(@FormParam("user") String user, @FormParam("friend") String friend) {
		return userService.sendFriendRequest(user, friend);
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean acceptFriend(@FormParam("user") String user, @FormParam("friend") String friend) {
		return userService.acceptFriendRequest(user, friend);
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean rejectFriend(@FormParam("user") String user, @FormParam("friend") String friend) {
		return userService.rejectFriendRequest(user, friend);
	}

	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User logout(User u) {

//		Context ctx = new InitialContext();
//		UserAppNodeLocal node = (UserAppNodeLocal) ctx.lookup(USER_SERVICE_LOCAL);
//
//		JSONObject requestMsg = new JSONObject(msgContent);
//		String username = requestMsg.getString("username");
//		String host = requestMsg.getString("host");
//
//		UserMsgSender msgSender = (UserMsgSender) ctx.lookup(UserMsgReceiver.SENDER_BEAN);
//		JSONObject response = new JSONObject();
//
//		response.put("host", host);
//		response.put("type", "logout");
//		response.put("username", username);
//		if (node.removeUser(username)) {
//			response.put("status", "success");
//			response.put("info", "Uspesno ste se odjavili.");
//		} else {
//			response.put("status", "fail");
//			response.put("info", "Odjava nije uspela.");
//		}
//		msgSender.sendMsg(response.toString(), "logout");

		String retMsg;
		String retType = "logout";
		JmsDTO dto = new JmsDTO();
		if (loginService.validUser(u.getUsername(), u.getSurname())) {
			dto = new JmsDTO("login", u.getUsername(), "success", "Uspesno ste se prijavili!");
			System.out.println("Prijava uspesna.");
		} else {
			dto = new JmsDTO("login", u.getUsername(), "fail", "Neuspesna prijava.");
			System.out.println("Prijava neuspesna.");
		}

		try {
			Context context = new InitialContext();
			UserMsgSender msgSender = (UserMsgSender) context.lookup(UserMsgReceiver.SENDER_BEAN);
			retMsg = new Gson().toJson(dto);
			msgSender.sendMsg(retMsg, retType);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
		return u;
	}

}
