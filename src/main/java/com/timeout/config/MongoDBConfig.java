package com.timeout.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
//@Configuration
@Setter
@Getter
//@Component
//@Order(3)
//@ConfigurationProperties(
//		prefix = "spring.data.mongodb.other")
public class MongoDBConfig implements CommandLineRunner {

	public String shardHost;
	public String username;
	public String password;
	public String database;
	public String replicaSet;
	public int connectionsPerHost;
	public int threadsAllowedToBlockForConnectionMultiplier;
	public int connectTimeout;
	public int maxWaitTime;
	public boolean autoConnectRetry;
	public boolean socketKeepAlive;
	public int socketTimeout;

	public Map<String, Integer> hosts = new ConcurrentHashMap<>();
	public static MongoClient mongoClient = new MongoClient();

	/**
	 * 解析数据库host
	 */
	public void funcParseMasterHost(String hostsString) {
		if (hostsString != null) {
			for (String hostPort : hostsString.split(",")) {
				hosts.put(hostPort.split(":")[0], Integer.parseInt(hostPort.split(":")[1]));
			}
		}
		if (hosts.isEmpty()) {
			throw new UnsupportedOperationException("host config is empty");
		}
	}

	@Override
	public void run(String... strings) throws Exception {
		MongoClientOptions.Builder optBuilder = MongoClientOptions.builder();
		optBuilder.connectionsPerHost(connectionsPerHost);
		optBuilder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
		optBuilder.connectTimeout(connectTimeout);
		optBuilder.maxWaitTime(maxWaitTime);
		optBuilder.socketKeepAlive(socketKeepAlive);
		optBuilder.socketTimeout(socketTimeout);
		optBuilder.sslEnabled(false);
		optBuilder.sslInvalidHostNameAllowed(false);

		funcParseMasterHost(shardHost);

		if (replicaSet != null && replicaSet.length() > 0) {
			optBuilder.requiredReplicaSetName(replicaSet);
		}
		List<ServerAddress> seeds = new ArrayList<>();
		List<MongoCredential> credentials = Arrays.asList(MongoCredential.createScramSha1Credential(username, database, password.toCharArray()));
		for (Map.Entry<String, Integer> entry : hosts.entrySet()) {
			seeds.add(new ServerAddress(entry.getKey(), entry.getValue()));
		}
		try {
			mongoClient = new MongoClient(seeds, credentials, optBuilder.build());
		} catch (Exception e) {
			System.exit(-1);
		}
		log.info("MongoDBConfig is init over");
	}

}
