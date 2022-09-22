package jpalogin.login.repository;

import jpalogin.login.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId = :loginId")
    Optional<Member> findUser(@Param("loginId") String loginId);

    Optional<Member> findByLoginId(String loginId);
}
