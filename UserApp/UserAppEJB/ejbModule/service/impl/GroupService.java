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
import service.interfaces.GroupServiceLocal;

/**
 * Session Bean implementation class GroupService
 */
@Stateless
@LocalBean
public class GroupService implements GroupServiceLocal {

	public static final String DB_HOST = "127.0.0.1";
	public static final int DB_PORT = 27017;
	public static final String DB_NAME = "UserAppDB";
	public static final String USERS = "Users";
	public static final String MESSAGES = "Messages";
	public static final String GROUPS = "Groups";
	public static final String FRIENDSHIPS = "Friendships";

	private Datastore datastore;

	/**
	 * Default constructor.
	 */
	public GroupService() {
		Morphia morphia = new Morphia();
		morphia.mapPackage("model");
		datastore = morphia.createDatastore(new MongoClient(), DB_NAME);
		datastore.ensureIndexes();
	}

	@Override
	public boolean createGroup(String str) {
		try {
			Gson g = new Gson();
			Group grupa = g.fromJson(str, Group.class);
			datastore.save(grupa);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Group getGroup(String id) {
		Group g = datastore.get(Group.class, new ObjectId(id));
		return g;
	}

	@Override
	public List<Group> readAll() {
		return datastore.createQuery(Group.class).asList();
	}

	@Override
	public boolean deleteGroup(String id) {
		try {
			final Query<Group> upit = datastore.createQuery(Group.class).filter("_id", new ObjectId(id));
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
	public boolean updateGroup(Group group) {
		try {
			Query<Group> query = datastore.createQuery(Group.class).field("_id").equal(group.getId());
			UpdateOperations<Group> ops = datastore.createUpdateOperations(Group.class);
			ops.set("name", group.getName());

			datastore.update(query, ops);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean addMember(String group, String member) {
		try {
			Query<Group> query = datastore.createQuery(Group.class).field("_id").equal(new ObjectId(group));
			UpdateOperations<Group> ops = datastore.createUpdateOperations(Group.class);
			ops.addToSet("members", member);

			datastore.update(query, ops);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeMember(String group, String member) {
		try {
			Group g = datastore.createQuery(Group.class).field("_id").equal(new ObjectId(group)).get();
			for (String user : g.getMembers()) {
				if (user.equals(member)) {
					System.out.println("if");
					g.getMembers().remove(member);
					break;
				}
			}

			datastore.save(g);
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<ObjectId> getMessages(String group) {
		try {
			Query<Group> query = datastore.createQuery(Group.class).field("_id").equal(new ObjectId(group));
			ArrayList<ObjectId> poruke = query.get().getMessages();
			return poruke;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean addMessage(String group, String message) {
		System.out.println(group);
		System.out.println(message);
		try {
			Query<Group> query = datastore.createQuery(Group.class).field("_id").equal(new ObjectId(group));
			UpdateOperations<Group> ops = datastore.createUpdateOperations(Group.class);
			ObjectId poruka = new ObjectId(message);
			if (query.get().getMessages() == null) {
				query.get().setMessages(new ArrayList<ObjectId>());
			}

			if (!query.get().getMessages().contains(poruka)) {
				ops.addToSet("messages", new ObjectId(message));
				datastore.update(query, ops);
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeMessage(String group, String message) {
		try {
			Group g = datastore.createQuery(Group.class).field("_id").equal(new ObjectId(group)).get();
			ObjectId poruka = new ObjectId(message);
			for (ObjectId msg : g.getMessages()) {
				if (msg.equals(poruka)) {
					g.getMessages().remove(poruka);
					break;
				}
			}

			datastore.save(g);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
