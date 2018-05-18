package rest;

import java.util.List;

import javax.ejb.EJB;
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

import model.User;
import service.interfaces.UserServiceLocal;

/**
 * @author Nikola
 *
 */
@Path("/user")
public class UserRestController {

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

	@GET
	@Path("/search/{username}/{name}/{surname}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> search(@PathParam("username") String username, @PathParam("name") String name,
			@PathParam("surname") String surname) {
		System.out.println(username);
		System.out.println(name);
		System.out.println(surname);
		return userService.findUsers(username, name, surname);
	}

}
