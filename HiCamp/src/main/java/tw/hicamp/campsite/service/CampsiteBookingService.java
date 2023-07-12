package tw.hicamp.campsite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.hicamp.campsite.model.CampsiteBooking;
import tw.hicamp.campsite.model.CampsiteBookingRepository;

@Service
public class CampsiteBookingService {
    
	@Autowired
    private  CampsiteBookingRepository cbRepo;
       
    public void createBooking(CampsiteBooking campsiteBooking) {
    	cbRepo.save(campsiteBooking);
    }
    
    public CampsiteBooking updateBooking(Integer bookingNo, CampsiteBooking updatedBooking) {
        CampsiteBooking existingBooking = cbRepo.findById(bookingNo).orElse(null);
        if (existingBooking != null) {
            // 更新預訂相關屬性
            existingBooking.setBooker(updatedBooking.getBooker());
            existingBooking.setBookerPhone(updatedBooking.getBookerPhone());
            existingBooking.setBookingDate(updatedBooking.getBookingDate());
            existingBooking.setCheckinDate(updatedBooking.getCheckinDate());
            existingBooking.setCheckoutDate(updatedBooking.getCheckoutDate());
            existingBooking.setBookingCampspaceQuantity(updatedBooking.getBookingCampspaceQuantity());
            existingBooking.setBookingAmount(updatedBooking.getBookingAmount());

            // 儲存更新後的預訂
            return cbRepo.save(existingBooking);
        }
        return null;
    }
    
    public List<CampsiteBooking> findByBookerAndBookerPhone(String booker, String bookerPhone) {
        return cbRepo.findByBookerAndBookerPhone(booker, bookerPhone);
    }
}
