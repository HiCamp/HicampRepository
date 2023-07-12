package tw.hicamp.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivityPictureRepository;
import tw.hicamp.activity.model.ActivityPeriodRepository;
import tw.hicamp.activity.model.ActivityRepository;

@Service
public class ActivityPeriodService {

	@Autowired
	private ActivityRepository actRepo;

	@Autowired
	private ActivityPictureRepository actPicRepo;
	
	@Autowired
	private ActivityPeriodRepository actPeriodRepo;

//  insert----------------------------------------------------------------------
	public List<ActivityPeriod> insert(List<ActivityPeriod> activityPeriods) {
		return actPeriodRepo.saveAll(activityPeriods);
	}
	
	public ActivityPeriod insertOnePeriod(ActivityPeriod activityPeriod) {
		return actPeriodRepo.save(activityPeriod);
	}
	
//	select----------------------------------------------------------------------
	
	public List<ActivityPeriod> findAllActPeriods() {
		return actPeriodRepo.findAll();
	}

	public ActivityPeriod findActPeriodById(Integer activityPeriodNo) {
		Optional<ActivityPeriod> optional = actPeriodRepo.findById(activityPeriodNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public ActivityPeriod findActPeriodByActId(Integer activityNo) {
		return actPeriodRepo.getReferenceById(activityNo);
	}

//	update----------------------------------------------------------------------
	@Transactional
	public ActivityPeriod updateActivityPeriodById(Integer activityPeriodNo, Integer activityNo, Date activityDepartureDate,
			Date activityReturnDate, Date signupDeadline, Integer activityPeriodQuota, Integer activityPeriodPrice, Integer activitySignupQuantity) {
		Optional<ActivityPeriod> optional = actPeriodRepo.findById(activityPeriodNo);
		if (optional.isPresent()) {
			ActivityPeriod activityPeriod = optional.get();
			
			activityPeriod.setActivityDepartureDate(activityDepartureDate);
			activityPeriod.setActivityReturnDate(activityReturnDate);
			activityPeriod.setSignupDeadline(signupDeadline);
			activityPeriod.setActivityPeriodQuota(activityPeriodQuota);
			activityPeriod.setActivityPeriodPrice(activityPeriodPrice);
			activityPeriod.setSignupQuantity(activitySignupQuantity);
			
			return activityPeriod;
		}
		return null;
	}

//	delete----------------------------------------------------------------------
	public void deleteActPeriodById(Integer activityPeriodNo) {
		actPeriodRepo.deleteById(activityPeriodNo);
	}


	public ActivityPeriodService() {
	}

}
