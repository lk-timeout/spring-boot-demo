package com.timeout.thread;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.timeout.redis.RedisMsgPubSubListener;

import redis.clients.jedis.Jedis;

public class TestSubscribe extends Thread {

	@Resource
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public void run() {
//		Jedis jedis = new Jedis();
//		RedisMsgPubSubListener listener = new RedisMsgPubSubListener();
//		jedis.subscribe(listener, "redisChatTest");
		redisTemplate.convertAndSend("pub", "redisChatTest");
		System.out.println("不阻塞");
	}
}
