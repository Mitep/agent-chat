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

import org.bson.types.ObjectId;

import com.google.gson.Gson;

import dtos.GroupDTO;
import model.Group;
import service.interfaces.GroupServiceLocal;

/**
 * @author Nikola
 *
 */
@Path("/group")
public class GroupRestController {

	@EJB
	private GroupServiceLocal groupService;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Group getGroup(@PathParam("id") String id) {
		return groupService.getGroup(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean createGroup(Group g) {
		return groupService.createGroup(new Gson().toJson(g));
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Group> readAllGroups() {
		return groupService.readAll();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateGroup(GroupDTO dto) {
		Group g = new Group(new ObjectId(dto.getId()), dto.getName(), dto.getMembers());
		return groupService.updateGroup(g);
	}

	@POST
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteGroup(@PathParam("id") String id) {
		return groupService.deleteGroup(id);
	}

}
