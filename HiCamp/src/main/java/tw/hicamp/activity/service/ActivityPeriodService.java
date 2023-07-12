package tw.hicamp.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivityPicture;
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

	// 會用到ㄇ?
//	public Activity findLatest() {
//		return actRepo.findFirstByOrderByAddedDesc();
//	}

//	update----------------------------------------------------------------------
	@Transactional
	public ActivityPeriod updateActivityById(Integer activityPeriodNo, Integer activityNo, Date activityDepartureDate,
			Date activityReturnDate, Date signupDeadline, Integer activityQuota, Integer activityPrice) {
		Optional<ActivityPeriod> optional = actPeriodRepo.findById(activityPeriodNo);
		if (optional.isPresent()) {
			ActivityPeriod activityPeriod = optional.get();
			
			activityPeriod.setActivityDepartureDate(activityDepartureDate);
			activityPeriod.setActivityReturnDate(activityReturnDate);
			activityPeriod.setSignupDeadline(signupDeadline);
			activityPeriod.setActivityPeriodQuota(activityQuota);
			activityPeriod.setActivityPeriodPrice(activityPrice);
			
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
