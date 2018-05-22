package service.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

import model.Message;
import service.interfaces.MessageServiceLocal;

/**
 * Session Bean implementation class MessageService
 */
@Stateless
@LocalBean
public class MessageService implements MessageServiceLocal {

	private Datastore datastore;

	/**
	 * Default constructor.
	 */
	public MessageService() {
		Morphia morphia = new Morphia();
		morphia.mapPackage("model");
		datastore = morphia.createDatastore(new MongoClient(), GroupService.DB_NAME);
		datastore.ensureIndexes();
	}

	@Override
	public boolean createMessage(String str) {
		try {
			Gson g = new Gson();
			Message poruka = g.fromJson(str, Message.class);
			datastore.save(poruka);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Message getMessage(String id) {
		Message m = datastore.get(Message.class, new ObjectId(id));
		return m;
	}

	@Override
	public List<Message> readAll() {
		return datastore.createQuery(Message.class).asList();
	}

	@Override
	public boolean deleteMessage(String id) {
		try {
			final Query<Message> upit = datastore.createQuery(Message.class).filter("_id", new ObjectId(id));
			WriteResult w = datastore.delete(upit);

			if (w.getN() == 1) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateMessage(Message msg) {
		try {
			Query<Message> query = datastore.createQuery(Message.class).field("_id").equal(msg.getId());
			UpdateOperations<Message> ops = datastore.createUpdateOperations(Message.class);
			ops.set("type", msg.getType());
			ops.set("sender", msg.getSender());
			ops.set("receiver", msg.getReceiver());
			ops.set("content", msg.getContent());

			datastore.update(query, ops);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<Message> getMessagesBySender(String sender) {
		Query<Message> query =  datastore.createQuery(Message.class);
		query.criteria("sender").equal(sender);
		return query.asList();
	}

}
