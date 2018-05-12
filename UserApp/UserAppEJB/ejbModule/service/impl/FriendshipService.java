package service.impl;

import java.util.Collection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;

import model.Friendship;
import service.interfaces.FriendshipServiceLocal;

/**
 * Session Bean implementation class FriendshipService
 */
@Stateless
@LocalBean
public class FriendshipService implements FriendshipServiceLocal {

	private Datastore datastore;
	
    /**
     * Default constructor. 
     */
    public FriendshipService() {
    	Morphia morphia = new Morphia();
		datastore = morphia.createDatastore(new MongoClient(), GroupService.DB_NAME);
		morphia.mapPackage("model");
    }

	@Override
	public boolean createFriendship(String str) {
		try {
			Gson g = new Gson();
			Friendship fr = g.fromJson(str, Friendship.class);
			datastore.save(fr);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Friendship getFriendship(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Friendship> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteFriendship(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
