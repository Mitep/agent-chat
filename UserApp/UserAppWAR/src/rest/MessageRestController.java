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

import dtos.MessageDTO;
import model.Message;
import service.interfaces.MessageServiceLocal;

/**
 * @author Nikola
 *
 */
@Path("/message")
public class MessageRestController {

	@EJB
	private MessageServiceLocal msgService;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessage(@PathParam("id") String id) {
		return msgService.getMessage(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean createMessage(Message m) {
		return msgService.createMessage(new Gson().toJson(m));
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> readAllMessages() {
		return msgService.readAll();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateMessage(MessageDTO dto) {
		Message m = new Message(new ObjectId(dto.getId()), dto.getType(), dto.getSender(), dto.getReceiver(),
				dto.getTimestamp(), dto.getContent());
		return msgService.updateMessage(m);
	}

	@POST
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteMessage(@PathParam("id") String id) {
		return msgService.deleteMessage(id);
	}

}
