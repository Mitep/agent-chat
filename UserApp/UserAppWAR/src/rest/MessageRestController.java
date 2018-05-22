package rest;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

	private Context ctx;

	public MessageRestController() throws NamingException {
		ctx = new InitialContext();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessage(@PathParam("id") String id) {
		MessageServiceLocal msgService;
		try {
			msgService = (MessageServiceLocal) ctx.lookup(MessageServiceLocal.LOOKUP_GLOBAL);
			return msgService.getMessage(id);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean createMessage(Message m) {
		MessageServiceLocal msgService;
		try {
			msgService = (MessageServiceLocal) ctx.lookup(MessageServiceLocal.LOOKUP_GLOBAL);
			return msgService.createMessage(new Gson().toJson(m));
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> readAllMessages() {
		MessageServiceLocal msgService;
		try {
			msgService = (MessageServiceLocal) ctx.lookup(MessageServiceLocal.LOOKUP_GLOBAL);
			return msgService.readAll();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean updateMessage(MessageDTO dto) {
		MessageServiceLocal msgService;
		try {
			msgService = (MessageServiceLocal) ctx.lookup(MessageServiceLocal.LOOKUP_GLOBAL);
			Message m = new Message(new ObjectId(dto.getId()), dto.getType(), dto.getSender(), dto.getReceiver(),
					dto.getContent());
			return msgService.updateMessage(m);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@POST
	@Path("/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteMessage(@PathParam("id") String id) {
		MessageServiceLocal msgService;
		try {
			msgService = (MessageServiceLocal) ctx.lookup(MessageServiceLocal.LOOKUP_GLOBAL);
			return msgService.deleteMessage(id);
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@GET
	@Path("/get/{sender}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMsgsBySender(@PathParam("sender") String sender) {
		MessageServiceLocal msgService;
		try {
			msgService = (MessageServiceLocal) ctx.lookup(MessageServiceLocal.LOOKUP_GLOBAL);
			return msgService.getMessagesBySender(sender);
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
