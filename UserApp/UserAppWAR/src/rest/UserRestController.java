package rest;

import java.util.List;

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

import com.google.gson.Gson;

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

	private Context ctx;

	public UserRestController() throws NamingException {
		ctx = new InitialContext();
	}

	@GET
	@Path("/test")
	public String test() {
		return "user test ok!";
	}

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByUsername(@PathParam("username") String username) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.getUserByUsername(username);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/name/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsersByName(@PathParam("name") String name) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.getUserByName(name);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/surname/{surname}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsersBySurname(@PathParam("surname") String surname) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.getUserBySurname(surname);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User createUser(User u) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.createUser(new Gson().toJson(u));
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> readAllUsers() {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.readAll();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateUser(User u) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.updateUser(u);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUser(@FormParam("name") String name) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.deleteUser(name);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/addgroup")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<ObjectId> addUserToGroup(@FormParam("username") String username, @FormParam("groupId") String groupId) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.addGroup(username, groupId);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> search(UserSearchDTO dto) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.findUsers(dto.getUsername(), dto.getName(), dto.getSurname());
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String js) {
		try {
			JSONObject obj = new JSONObject(js);
			LoginServiceLocal loginService = (LoginServiceLocal) ctx.lookup(LoginServiceLocal.LOOKUP_GLOBAL);

			String retType = "login";
			String host = obj.getString("host");
			String username = obj.getString("username");
			String password = obj.getString("password");
			JSONObject response = new JSONObject();
			if (loginService.validUser(username, password)) {
				response.put("type", "login");
				response.put("info", "Uspesno ste se prijavili!");
				response.put("status", "success");

				UserAppNodeLocal node = (UserAppNodeLocal) ctx.lookup(UserAppNodeLocal.LOOKUP_GLOBAL);
				node.addUser(username, host);
				System.out.println("Prijava uspesna.");
			} else {
				response.put("type", "login");

				response.put("info", "Neuspesna prijava.");
				response.put("status", "fail");

				System.out.println("Prijava neuspesna.");
			}
			response.put("username", username);
			response.put("host", host);
			UserMsgSender msgSender = (UserMsgSender) ctx.lookup(UserMsgReceiver.SENDER_GLOBAL);
			msgSender.sendMsg(response.toString(), retType);
			return response.toString();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addFriendRequest(@FormParam("user") String user, @FormParam("friend") String friend) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.sendFriendRequest(user, friend);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean acceptFriend(@FormParam("user") String user, @FormParam("friend") String friend) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.acceptFriendRequest(user, friend);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean rejectFriend(@FormParam("user") String user, @FormParam("friend") String friend) {
		UserServiceLocal userService;
		try {
			userService = (UserServiceLocal) ctx.lookup(UserServiceLocal.LOOKUP_GLOBAL);
			return userService.rejectFriendRequest(user, friend);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(String js) {

		try {
			UserAppNodeLocal node = (UserAppNodeLocal) ctx.lookup(UserAppNodeLocal.LOOKUP_GLOBAL);

			JSONObject requestMsg = new JSONObject(js);
			String username = requestMsg.getString("username");
			String host = requestMsg.getString("host");
			JSONObject response = new JSONObject();
			response.put("host", host);
			response.put("type", "logout");
			response.put("username", username);
			if (node.removeUser(username)) {
				System.out.println("Odjava uspesna");
				response.put("status", "success");
				response.put("info", "Uspesno ste se odjavili.");
			} else {
				System.out.println("Odjava neuspesna");
				response.put("status", "fail");
				response.put("info", "Odjava nije uspela.");
			}
			UserMsgSender msgSender = (UserMsgSender) ctx.lookup(UserMsgReceiver.SENDER_GLOBAL);
			msgSender.sendMsg(response.toString(), "logout");
			return response.toString();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
