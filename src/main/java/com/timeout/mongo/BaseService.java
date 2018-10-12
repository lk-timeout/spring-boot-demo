//package com.timeout.mongo;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//import com.timeout.utils.tuple.TowTuple;
//
//import cn.xlink.maintenance.exception.Rest400StatusException;
//import cn.xlink.maintenance.pojo.MongoSearchQuery;
//import cn.xlink.maintenance.util.ContainerGetter;
//import cn.xlink.maintenance.util.ERROR_CODE;
//import cn.xlink.maintenance.util.MongoQuery;
//import cn.xlink.maintenance.util.MongoQuery.FieldType;
//
//public abstract class BaseService {
//	public String nextId() {
//		return new ObjectId().toString();
//	}
//
//	/**
//	 * dubbo服务端请求超时时间
//	 */
//	public static final int SERVICE_TIME_OUT = 5000;
//
//	/**
//	 * 列表查询最多查1千条
//	 */
//	protected static final int LIMIT_MAX = 1000;
//	protected static final int DEFAULT_OFFSET = 0;
//	protected static final int DEFAULT_LIMIT = 10;
//
//	/**
//	 * 验证请求查询偏移量
//	 *
//	 * @param offset
//	 */
//	protected final void funcValidOffset(int offset) {
//		if (offset < 0) {
//			throw new Rest400StatusException(ERROR_CODE.PARAM_VALID_ERROR, "param offset error");
//		}
//	}
//
//	/**
//	 * 验证查询列表上限值
//	 *
//	 * @param limit
//	 */
//	protected final void funcValidLimit(int limit) {
//		if (limit <= 0 || limit > LIMIT_MAX) {
//			throw new Rest400StatusException(ERROR_CODE.PARAM_VALID_ERROR, "param limit error");
//		}
//	}
//
//	protected final MongoSearchQuery funcParseSearchQuery(Map<String, TowTuple<String, MongoQuery.FieldType>> fieldMap, JSONObject body_json) {
//		int offset = body_json.containsKey("offset") ? body_json.getInteger("offset") : DEFAULT_OFFSET;
//		int limit = body_json.containsKey("limit") ? body_json.getInteger("limit") : DEFAULT_LIMIT;
//		funcValidOffset(offset);
//		funcValidLimit(limit);
//		// 过滤字段
//		Set<String> filtersSet = ContainerGetter.hashSet();
//		// 进行参数解析
//		JSONArray filters = body_json.getJSONArray("filter");
//		JSONObject queryField = body_json.getJSONObject("query");
//		JSONObject orderField = body_json.getJSONObject("order");
//
//		Bson[] querys = new Bson[0];
//		Document sorts = new Document();
//
//		if (filters != null) {
//			for (int ix = 0, len = filters.size(); ix < len; ++ix) {
//				String field = filters.getString(ix);
//				filtersSet.add(field);
//			}
//		}
//		if (queryField != null) {
//			int index = 0;
//			for (String field : queryField.keySet()) {
//				JSONObject query = queryField.getJSONObject(field);
//				for (String opt : query.keySet()) {
//					MongoQuery.OpreateType opreateType = MongoQuery.OpreateType.fromString(opt);
//					if (opreateType == MongoQuery.OpreateType.Unkown) {
//						throw new Rest400StatusException(ERROR_CODE.PARAM_VALID_ERROR, "opreate type unkown :" + opt);
//					}
//					TowTuple<String, MongoQuery.FieldType> fieldType = fieldMap.get(field);
//					String value = query.getString(opt);
//					querys = Arrays.copyOf(querys, querys.length + 1);
//					if (fieldType != null) {
//						querys[index++] = opreateType.getQuery(fieldType.first, fieldType.second, value);
//					} else {
//						querys[index++] = opreateType.getQuery("extend." + field, FieldType.String, value);
//					}
//				}
//			}
//		}
//
//		if (orderField != null) {
//			for (String field : orderField.keySet()) {
//				MongoQuery.OrderType orderType = MongoQuery.OrderType.fromString(orderField.getString(field));
//				if (orderType == MongoQuery.OrderType.Unkown) {
//					throw new Rest400StatusException(ERROR_CODE.PARAM_VALID_ERROR, "order type unkown :" + orderField.getString(field));
//				}
//				TowTuple<String, MongoQuery.FieldType> fieldType = fieldMap.get(field);
//				if (fieldType != null) {
//					sorts.append(fieldType.first, orderType.nosql());
//				} else {
//					sorts.append("extend." + field, orderType.nosql());
//				}
//			}
//		}
//		return new MongoSearchQuery(offset, limit, filtersSet, querys.length > 0 ? Filters.and(querys) : new Document(), sorts);
//	}
//
//	/**
//	 * 根据条件查询返回结果
//	 *
//	 * @param key
//	 * @param value
//	 * @param dbName
//	 * @param collection
//	 * @return
//	 * @throws Exception
//	 * @Date 2018年6月7日 上午10:42:25
//	 */
//	public Document queryByKeyValue(String key, String value, String dbName, String collection) throws Exception {
//		Bson filters = Filters.eq(key, value);
//		Document document = MongoService.instance().queryOne(dbName, collection, filters);
//		if (document.isEmpty()) {
//			return new Document();
//		}
//		return document;
//	}
//
//	/**
//	 * 查询并返回filters条件下的数据，如果为空则返回空的document对象
//	 *
//	 * @param filters
//	 * @param dbName
//	 * @param collection
//	 * @return
//	 * @throws Exception
//	 * @Date 2018年6月20日 上午11:44:50
//	 */
//	public Document queryByKeyValue(Bson filters, String dbName, String collection) throws Exception {
//		Document document = MongoService.instance().queryOne(dbName, collection, filters);
//		if (document.isEmpty()) {
//			return new Document();
//		}
//		return document;
//	}
//
//	/**
//	 * 判断filters条件下的数据是否存在
//	 *
//	 * @param filters 过滤条件
//	 * @param dbName 数据库名称
//	 * @param collection 表名
//	 * @return true 存在 false 不存在
//	 * @throws Exception
//	 * @Date 2018年6月20日 上午11:44:53
//	 */
//	public boolean existByKeyValue(Bson filters, String dbName, String collection) throws Exception {
//		Document document = MongoService.instance().queryOne(dbName, collection, filters);
//		if (document.isEmpty()) {
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 根据条件统计数量
//	 *
//	 * @param filter
//	 * @param dbName
//	 * @param collectionName
//	 * @return
//	 * @Date 2018年6月20日 上午11:44:56
//	 */
//	public long countFilter(Bson filter, String dbName, String collectionName) {
//		MongoCollection<Document> collection = MongoService.instance().getCollection(dbName, collectionName);
//		long doc_num = collection.count(filter);
//		return doc_num;
//	}
//
//	/**
//	 * 检查数据是否有重复的
//	 *
//	 * @param str_list
//	 * @return true 有重复 false 无重复
//	 * @Date 2018年6月27日 下午2:23:33
//	 */
//	public static boolean checkRepeat(List<String> str_list) {
//		Set<String> set = new HashSet<String>();
//		for (String str : str_list) {
//			set.add(str);
//		}
//		if (set.size() != str_list.size()) {
//			return true;//有重复
//		} else {
//			return false;//不重复
//		}
//	}
//
//}
