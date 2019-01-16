package cn.abelib.data.mongo;

import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 *
 * @author abel
 * @date 2018/2/18
 *  MongoDB DAO 接口
 */

public interface MongoDao {
    /**
     *  插入文档
     * @param table
     * @param map
     */
    void insert(String table, Map<String, Object> map);

    void insert(String table, List<Map<String, Object>> mapList);

    List<Document> list(String table);

    void list(String table, int pageSize, int pageNum);

    boolean delete(String table, String id);

    boolean delete(String table, List<String> idList);

    boolean delete(String table);

    boolean deleteMany(String table, String id);

    boolean deleteMany(String table, List<String> idList);

    void update(String table, Integer id, String field, Object object);

    List<Document> find(String table, List<String> idList);

    Document findById(String table, String id);

    List<Document> find(String table, Document document);

}
