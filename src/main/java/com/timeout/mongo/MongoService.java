package com.timeout.mongo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.timeout.config.MongoDBConfig;
import com.timeout.utils.ContainerGetter;

public class MongoService {

	private static MongoService unique = null;

	public static MongoService instance() {
		if (null == unique) {
			unique = new MongoService();
		}
		return unique;
	}

	/**
	 * 重复索引主键错误码
	 */
	private static final int ERROR_DUPLICATE_KEY = 11000;

	public MongoDatabase getDatabase(String databaseName) {
		return MongoDBConfig.mongoClient.getDatabase(databaseName);
	}

	public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
		return MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
	}

	/**
	 * 插入一个数据
	 *
	 */
	public void insertIngore(String databaseName, String collectionName, Document document) {
		try {
			MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
//			addRegion(document);
			collection.insertOne(document);
		} catch (com.mongodb.MongoWriteException e) {
			// 忽略重复主键
			if (e.getCode() == ERROR_DUPLICATE_KEY) {
				return;
			}
			throw e;
		}
	}

	/**
	 * @param databaseName 数据库名称
	 * @param collectionName 表名（集合名称）
	 * @param indesList 要插入的索引
	 * @param opt 操作 ”1“：升序，”-1“：降序。
	 * @Date 2018年9月13日 下午2:43:10
	 */
	public void createIndex(String databaseName, String collectionName, List<String> indesList, int opt) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
//			addRegion(document);
		MongoCursor<Document> indexs = collection.listIndexes().iterator();
		Set<String> indexSet = new HashSet<>();
		while (indexs.hasNext()) {
			Document index = indexs.next();
			indexSet.add(index.getString("name"));
		}
		for (String index_key : indesList) {
			if (!indexSet.contains(index_key + "_" + opt)) {
				collection.createIndex(new Document(index_key, opt));
			}
		}
	}

	/**
	 * 指定region插入
	 *
	 * @param collectionName
	 * @param region
	 * @param document
	 */
	public void insertIngore(String databaseName, int region, String collectionName, Document document) {
		try {
			MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
//			document.append(getShardingField(), region);
			collection.insertOne(document);
		} catch (com.mongodb.MongoWriteException e) {
			// 忽略重复主键
			if (e.getCode() == ERROR_DUPLICATE_KEY) {
				return;
			}
			throw e;
		}
	}

	/**
	 * 插入多个数据带region
	 *
	 * @param collectionName
	 */
	public void insertMany(String databaseName, String collectionName, List<Document> documentList) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
