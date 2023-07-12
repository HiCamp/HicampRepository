package tw.hicamp.activity.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.model.ActivityPictureRepository;

@Service
public class ActivityPictureService {

	@Autowired
	private ActivityPictureRepository actPicRepo;

//	insert
	public List<ActivityPicture> insertPictures(List<ActivityPicture> activityPictures) {
	    return actPicRepo.saveAll(activityPictures);
	}

//	select
	public List<ActivityPicture> findAllPictures() {
		return actPicRepo.findAll();
	}
	
	
	public Optional<ActivityPicture> findPicById(Integer activityPictureNo) {
	    return actPicRepo.findById(activityPictureNo);
	}
	
	public Integer findByActivityNo(Integer activityNo) {
		return actPicRepo.findFirstActivityPictureNoByActivityNo(activityNo);
	}

//	public ActivityPicture findPicById(Integer activityPictureNo) {
//		Optional<ActivityPicture> optional = actPicRepo.findById(activityPictureNo);
//		
//		if (optional.isPresent()) {
//			
//			return optional.get();
//		}
//		return null;
//	}

//	delete
	public void deletePicById(Integer activityPictureNo) {
		actPicRepo.deleteById(activityPictureNo);
	}
	
	public void deletePicByActNo(Integer activityNo) {
		actPicRepo.deleteById(activityNo);
	}

}
