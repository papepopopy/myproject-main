package com.spring.myproject.repository;

import com.spring.myproject.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 지연 로딩시 사용
    @EntityGraph(attributePaths = "roleSet")
    Member findByEmail(String email);
}
