package com.sparcs.teamf.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
}
