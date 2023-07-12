package tw.hicamp.campsite.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampsiteBookingRepository extends JpaRepository<CampsiteBooking, Integer> {
	 List<CampsiteBooking> findByBookerAndBookerPhone(String booker, String bookerPhone);
}
