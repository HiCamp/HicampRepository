package tw.hicamp.activity.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityPictureRepository extends JpaRepository<ActivityPicture, Integer> {

	@Query(value = "SELECT TOP 1 activityPictureNo FROM activityPicture WHERE activityNo = :activityNo ORDER BY activityPictureNo ASC", nativeQuery = true)
	Integer findFirstActivityPictureNoByActivityNo(@Param("activityNo") Integer activityNo);

}
