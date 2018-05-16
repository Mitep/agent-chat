package service.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

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
		morphia.mapPackage("model");
		datastore = morphia.createDatastore(new MongoClient(), GroupService.DB_NAME);
		datastore.ensureIndexes();
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
		Friendship f = datastore.get(Friendship.class, new ObjectId(id));
		return f;
	}

	@Override
	public List<Friendship> readAll() {
		return datastore.createQuery(Friendship.class).asList();
	}

	@Override
	public boolean deleteFriendship(String id) {
		try {
			final Query<Friendship> upit = datastore.createQuery(Friendship.class).filter("_id", new ObjectId(id));
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
	public boolean updateFriendship(Friendship fr) {
		try {
			Query<Friendship> query = datastore.createQuery(Friendship.class).field("_id").equal(fr.getId());
			UpdateOperations<Friendship> ops = datastore.createUpdateOperations(Friendship.class).set("userId",
					fr.getUserId());
			ops.set("userId2", fr.getUserId2());
			ops.set("status", fr.getStatus());

			UpdateResults ur = datastore.update(query, ops);
			return ur.getUpdatedCount() == 1;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean setFriends(String id) {

		try {
			Query<Friendship> query = datastore.createQuery(Friendship.class).field("_id").equal(new ObjectId(id));
			UpdateOperations<Friendship> ops = datastore.createUpdateOperations(Friendship.class).set("status",
					Friendship.FRIENDS);

			UpdateResults ur = datastore.update(query, ops);
			return ur.getUpdatedCount() == 1;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean setPending(String id) {
		try {
			Query<Friendship> query = datastore.createQuery(Friendship.class).field("_id").equal(new ObjectId(id));
			UpdateOperations<Friendship> ops = datastore.createUpdateOperations(Friendship.class).set("status",
					Friendship.PENDING);

			UpdateResults ur = datastore.update(query, ops);
			return ur.getUpdatedCount() == 1;
		} catch (Exception e) {
			return false;
		}
	}

}
