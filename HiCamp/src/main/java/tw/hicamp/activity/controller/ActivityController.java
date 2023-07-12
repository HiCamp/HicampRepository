package tw.hicamp.activity.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.LinkedList;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tw.hicamp.activity.dto.ActivityDto;
import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.service.ActivityPictureService;
import tw.hicamp.activity.service.ActivityService;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService aService;

	@Autowired
	private ActivityPictureService actPicService;

// ==新增==========================================================================================

// 新增活動資料+活動照片
	@PostMapping("/activity/insertData")
	public String InsertActivity(
			@RequestParam("activityType") String activityType,
			@RequestParam("activityName") String activityName,
			@RequestParam("activityLocation") String activityLocation,
			@RequestParam("activityInfo") String activityInfo, 
			@RequestParam("activityQuota") Integer activityQuota,
			@RequestParam("activityPrice") Integer activityPrice,
			@RequestParam("activityPicture") MultipartFile[] files, 
			Model model) throws IOException {

		Activity activity = new Activity();

		activity.setActivityType(activityType);
		activity.setActivityName(activityName);
		activity.setActivityLocation(activityLocation);
		activity.setActivityInfo(activityInfo);
		activity.setActivityQuota(activityQuota);
		activity.setActivityPrice(activityPrice);

		List<ActivityPicture> activityPictureList = new ArrayList<>();

		for (MultipartFile file : files) {
			ActivityPicture actPicture = new ActivityPicture();
			byte[] actPictureByte = file.getBytes();
			actPicture.setActivityFileName(file.getOriginalFilename());
			actPicture.setActivityPicture(actPictureByte);
			actPicture.setActivity(activity);

			activityPictureList.add(actPicture);
		}

		activity.setActivityPictures(activityPictureList);

		aService.insert(activity);

		return "redirect:/activity/showdata";
	}

// 上傳照片(更新頁面編輯圖片)
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

			return "redirect:/activity/findByNo?activityNo=" + activityNo;
		} catch (IOException e) {
			e.printStackTrace();
			return "上傳失敗";
		}
	}

// ==查詢==========================================================================================

// 查詢所有活動資料(不含照片)
	@GetMapping("/activity/showdata")
	public String findAllActivities(Model model) {
		List<Activity> activityList = aService.findAllActivity();
		model.addAttribute("activity", activityList);
		return "activity/activityFindAll";
	}

// 顯示照片(透過ID搜尋圖片, 顯示於彈跳視窗)
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

// 查詢單筆資料
	@GetMapping("/activity/findByNo")
	public String findActivityWithPictures(@RequestParam("activityNo") Integer activityNo, Model model) {
		Activity activity = aService.findActivityByActId(activityNo);
		List<ActivityPicture> activityPictures = activity.getActivityPictures();

		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);

		return "activity/activityFindById";
	}
	
//	查詢活動+第一張照片 (前台dto)
	@GetMapping("/activity/alldata")
	public String showActivityWithPictures(Model model) {
		List<Activity> activityList = aService.findAllActivity();

		List<ActivityDto> dtos = new ArrayList<>();

		for (Activity activity : activityList) {
			ActivityDto activityDTO = new ActivityDto();
			activityDTO.setActivityNo(activity.getActivityNo());
			activityDTO.setActivityType(activity.getActivityType());
			activityDTO.setActivityName(activity.getActivityName());
			activityDTO.setActivityInfo(activity.getActivityInfo());
			activityDTO.setActivityQuota(activity.getActivityQuota());
			activityDTO.setActivityPrice(activity.getActivityPrice());

			Integer activityNo = actPicService.findByActivityNo(activity.getActivityNo());

			actPicService.findByActivityNo(activity.getActivityNo());
			activityDTO.setActivityPicNo(activityNo);

			dtos.add(activityDTO);
		}
		model.addAttribute("activity", dtos);
		return "activity/frontPage";
	}

	// 查詢單筆資料 (前台)
	@GetMapping("/activity/findByIdOnFrontpage")
	public String findActivityWithPicturesFront(@RequestParam("activityNo") Integer activityNo, Model model) {
		Activity activity = aService.findActivityByActId(activityNo);
		List<ActivityPicture> activityPictures = activity.getActivityPictures();

		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);

		return "activity/previewPage";
	}

// ==update==========================================================================================

// 修改活動資料
	@ResponseBody
	@PutMapping("/activity/update")
	public String updateActivity(@RequestBody Activity activity, Model model) {

		aService.updateActivityById(activity.getActivityNo(), activity.getActivityType(), activity.getActivityName(),
				activity.getActivityLocation(), activity.getActivityInfo(), activity.getActivityQuota(),
				activity.getActivityPrice());

		model.addAttribute("activity", activity);

		return "redirect:activity/findByNo";
	}

// ==delete==========================================================================================

//	刪除活動資料+照片
	@DeleteMapping("/activity/delete")
	public String deleteActivity(@RequestParam("activityNo") Integer activityNo) {
		aService.deleteById(activityNo);
		actPicService.deletePicByActNo(activityNo);
		return "activity/activityFindAll";
	}

//	刪除照片
	@DeleteMapping("/activity/deletepic")
	public String deleteActivityPicture(@RequestParam("activityPictureNo") Integer activityPictureNo) {
		actPicService.deletePicById(activityPictureNo);
		return "刪除成功";
	}

// ==Paging==========================================================================================

}