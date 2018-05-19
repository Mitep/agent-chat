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

import com.google.gson.Gson;

import dtos.JmsDTO;
import dtos.UserSearchDTO;
import jms.UserMsgReceiver;
import jms.UserMsgSender;
import model.User;
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(User u) {
		String retMsg;
		String retType = "login";
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
