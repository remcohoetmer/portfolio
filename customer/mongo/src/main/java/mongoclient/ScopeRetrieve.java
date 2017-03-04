package mongoclient;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClients;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * The tutorial from http://docs.mongodb.org/ecosystem/tutorial/getting-started-with-java-driver/
 */
public class ScopeRetrieve {
    // CHECKSTYLE:OFF
    /**
     * Run this main method to see the output of this quick example.
     *
     * @param args takes no args
     * @throws UnknownHostException if it cannot connect to a MongoDB instance at localhost:27017
     */
    public static void main(final String[] args) throws UnknownHostException {
        // connect to the local database server
        MongoClient mongoClient = new MongoClient();

        /*
        // Authenticate - optional
        MongoCredential credential = MongoCredential.createMongoCRCredential(userName, database, password);
        MongoClient mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));
        */

        MongoDatabase db_rw = mongoClient.getDatabase("rw");

        // get a list of the collections in this database and print them out
        ListCollectionsIterable<Document> collectionNames = db_rw.listCollections();
        System.out.println("Collection" + collectionNames.toString());
        for (final Document s : collectionNames) {
            System.out.println("Collection" + s);
        }

        // get a collection object to work with
        MongoCollection<Document> coll = db_rw.getCollection("scope");

        // drop all the data in it
        coll.drop();

        // make a document and insert it
        Document doc1 = new Document("name", "Marketing")
                .append("status", "active");
        Document doc2 = new Document("name", "Operations")
                .append("status", "inactive");
        Document doc3 = new Document("name", "Production")
                .append("status", "active");
        Document doc4 = new Document("name", "Development")
                .append("status", "active");

        coll.insertMany(Arrays.asList(doc1, doc2,doc3,doc4));

        // get it (since it's the only one in there since we dropped the rest earlier on)
        Bson filter = Filters.eq("status", "active");
        FindIterable<Document> myDoc = coll.find(filter);
        for (Document document : myDoc) {
        	  System.out.println(document);
		}
       //db.dropDatabase();
        mongoClient.close();
    }
    void async() throws InterruptedException {
    	// or a Connection String
    	com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost"));

    	com.mongodb.async.client.MongoDatabase db_rw = mongoClient.getDatabase("rw");
        // get a list of the collections in this database and print them out
        com.mongodb.async.client.ListCollectionsIterable<Document> collectionNames = db_rw.listCollections();
        collectionNames.forEach( y-> System.out.println(y)
				
		, new SingleResultCallback<Void>() {
			@Override
			public void onResult(Void result, Throwable t) {
				
			}
		});

        // get a collection object to work with
        com.mongodb.async.client.MongoCollection<Document> coll = db_rw.getCollection("scope");

        // drop all the data in it
//        coll.drop();

        // make a document and insert it
        Document doc1 = new Document("name", "Marketing")
                .append("status", "active");
        Document doc2 = new Document("name", "Operations")
                .append("status", "inactive");
        Document doc3 = new Document("name", "Production")
                .append("status", "active");
        Document doc4 = new Document("name", "Development")
                .append("status", "active");

        SingleResultCallback<Void> callback =  (Void result, Throwable t) -> {
				System.err.println( "inserted, result"+ result);
			};
		coll.insertMany(Arrays.asList(doc1, doc2,doc3,doc4), callback);

        // get it (since it's the only one in there since we dropped the rest earlier on)
        Bson filter = Filters.eq("status", "active");
 
        List<Document> docs = new ArrayList<>();
        coll.find(filter).into(docs,
        new SingleResultCallback<List<Document>>() {
            @Override
            public void onResult(final List<Document> result, final Throwable t) {
                System.out.println("Found Documents: #" + result.size());
            }
        });
       //db.dropDatabase();
        Thread.sleep(100);
        mongoClient.close();
    }
    // CHECKSTYLE:ON
}