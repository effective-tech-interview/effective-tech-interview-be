package com.sparcs.teamf.sampleredis;

import org.springframework.data.repository.CrudRepository;

public interface PersonRedisRepository extends CrudRepository<Person, String> {

}
