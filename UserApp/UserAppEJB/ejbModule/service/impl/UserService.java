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

import dtos.UserSearchDTO;
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
	public User createUser(String str) {
		try {
			Gson g = new Gson();
			User u = g.fromJson(str, User.class);
			List<User> users = readAll();
			for (User user : users) {
				if (u.getUsername().equals(user.getUsername())) {
					System.out.println("Username " + u.getUsername() + " already exists!");
					return null;
				}
			}

			datastore.save(u);
			return u;
		} catch (Exception e) {
			return null;
		}
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
		List<User> lista = datastore.createQuery(User.class).filter("username == ", username)
				.filter("password == ", password).asList();
		if (lista.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<User> getUserByName(String name) {
		List<User> lista = datastore.createQuery(User.class).filter("name == ", name).asList();
		if (lista.size() != 0) {
			return lista;
		} else {
			return null;
		}
	}

	@Override
	public List<User> getUserBySurname(String surname) {
		List<User> lista = datastore.createQuery(User.class).filter("surname == ", surname).asList();
		if (lista.size() != 0) {
			return lista;
		} else {
			return null;
		}
	}

	@Override
	public List<String> getUsersGroups(String username) {
		Query<User> query = datastore.createQuery(User.class);
		User user = query.field("username").equal(username).get();
		return user.getGroups();
	}

	@Override
	public List<String> addGroup(String username, String groupId) {
		User u = getUserByUsername(username);
		if (u != null) {
			System.out.println("not null");
			if (u.getGroups() == null) {
				u.setGroups(new ArrayList<String>());
			}
			u.getGroups().add(groupId);

			datastore.save(u);

			return u.getGroups();
		} else {
			System.out.println("ipak je null");
			return null;
		}
	}

	@Override
	public List<User> findUsers(String username, String name, String surname) {
		List<User> listUsrNames = new ArrayList<User>();
		List<User> listNames = new ArrayList<User>();
		List<User> listSurnames = new ArrayList<User>();

		if (username != null && username != "")
			listUsrNames = datastore.createQuery(User.class).field("username").containsIgnoreCase(username).asList();
		if (name != null && name != "")
			listNames = datastore.createQuery(User.class).field("name").containsIgnoreCase(name).asList();
		if (surname != null && surname != "")
			listSurnames = datastore.createQuery(User.class).field("surname").containsIgnoreCase(surname).asList();

		ArrayList<User> korisnici = new ArrayList<>();
		korisnici.addAll(listUsrNames);
		korisnici.addAll(listNames);
		korisnici.addAll(listSurnames);
		return korisnici;
	}

	@Override
	public List<UserSearchDTO> findUsersDTO(String username, String name, String surname) {
		List<User> listUsrNames = new ArrayList<User>();
		List<User> listNames = new ArrayList<User>();
		List<User> listSurnames = new ArrayList<User>();

		if (!username.equals("") && username != null)
			listUsrNames = datastore.createQuery(User.class).field("username").containsIgnoreCase(username).asList();
		if (!name.equals("") && name != null)
			listNames = datastore.createQuery(User.class).field("name").containsIgnoreCase(name).asList();
		if (!surname.equals("") && surname != null)
			listSurnames = datastore.createQuery(User.class).field("surname").containsIgnoreCase(surname).asList();

		ArrayList<UserSearchDTO> korisnici = new ArrayList<>();

		ArrayList<String> usernames = new ArrayList<String>();

		for (User u : listUsrNames) {
			UserSearchDTO utemp = new UserSearchDTO(u.getUsername(), u.getName(), u.getSurname());
			if (!usernames.contains(u.getUsername()))
				korisnici.add(utemp);
		}
		for (User u : listNames) {
			UserSearchDTO utemp = new UserSearchDTO(u.getUsername(), u.getName(), u.getSurname());
			if (!usernames.contains(u.getUsername()))
				korisnici.add(utemp);
		}
		for (User u : listSurnames) {
			UserSearchDTO utemp = new UserSearchDTO(u.getUsername(), u.getName(), u.getSurname());
			if (!usernames.contains(u.getUsername()))
				korisnici.add(utemp);
		}

		// korisnici.addAll(listUsrNames);
		// korisnici.addAll(listNames);
		// korisnici.addAll(listSurnames);

		return korisnici;
	}

	@Override
	public boolean addFriend(String user, String friend) {
		try {
			User u = getUserByUsername(user);
			u.getFriends().add(friend);
			datastore.save(u);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean removeFriend(String user, String friend) {
		try {
			User u = getUserByUsername(user);
			u.getFriends().remove(friend);
			User rec = getUserByUsername(friend);
			rec.getFriends().remove(user);
			datastore.save(u);
			datastore.save(rec);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean sendFriendRequest(String user, String friend) {
		try {
			User sender = getUserByUsername(user);
			User recipient = getUserByUsername(friend);
			sender.getSentRequests().add(friend);
			recipient.getReceivedRequests().add(user);
			datastore.save(sender);
			datastore.save(recipient);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean acceptFriendRequest(String user, String friend) {
		try {
			User accepter = getUserByUsername(user);
			User initiator = getUserByUsername(friend);
			accepter.getReceivedRequests().remove(friend);
			accepter.getFriends().add(friend);
			initiator.getSentRequests().remove(user);
			initiator.getFriends().add(user);
			datastore.save(accepter);
			datastore.save(initiator);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean rejectFriendRequest(String user, String friend) {
		try {
			User accepter = getUserByUsername(user);
			User initiator = getUserByUsername(friend);
			accepter.getReceivedRequests().remove(friend);
			initiator.getSentRequests().remove(user);
			datastore.save(accepter);
			datastore.save(initiator);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
