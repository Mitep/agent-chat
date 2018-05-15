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
		morphia.mapPackage("model");
		datastore = morphia.createDatastore(new MongoClient(), GroupService.DB_NAME);
		datastore.ensureIndexes();
	}

	@Override
	public boolean createUser(String str) {
		try {
			Gson g = new Gson();
			User u = g.fromJson(str, User.class);
			List<User> users = readAll();
			boolean flag = true;
			for(User user:users){
				System.out.println(user.getUsername());	
				if(u.getUsername().equals(user.getUsername())){
					System.out.println("Username already exists!");
					flag = false;
					break;
				}
			}
			if(flag==true){
				datastore.save(u);
				System.out.println("Registered.");
			}
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public User getUser(String id) {
		User u = datastore.get(User.class, new ObjectId(id));
		return u;
	}

	@Override
	public List<User> readAll() {
		return datastore.createQuery(User.class).asList();
	}

	@Override
	public boolean deleteUser(String username) {
		try {
			final Query<User> upit = datastore.createQuery(User.class).field("username").equal(username);
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
	public boolean updateUser(User user) {
		try {
			Query<User> query = datastore.createQuery(User.class).field("username").equal(user.getUsername());
			UpdateOperations<User> ops = datastore.createUpdateOperations(User.class);
			ops.set("name", user.getName());
			ops.set("surname", user.getSurname());
			ops.set("password", user.getPassword());

			datastore.update(query, ops);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public User getUserByUsername(String username) {
		List<User> lista = datastore.createQuery(User.class).filter("username == ", username).asList();
		if (lista.size() == 1) {
			return lista.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean validateUser(String username, String password) {
		// TODO Auto-generated method stub
		List<User> lista = datastore.createQuery(User.class).filter("username == ", username).filter("password == ", password).asList();
		if (lista.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
