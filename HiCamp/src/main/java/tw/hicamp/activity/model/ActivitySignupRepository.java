package tw.hicamp.activity.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivitySignupRepository extends JpaRepository<ActivitySignup, Integer> {

	List<ActivitySignup> findByActivityPeriodNo(Integer activityPeriodNo);

	List<ActivitySignup> findSingupOrderBySignupDate(Date signupDate);

	List<ActivitySignup> findByMemberNo(Integer memberNo);

	List<ActivitySignup> findBySignupDateBetween(Date startDate, Date signupDate);

	@Query(value = "SELECT * FROM activitySignup WHERE " +
	        "signupPaymentStatus LIKE CONCAT('%', :keyword, '%') OR " +
	        "signupDate LIKE CONCAT('%', :keyword, '%') OR " +
	        "signupTotalAmount LIKE CONCAT('%', :keyword, '%') OR " +
	        "signupQuantity LIKE CONCAT('%', :keyword, '%') AND "
	        + "memberNo =:memberNo", nativeQuery = true)
	List<ActivitySignup> findByKeyword(@Param("keyword") String keyword, @Param("memberNo")int memberNo);
}
