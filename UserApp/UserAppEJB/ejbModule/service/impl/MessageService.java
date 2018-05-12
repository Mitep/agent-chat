package service.impl;

import java.util.Collection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.mongodb.MongoClient;

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
		datastore = morphia.createDatastore(new MongoClient(), GroupService.DB_NAME);
		morphia.mapPackage("model");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Message> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteMessage(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
