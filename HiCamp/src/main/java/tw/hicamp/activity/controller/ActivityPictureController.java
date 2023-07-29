package tw.hicamp.activity.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.service.ActivityPictureService;
import tw.hicamp.activity.service.ActivityService;

@Controller
public class ActivityPictureController {

	@Autowired
	private ActivityService aService;

	@Autowired
	private ActivityPictureService actPicService;
	
// ==管理者頁面 Outline ======================================================================================

// 1.上傳多張照片(更新頁面編輯圖片)  http://localhost:8080/HiCamp/activity/insertpic
// 2.顯示所有照片(透過ID搜尋圖片, 顯示於彈跳視窗) http://localhost:8080/HiCamp/activity/showPictures
// 3.刪除單張照片  http://localhost:8080/HiCamp/activity/deletepic

// ==========================================================================================================	

// 1. 上傳照片(後台更新頁面編輯圖片) -----------------------------------------------------------------------------
	@PostMapping("/activity/insertpic")
	public String uploadPicture(@RequestParam("activityNo") Integer activityNo,
			@RequestParam("activityPicture") MultipartFile[] files, Model model) {

		Activity activity = aService.findActivityById(activityNo);

		if (activity == null) {
			return "查無此筆資料";
		}
		
		try {
			List<ActivityPicture> activityPictureList = new ArrayList<>();

			for (MultipartFile file : files) {
				ActivityPicture actPicture = new ActivityPicture();
				actPicture.setActivityNo(activityNo);
				actPicture.setActivityFileName(file.getOriginalFilename());
				actPicture.setActivityPicture(file.getBytes());
				actPicture.setActivity(activity);
				activityPictureList.add(actPicture);
			}

			actPicService.insertPictures(activityPictureList);
			aService.findActivityById(activityNo);

			model.addAttribute("activity", activity);
			model.addAttribute("activityPictures", activityPictureList);

			return "redirect:/activity/findActivityWithPicPeriodByActivityNo?activityNo=" + activityNo;
		} catch (IOException e) {
			e.printStackTrace();
			return "上傳失敗";
		}
	}

// 2. 顯示照片(透過ID搜尋圖片, 顯示於彈跳視窗)-----------------------------------------------------------------------
// 先透過ActivityNo找圖片ID
	@ResponseBody
	@GetMapping("/activity/activityPicNo")
	public List<Integer> getActPicIdByActivityId(@RequestParam("activityNo") Integer activityNo) {
		Activity activity = aService.findActivityByActId(activityNo);
		List<ActivityPicture> ListActivityPictures = activity.getActivityPictures();

		List<Integer> actPictureNos = new LinkedList<>();
		for (ActivityPicture actPicture : ListActivityPictures) {
			Integer onePicNo = actPicture.getActivityPictureNo();
			actPictureNos.add(onePicNo);
		}
		return actPictureNos;
	}

// 再透過圖片ID顯示圖片
	@GetMapping("/activity/showPictures")
	public ResponseEntity<byte[]> getActivityPictures(@RequestParam("activityPictureNo") Integer activityPictureNo) {
		Optional<ActivityPicture> optional = actPicService.findPicById(activityPictureNo);
		if (optional.isPresent()) {
			ActivityPicture actPictures = optional.get();
			byte[] activityPictures = actPictures.getActivityPicture();

			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(activityPictures);
		}
		return null;
	}

//	3. 刪除單張照片---------------------------------------------------------------------------------------------------
	@ResponseBody
	@DeleteMapping("/activity/deletepic")
	public String deleteActivityPicture(@RequestParam("activityPictureNo") Integer activityPictureNo) {
		actPicService.deletePicById(activityPictureNo);
		return "刪除成功";
	}
	
}
