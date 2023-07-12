package tw.hicamp.activity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.service.ActivityPictureService;

@Controller
public class ActivityPictureController {

	@Autowired
	private ActivityPictureService aPicService;
	
	// insert----------------------------------------------------------------------
	
	@GetMapping("/activity/picture")
	public String newPicture() {
		return "Activity/uploadPicture";
	}
	
	@PostMapping("activity/upload")
	public String uploadPicture(@RequestParam("activityFileName") String activityFileName, 
			@RequestParam("activityPicture") MultipartFile file) {
		
		try {
			List<ActivityPicture> activityPicture = (List<ActivityPicture>) new ActivityPicture();
			((ActivityPicture) activityPicture).setActivityFileName(activityFileName);
			((ActivityPicture) activityPicture).setActivityPicture(file.getBytes());
			
			aPicService.insertPictures(activityPicture);
			return "Activity/activityFindAll";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/Activity/error";
		}
	}
	
	
	// select----------------------------------------------------------------------
	
	
	
	// update----------------------------------------------------------------------
	
	
	// delete----------------------------------------------------------------------
	
}
