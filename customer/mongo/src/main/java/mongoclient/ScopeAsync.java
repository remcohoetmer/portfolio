package mongoclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClients;
import com.mongodb.client.model.Filters;



public class ScopeAsync {

	public static void async() throws UnknownHostException, InterruptedException {

		// or a Connection String
		com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost"));

		com.mongodb.async.client.MongoDatabase db_rw = mongoClient.getDatabase("rw");
		// get a list of the collections in this database and print them out
		com.mongodb.async.client.ListCollectionsIterable<Document> collectionNames = db_rw.listCollections();

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


		coll.find(filter).into(new ArrayList<Document>(),
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
	public static void main(final String[] args) throws Exception {
		convertbson();
	}
	static class Scope {
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		String name;
		String status;
		public String toString() {
			return "name=" + name + " status="+ status + "\n";
		}
		public Scope()
		{

		}
	}
	private static void convertbson() throws JsonParseException, JsonMappingException, IOException {
		Document doc1 = new Document("name", "Marketing")
				.append("status", "active");
		final String json = doc1.toJson();
		ObjectMapper mapper= new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Scope   xx = mapper.readValue(json, Scope.class);
		System.out.println(xx);

	}
}