//		for(Document document : documentList){
//			addRegion(document);
//		}
		collection.insertMany(documentList);
	}

	/**
	 * 批量插入
	 *
	 * @param collectionName
	 * @param documentList
	 */
	public void insertManyIngore(String databaseName, String collectionName, List<Document> documentList) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		try {
//			for(Document document : documentList){
//				addRegion(document);
//			}
			collection.insertMany(documentList);
		} catch (com.mongodb.MongoWriteException e) {
			// 忽略重复主键
			if (e.getCode() == ERROR_DUPLICATE_KEY) {
				return;
			}
			throw e;
		}
	}

	/**
	 * 指定region插入
	 *
	 * @param region
	 * @param collectionName
	 * @param documentList
	 */
	public void insertManyIngore(String databaseName, int region, String collectionName, List<Document> documentList) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		try {
//			for(Document document : documentList){
//				document.append(getShardingField(), region);
//			}
			collection.insertMany(documentList);
		} catch (com.mongodb.MongoWriteException e) {
			// 忽略重复主键
			if (e.getCode() == ERROR_DUPLICATE_KEY) {
				return;
			}
			throw e;
		}
	}

	/**
	 * @param databaseName
	 * @param collectionName
	 * @param original
	 * @param update
	 * @Date 2018年5月7日 下午2:20:28
	 */
	public void update(String databaseName, String collectionName, Document original, Document update) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.updateOne(original, new Document().append("$set", update));
	}

	public void findAndModify(String databaseName, String collectionName, Document origin, Document update) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.findOneAndUpdate(origin, new Document().append("$set", update));
	}

	public void findAndReplace(String databaseName, String collectionName, Document original, Document update) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.findOneAndReplace(original, update);
	}

	public void replaceOne(String databaseName, String collectionName, Document original, Document update) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.replaceOne(original, update);
	}

	/**
	 *
	 * @param
	 * @return
	 */
	public Document queryOne(String databaseName, String collectionName, Bson filters) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		Document document = collection.find(filters).first();
		if (document == null) {
			return new Document();
		}
		return document;
	}

	/**
	 * 查询排序后的第一个
	 */
	public Document queryOne(String databaseName, String collectionName, Document sort, Bson filters) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		Document document = collection.find(filters).sort(sort).first();
		if (document == null) {
			return new Document();
		}
		return document;
	}

	/**
	 *
	 * @param collectionName
	 * @return
	 */
	public List<Document> queryAll(String databaseName, String collectionName, Bson filter) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		MongoCursor<Document> cursor = null;
		if (null == filter) {
			cursor = collection.find().iterator();
		} else {
			cursor = collection.find(filter).iterator();
		}
		List<Document> list = ContainerGetter.arrayList();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	/**
	 * 查询数据
	 *
	 * @param collectionName
	 * @param start
	 * @param limit
	 * @return
	 */

	public List<Document> queryAll(String databaseName, String collectionName, Document filter, int start, int limit) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(filter).skip(start).limit(limit).iterator();
		List<Document> list = ContainerGetter.arrayList();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	public List<Document> queryAll(String databaseName, String collectionName, int start, int limit) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find().skip(start).limit(limit).iterator();
		List<Document> list = ContainerGetter.arrayList();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	public List<Document> queryAll(String databaseName, String collectionName, Bson filter, Document sort, int start, int limit) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(filter).sort(sort).skip(start).limit(limit).iterator();
		List<Document> list = ContainerGetter.arrayList();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	public List<Document> queryAll(String databaseName, String collectionName, Bson filter, Bson sort, int start, int limit) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(filter).sort(sort).skip(start).limit(limit).iterator();
		List<Document> list = ContainerGetter.arrayList();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	public long count(String databaseName, String collectionName) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		return collection.count();
	}

	public long count(String databaseName, String collectionName, Bson filter) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		return collection.count(filter);
	}

	public void updateMulti(String databaseName, String collectionName, Bson filter, Document update) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.updateMany(filter, new Document().append("$set", update));
	}

	public void deleteById(String databaseName, String collectionName, Object id) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.deleteOne(Filters.eq("_id", id));
	}

	public void deleteByIDs(String databaseName, String collectionName, List<String> ids) {

		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.deleteMany(Filters.in("_id", ids.toArray()));
	}

	public void deleteFieldFirst(String databaseName, String collectionName, Document filter, Document update) {
		MongoCollection<Document> collection = MongoDBConfig.mongoClient.getDatabase(databaseName).getCollection(collectionName);
		collection.updateOne(filter, new Document().append("$unset", update));
	}

	public Integer getTotalSize(String databaseName, Set<String> collectionNameSet) {
		Integer totalSize = 0;
		if (null != collectionNameSet && !collectionNameSet.isEmpty()) {

			Set<String> allCollectionNameSet = ContainerGetter.hashSet();
			MongoCursor<String> cursor = MongoDBConfig.mongoClient.getDatabase(databaseName).listCollectionNames().iterator();
			while (cursor.hasNext()) {
				allCollectionNameSet.add(cursor.next());
			}
			cursor.close();

			for (String collectionName : collectionNameSet) {
				if (!allCollectionNameSet.contains(collectionName)) {
					continue;
				}

				BsonDocument command = new BsonDocument("collStats", new BsonString(collectionName));
				Document doc = MongoDBConfig.mongoClient.getDatabase(databaseName).runCommand(command, MongoDBConfig.mongoClient.getDatabase(databaseName).getReadPreference());
				if (null != doc) {
					totalSize += doc.getInteger("size", 0);
				}
			}
		}
		return totalSize;
	}

}
