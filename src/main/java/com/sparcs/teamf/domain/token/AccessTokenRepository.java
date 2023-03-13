package com.sparcs.teamf.domain.token;

import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
}
