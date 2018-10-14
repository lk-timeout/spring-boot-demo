package com.timeout.springbootdemo;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeout.Application;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootTest(classes = Application.class)
public class SpringBootDemoApplicationTests {

//	@Test
//	public void contextLoads() {
//	}
//
//	@Autowired
//	private UserMapper userMapper;
//
//	@Test
//	@Rollback
//	public void findByName() throws Exception {
//		userMapper.insert("AAA", 20);
//		User u = userMapper.findByName("AAA");
//		Assert.assertEquals(20, u.getAge().intValue());
//	}

//	@Autowired
//	private MongoTemplate MongoTemplate;

//	@Autowired
//	private UserRepository userRepository;

//	@Before
//	public void setUp() {
//		userRepository.deleteAll();
//	}
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void test() throws Exception {

//		System.out.println(MongoTemplate.getDb().toString());
		// 创建三个User，并验证User总数
//		userRepository.save(new User(1L, "didi", 30));
//		userRepository.save(new User(2L, "mama", 40));
//		userRepository.save(new User(3L, "kaka", 50));
//		MongoTemplate.insert(new User(4L, "LK", 11));

//		MongoTemplate.insert(new User(5L, "LK", 12));
//		Assert.assertEquals(3, userRepository.findAll().size());

//		// 删除一个User，再验证User总数
//		Query query = new Query();
//		query.addCriteria(Criteria.where("id").is(5L));
//		query.fields();
//		User u = MongoTemplate.findOne(query, User.class);
//		System.out.println(u.toString());
//		MongoCollection<Document> collection = MongoTemplate.getCollection("aaa");
//		Bson fite = Filters.eq("_id", 5L);
//		Document document = collection.find(fite).first();
//		System.out.println(document.toJson());
//		Document document = new Document();
//		document.put("_id", "asdas");
//		document.put("name", "asdas");
//		document.put("age", 22);
//		MongoService.instance().insertIngore("lik", "aa2a", document);
//		Bson fite = Filters.eq("_id", "asdas");
//		Document document2 = MongoService.instance().queryOne("lik", "aaa", fite);
//		System.out.println(document2.toJson());

//		User u = userRepository.findById(4L);
//		System.out.println(u.toString());
//		userRepository.delete(u);
//		Assert.assertEquals(2, userRepository.findAll().size());

		// 删除一个User，再验证User总数
//		User u = userRepository.findByName("mama");
//		System.out.println(u.toString());
//		userRepository.delete(u);
//		Assert.assertEquals(1, userRepository.findAll().size());

//		Document document = new Document();
//		document.put("_id", "jlkjljkljl");
//		document.put("name", "asdasd");
//		MongoService.instance().insertIngore(null, "user", document);

		// 保存字符串
//		stringRedisTemplate.opsForValue().set("aaa", "111");
//		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
		redisTemplate.opsForValue().set("bbbb", "123");
		redisTemplate.opsForValue().set("aa", "2222", 1000, TimeUnit.SECONDS);
//		stringRedisTemplate.opsForValue().se
		System.out.println(redisTemplate.opsForValue().get("bbbb"));

	}
}
