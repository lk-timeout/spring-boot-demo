package com.timeout.springbootdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeout.Application;
import com.timeout.mongo.UserRepository;
import com.timeout.prjo.User;

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

	@Autowired
	private MongoTemplate MongoTemplate;

	@Autowired
	private UserRepository userRepository;

//	@Before
//	public void setUp() {
//		userRepository.deleteAll();
//	}

	@Test
	public void test() throws Exception {

		// 创建三个User，并验证User总数
		userRepository.save(new User(1L, "didi", 30));
		userRepository.save(new User(2L, "mama", 40));
		userRepository.save(new User(3L, "kaka", 50));
		MongoTemplate.insert(new User(4L, "LK", 11));
//		Assert.assertEquals(3, userRepository.findAll().size());

//		// 删除一个User，再验证User总数
//		Query query = new Query();
//		query.addCriteria(Criteria.where("id").is(4L));
//		query.fields();
//		User u = MongoTemplate.findOne(query, User.class);
//		MongoCollection<Document> collection = MongoTemplate.getCollection("user");

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

	}
}
