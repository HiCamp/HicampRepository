package tw.hicamp.activity.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivitySignup;
import tw.hicamp.activity.model.ActivitySignupRepository;

@Service
public class ActivitySignupService {

	@Autowired
	private ActivitySignupRepository actSignupRepo;

	public ActivitySignup insertActivitySignup(ActivitySignup activitySignup) {
		return actSignupRepo.save(activitySignup);
	}

	public List<ActivitySignup> findAllSignupOrders() {
		return actSignupRepo.findAll();
	}

	public ActivitySignup findActivitySignupOrdersBySignupNo(Integer activitySignupNo) {
		Optional<ActivitySignup> optional = actSignupRepo.findById(activitySignupNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<ActivitySignup> findSignupOrdersBymemberNo(Integer memberNo) {
		List<ActivitySignup> signupOrders = actSignupRepo.findByMemberNo(memberNo);
		return signupOrders;
	}

	@Transactional
	public ActivitySignup updateActivitySignupOrderByNo(Integer activitySignupNo, Integer memberNo,
			Integer activityPeriodNo, Date signupDate, Integer signupQuantity, Integer signupTotalAmount,
			String signupPaymentStatus) {
		Optional<ActivitySignup> optional = actSignupRepo.findById(activitySignupNo);

		if (optional.isPresent()) {
			ActivitySignup activitySignup = optional.get();

//			activitySignup.setActivitySignupNo(activitySignupNo);
			activitySignup.setMemberNo(memberNo);
			activitySignup.setActivityPeriodNo(activityPeriodNo);
			activitySignup.setSignupDate(signupDate);
			activitySignup.setSignupTotalAmount(signupTotalAmount);
			activitySignup.setSignupPaymentStatus(signupPaymentStatus);
			activitySignup.setSignupQuantity(signupQuantity);

			return activitySignup;
		}
		return null;
	}

//	透過期別查詢訂單
	public int findTotalOrdersByActivityPeriodNo(Integer activityPeriodNo) {
		List<ActivitySignup> activitySignupList = actSignupRepo.findByActivityPeriodNo(activityPeriodNo);

		int totalOrders = 0;
		for (ActivitySignup activitySignup : activitySignupList) {
			totalOrders += activitySignup.getSignupQuantity();
		}
		return totalOrders;
	}

//	透過日期查詢
	public List<ActivitySignup> findSignupOrderBySignupDate(Date signupDate, int memberNo) {
//		 List<ActivitySignup> activitySignupList = actSignupRepo.findSingupOrderBySignupDate(signupDate);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(signupDate);
		calendar.add(Calendar.YEAR, -1);
		Date startDate = calendar.getTime();
		List<ActivitySignup> activitySignupList = actSignupRepo.findBySignupDateBetween(startDate, signupDate);

		return activitySignupList;
	}

//	public List<ActivitySignup> findAllSignupOrdersByMemberNo(Integer memberNo){
//		 List<ActivitySignup> activitySignupList = actSignupRepo.findAllSignupOrdersById(memberNo);
//		 return activitySignupList;
//	}

	public void deleteActSignupOrderBySignupNo(Integer activitySignupNo) {
		actSignupRepo.deleteById(activitySignupNo);
	}
	
	public List<ActivitySignup> findByKeyword(String keyword, int memberNo) {
	    return actSignupRepo.findByKeyword("%" + keyword + "%", memberNo);
	}
	
	
	//chart
    public List<Map<String, Object>> getSignupDataPerMonth() {
        return actSignupRepo.findSignupDataGroupByMonth();
    }
    

    public List<Map<String, Object>> getSignupDataByType() {
        return actSignupRepo.findSignupDataGroupByType();
    }
	
	
	
	

	public ActivitySignupService() {
	}

}
