package com.userapp.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Session Bean implementation class HelloWorldBean
 */
@Stateless
@LocalBean
public class HelloWorldBean implements HelloWorld {

    /**
     * Default constructor. 
     */
    public HelloWorldBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public String sayHello() {
		return "Hello World !!!";
	}

	@Override
	public void dbTest() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("agentiDB");
		MongoCollection<Document> collection = database.getCollection("user");
		Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1).append("info",
				new Document("x", 203).append("y", 102));
		System.out.println(doc);
		collection.insertOne(doc);
		mongoClient.close();
		System.out.println("Check database for changes");
	}

}
