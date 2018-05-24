package service.impl;

import java.util.ArrayList;
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

import model.Group;
import model.Message;
import model.User;
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
			ObjectId id_poruke = datastore.get(poruka).getId();
			if (poruka.getType() == Message.GROUP_MSG) {
				ObjectId id_grupe = new ObjectId(poruka.getReceiver());
				
				Group grupa = datastore.createQuery(Group.class).field("_id").equal(id_grupe).get();
				grupa.getMessages().add(id_poruke.toHexString());
				datastore.save(grupa);
			}

		} catch (Exception e) {
			e.printStackTrace();
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
		Query<Message> query = datastore.createQuery(Message.class);
		query.criteria("sender").equal(sender);
		return query.asList();
	}

	@Override
	public List<Message> getRelatedMessages(String user) {
		Query<Message> query = datastore.createQuery(Message.class);
		Query<Message> query2 = datastore.createQuery(Message.class);
		query.criteria("sender").equal(user);
		query2.criteria("receiver").equal(user);

		ArrayList<Message> msgs = new ArrayList<>();
		ArrayList<String> grupe = datastore.createQuery(User.class).filter("username == ", user).get().getGroups();
		if (grupe != null) {
			for (int i = 0; i < grupe.size(); i++) {
				ObjectId id_grupe = new ObjectId(grupe.get(i));
				Group g = datastore.createQuery(Group.class).field("_id").equal(id_grupe).get();
				if (g.getMessages() != null) {
					ArrayList<Message> listaPoruka = new ArrayList<>();
					for (String poruka : g.getMessages()) {
						ObjectId id_poruke = new ObjectId(poruka);
						listaPoruka.add(datastore.createQuery(Message.class).field("_id").equal(id_poruke).get());
					}
					msgs.addAll(listaPoruka);
				}
			}
		}

		msgs.addAll(query.asList());
		msgs.addAll(query2.asList());
		return msgs;
	}

}
