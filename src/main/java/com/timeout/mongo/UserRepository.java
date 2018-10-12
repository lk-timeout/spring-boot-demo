package com.timeout.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.timeout.prjo.User;

public interface UserRepository extends MongoRepository<User, Long> {

	User findByName(String name);

	User findById(long id);

}
