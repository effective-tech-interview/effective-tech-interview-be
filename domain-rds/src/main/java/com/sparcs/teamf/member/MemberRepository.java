package com.sparcs.teamf.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Override
    @Modifying
    @Query("UPDATE Member m SET m.deletedAt = CURRENT_TIMESTAMP WHERE m.id = :id")
    void deleteById(@Param("id") Long id);
}
