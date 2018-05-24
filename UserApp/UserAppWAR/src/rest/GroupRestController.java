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

	private Context ctx;

	public GroupRestController() throws NamingException {
		ctx = new InitialContext();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Group getGroup(@PathParam("id") String id) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.getGroup(id);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Group createGroup(Group g) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.createGroup(new Gson().toJson(g));
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Group> readAllGroups() {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.readAll();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateGroup(GroupDTO dto) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			Group g = new Group(new ObjectId(dto.getId()), dto.getName(), dto.getMembers());
			return groupService.updateGroup(g);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteGroup(@PathParam("id") String id) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.deleteGroup(id);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/user/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addUser(@FormParam("groupId") String groupId, @FormParam("user") String user) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.addMember(groupId, user);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/user/remove")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean removeUser(@FormParam("groupId") String groupId, @FormParam("user") String user) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.removeMember(groupId, user);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/message/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addMessage(@FormParam("group") String group, @FormParam("message") String message) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.addMessage(group, message);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/message/remove")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean removeMessage(@FormParam("group") String group, @FormParam("message") String message) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.removeMessage(group, message);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@GET
	@Path("/members/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> members(@PathParam("id") String id) {
		GroupServiceLocal groupService;
		try {
			groupService = (GroupServiceLocal) ctx.lookup(GroupServiceLocal.LOOKUP_GLOBAL);
			return groupService.getMembers(id);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
