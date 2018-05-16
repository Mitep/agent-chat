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

import dtos.FriendshipDTO;
import model.Friendship;
import service.interfaces.FriendshipServiceLocal;

/**
 * @author Nikola
 *
 */
@Path("/friendship")
public class FriendshipRestController {

	@EJB
	private FriendshipServiceLocal frendService;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Friendship getFriendship(@PathParam("id") String id) {
		return frendService.getFriendship(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean createFriendship(FriendshipDTO dto) {
		Friendship f = new Friendship(dto.getUserId(), dto.getUserId2(), dto.getStatus());
		return frendService.createFriendship(new Gson().toJson(f));
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Friendship> readAllFriendships() {
		return frendService.readAll();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateFriendship(FriendshipDTO g) {
		Friendship f = new Friendship(g.getId(), g.getUserId(), g.getUserId2(), g.getStatus());
		return frendService.updateFriendship(f);
	}

	@POST
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteFriendship(@PathParam("id") String id) {
		return frendService.deleteFriendship(id);
	}

}
