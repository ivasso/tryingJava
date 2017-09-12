import com.mongodb.*;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.Arrays;
import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.inc;

public class Main {
    public static void main(String[] args ){

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("testDatabase");
        MongoCollection <Document> collection = database.getCollection("test");

//        printFirst(collection);
//        collectionCount(collection);
//        printAll(collection);
//        printFirstOrLast(collection, 1);
//        findByStringAndInt(collection, "i", 30);
//        findByRange(collection, "i", 10, 14);
//        updateOneByField(collection, "i", 10, 1234);
//        updateManyByField(collection, "i", 0, 1);
//        incrementManybyField(collection, "i", 1, -100);
        deleteOneByField(collection, "i", 42);
        deleteManyByField(collection, "i", -99);
    }

    private void documentCreation(MongoCollection <Document> collection) {

        Document document = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));

        collection.insertOne(document);;
    }
    private void documentsCreation(MongoCollection <Document> collection) {

        List <Document> documents = new ArrayList <Document>();
        for (int i = 0; i < 100; i++) {
            documents.add(new Document("i", i));
        }
        collection.insertMany(documents);
    }
    private void printFirst (MongoCollection <Document> collection) {

        Document doc = collection.find().first();
        System.out.println(doc.toJson());
    }
    private static void collectionCount (MongoCollection <Document> collection) {

        System.out.println(collection.count());
    }
    private static void printAll (MongoCollection <Document> collection) {

        MongoCursor <Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }
    private static void printFirstOrLast(MongoCollection <Document> collection, int zeroFirstElseLast) {

        if (zeroFirstElseLast == 0) {
            System.out.println(collection.find().first());
        } else {
            MongoCursor <Document> cursor = collection.find().iterator();
            for (int i = 0; i < collection.count() -1; i++) {
                cursor.next();
            }
            System.out.println(cursor.next().toJson());
        }
    }
    private static void findByStringAndInt (MongoCollection <Document> collection, String str, Integer i) {

        Document document = collection.find(eq(str, i)).first();
        System.out.println(document.toJson());
    }
    private static void findByRange (MongoCollection <Document> collection, String fieldName, int from, int to) {

        Block <Document> printBlock = new Block <Document>() {
            public void apply (final Document document) {

                System.out.println(document.toJson());
            }
        };

        collection.find(and(gte(fieldName, from), lte(fieldName, to))).forEach(printBlock);
    }
    private static void updateOneByField (MongoCollection <Document> collection, String fieldName, int value, int newValue) {
        collection.updateOne(eq(fieldName, value), new Document("$set", new Document(fieldName, newValue)));
    }
    private static void incrementManybyField (MongoCollection <Document> collection, String fieldName, int value, int newValue) {

        UpdateResult updateResult = collection.updateMany(eq(fieldName, value), inc(fieldName, (Number)newValue));
        System.out.println(updateResult.getModifiedCount());
    }
    private static void updateManyByField (MongoCollection <Document> collection, String fieldName, int value, int newValue) {

        UpdateResult updateResult = collection.updateMany(eq(fieldName, value), new Document("$set", new Document(fieldName, newValue)));
        System.out.println(updateResult.getModifiedCount());
    }
    private static void deleteOneByField (MongoCollection <Document> collection, String fieldName, int eqValue) {

        DeleteResult deleteResult = collection.deleteOne(eq(fieldName, eqValue));
        System.out.println("deleted" + deleteResult.getDeletedCount());
    }
    private static void deleteManyByField (MongoCollection <Document> collection, String fieldName, int eqValue) {

        DeleteResult deleteResult = collection.deleteMany(eq(fieldName, eqValue));
        System.out.println("deleted" + deleteResult.getDeletedCount());
    }
}
