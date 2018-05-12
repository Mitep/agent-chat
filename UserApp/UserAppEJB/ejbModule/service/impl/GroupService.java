package service.impl;

import java.util.Collection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

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
	public static final String DB_NAME = "UserApp";
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
		datastore = morphia.createDatastore(new MongoClient(), DB_NAME);
		morphia.mapPackage("model");
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
		return null;
	}

	@Override
	public Collection<Group> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteGroup(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
