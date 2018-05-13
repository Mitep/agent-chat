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
			final Query<Group> upit = datastore.createQuery(Group.class)
                    .filter("_id", new ObjectId(id));
			datastore.delete(upit);
			
		}catch(Exception e) {
			return false;
		}
		return true;
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

}
