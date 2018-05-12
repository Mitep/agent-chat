package service.impl;

import java.util.Collection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;

import model.User;
import service.interfaces.UserServiceLocal;

/**
 * Session Bean implementation class UserService
 */
@Stateless
@LocalBean
public class UserService implements UserServiceLocal {

	private Datastore datastore;
	
    /**
     * Default constructor. 
     */
    public UserService() {
    	Morphia morphia = new Morphia();
		datastore = morphia.createDatastore(new MongoClient(), GroupService.DB_NAME);
		morphia.mapPackage("model");
    }

	@Override
	public boolean createUser(String str) {
		try {
			Gson g = new Gson();
			User u = g.fromJson(str, User.class);
			datastore.save(u);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public User getUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<User> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteUser(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
