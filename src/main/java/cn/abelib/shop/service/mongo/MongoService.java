package cn.abelib.shop.service.mongo;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by abel on 2018/2/18.
 */
@Service
public class MongoService implements MongoDao{
    @Autowired
    private MongoDatabase mongoDatabase;

    public void insert(String table, Map<String, Object> map){
        if (map == null)
            return;
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        Document document = new Document(map);
        collection.insertOne(document);
    }

    public void insert(String table, List<Map<String, Object>> mapList){
        if (mapList == null || CollectionUtils.isEmpty(mapList))
            return;
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        List<Document> documents = Lists.newArrayList();
        for (Map<String, Object> map : mapList){
            documents.add(new Document(map));
        }
        collection.insertMany(documents);
    }

    public List<Document> list(String table){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        List<Document> documents = Lists.newArrayList();
        while (mongoCursor.hasNext()){
            Document document = mongoCursor.next();
            documents.add(document);
        }
        return documents;
    }

    //todo  分页
    public void list(String table, int pageSize, int pageNum){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        FindIterable<Document> findIterable = collection.find();
    }

    public boolean delete(String table, String id){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        Document document = this.findById(table, id);
        DeleteResult deleteResult = collection.deleteOne(document);
        long deleteCount = deleteResult.getDeletedCount();
       return deleteCount == 1;
    }

    public boolean delete(String table, List<String> idList){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        long deleteCount = 0;
        for (String id : idList){
            Document document = this.findById(table, id);
            DeleteResult deleteResult = collection.deleteOne(document);
            deleteCount += deleteResult.getDeletedCount();
        }
        return deleteCount == idList.size();
    }

    //todo
    public boolean delete(String table){
        return false;
    }

    public boolean deleteMany(String table, String id){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        Document document = this.findById(table, id);
        DeleteResult deleteResult = collection.deleteMany(document);
        long deleteCount = deleteResult.getDeletedCount();
        return deleteCount > 0;
    }

    public boolean deleteMany(String table, List<String> idList){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        long deleteCount = 0;
        for (String id : idList){
            Document document = this.findById(table, id);
            DeleteResult deleteResult = collection.deleteMany(document);
            deleteCount += deleteResult.getDeletedCount();
        }
        return deleteCount > idList.size() - 1;
    }

    public void update(String table, Integer id, String field, Object object){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        collection.updateOne(Filters.eq("_id", id), new Document("$set",new Document(field, object)));
    }

    public List<Document> find(String table, List<String> idList){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        List<Document> documents = Lists.newArrayList();
        for (String id : idList){
            Document document = this.findById(table, id);
            documents.add(document);
        }
        return documents;
    }

    public Document findById(String table, String id){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        BasicDBObject query = new BasicDBObject("_id", id);
        FindIterable<Document> iterable = collection.find(query);
        return iterable.first();
    }

    public List<Document> find(String table, Document document){
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        FindIterable<Document> iterable = collection.find(document);
        MongoCursor<Document> cursor = iterable.iterator();
        List<Document> documents = Lists.newArrayList();
        while (cursor.hasNext()){
            Document item = cursor.next();
            if (item != null)
                documents.add(item);
        }
        return documents;
    }
}
