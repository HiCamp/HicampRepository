package tw.hicamp.activity.service;

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
import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.model.ActivityPictureRepository;
import tw.hicamp.activity.model.ActivityRepository;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository actRepo;

	@Autowired
	private ActivityPictureRepository actPicRepo;

	// insert----------------------------------------------------------------------
	public Activity insert(Activity activity) {
		return actRepo.save(activity);
	}

//	public Activity insertAct(Activity activity, ActivityPicture activityPicture) {
////		activity.setActivityPictures(new HashSet<>(Collections.singletonList(activityPicture)));
////	    Set<ActivityPicture> pictures = new HashSet<>();
////	    pictures.add(activityPicture);
////	    activity.setActivityPictures(pictures);
//		activityPicture.setActivity(activity);
//		actRepo.save(activity);
//		actPicRepo.save(activityPicture);
//		return activity;
//	}
	 
//	select----------------------------------------------------------------------
	public List<Activity> findAllActivity() {
		return actRepo.findAll();
	}

	public Activity findActivityById(Integer activityNo) {
		Optional<Activity> optional = actRepo.findById(activityNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Activity findActivityByActId(Integer activityNo) {
		return actRepo.getReferenceById(activityNo);
	}

	// 會用到ㄇ?
//	public Activity findLatest() {
//		return actRepo.findFirstByOrderByAddedDesc();
//	}

//	update----------------------------------------------------------------------
	@Transactional
	public Activity updateActivityById(Integer activityNo, String activityType, String activityName,
			String activityLocation, String activityInfo, Integer activityQuota, Integer activityPrice) {
		Optional<Activity> optional = actRepo.findById(activityNo);
		if (optional.isPresent()) {
			Activity activity = optional.get();
			activity.setActivityType(activityType);
			activity.setActivityName(activityName);
			activity.setActivityLocation(activityLocation);
			activity.setActivityInfo(activityInfo);
			activity.setActivityQuota(activityQuota);
			activity.setActivityPrice(activityPrice);
			return activity;
		}
		return null;
	}

//	delete----------------------------------------------------------------------
	public void deleteById(Integer activityNo) {
		actRepo.deleteById(activityNo);
	}

//	Paging----------------------------------------------------------------------
	public Page<Activity> findByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "activityNo");
		Page<Activity> page = actRepo.findAll(pgb);
		return page;
	}
	
	public ActivityService(ActivityRepository actRepo, ActivityPictureRepository actPicRepo) {
	this.actRepo = actRepo;
	this.actPicRepo = actPicRepo;
}
	public ActivityService() {
	}

}
