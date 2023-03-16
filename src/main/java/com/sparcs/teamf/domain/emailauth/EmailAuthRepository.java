package com.sparcs.teamf.domain.emailauth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    Optional<EmailAuth> findFirstByEmailAndEventOrderByCreatedDateDesc(String email, Event event);

    void deleteAllByEmail(String email);
}
