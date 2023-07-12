package tw.hicamp.member.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	Member findByMemberEmail(String email);
	
	@Query(value = "SELECT MONTH(memberSignupDate) AS month, COUNT(*) AS count FROM member WHERE YEAR(memberSignupDate) = :year GROUP BY MONTH(memberSignupDate) ORDER BY MONTH(memberSignupDate)", nativeQuery = true)
	List<Map<Integer, Integer>> findSignupDateByMemberSignupDate(@Param("year") int year);

}
