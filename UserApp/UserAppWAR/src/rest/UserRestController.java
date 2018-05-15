package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean createUser(User u) {
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
	@Path("/delete/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteUser(@PathParam("name") String name) {
		return userService.deleteUser(name);
	}

}
