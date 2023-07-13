package tw.hicamp.activity.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import tw.hicamp.activity.dto.ActivityDtoForBackEndPage;
import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.service.ActivityPeriodService;
import tw.hicamp.activity.service.ActivityPictureService;
import tw.hicamp.activity.service.ActivityService;
import tw.hicamp.activity.service.ActivitySignupService;

@Controller
public class ActivityController {

	@Autowired
	private ActivityService aService;

	@Autowired
	private ActivityPictureService actPicService;

	@Autowired
	private ActivityPeriodService actPeriodService;
	
	@Autowired
	private ActivitySignupService actSignupService;

// ==管理者頁面==========================================================================================

// 1.新增活動資料(含照片&期別)  http://localhost:8080/HiCamp/activity/insertActivity
// 2.查詢所有活動資訊(含照片&期別) http://localhost:8080/HiCamp/activity/allDataInBackEndPage
// 3.查詢單筆活動資訊(透過活動編號)  http://localhost:8080/HiCamp/activity/findActivityWithPicPeriodByActivityNo
// 4.查詢單筆活動資料(透過期別編號) http://localhost:8080/HiCamp/activity/

// --新增活動資訊 (含照片&期別) --------------------------------------------------------------------
	@PostMapping("/activity/insertActivity")
	public String InsertActivityPicPeriod(@RequestParam("activityType") String activityType,
			@RequestParam("activityName") String activityName,
			@RequestParam("activityLocation") String activityLocation,
			@RequestParam("activityInfo") String activityInfo, @RequestParam("activityQuota") Integer activityQuota,
			@RequestParam("activityPrice") Integer activityPrice,

			@RequestParam("activityPicture") MultipartFile[] files,

			@RequestParam("activityDepartureDate") Date activityDepartureDate,
			@RequestParam("activityReturnDate") Date activityReturnDate,
			@RequestParam("signupDeadline") Date signupDeadline, @RequestParam("signupQuantity") Integer signupQuantity,
			@RequestParam("activityPeriodQuota") Integer activityPeriodQuota,
			@RequestParam("activityPeriodPrice") Integer activityPeriodPrice,

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

		List<ActivityPeriod> activityPeriodList = new ArrayList<>();

		ActivityPeriod activityPeriod = new ActivityPeriod();

		activityPeriod.setActivityDepartureDate(activityDepartureDate);
		activityPeriod.setActivityReturnDate(activityReturnDate);
		activityPeriod.setSignupDeadline(signupDeadline);
		activityPeriod.setSignupQuantity(signupQuantity);
		activityPeriod.setActivityPeriodQuota(activityPeriodQuota);
		activityPeriod.setActivityPeriodPrice(activityPeriodPrice);
		activityPeriod.setActivity(activity);

		activityPeriodList.add(activityPeriod);

		activity.setActivityPictures(activityPictureList);
		activity.setActivityPeriods(activityPeriodList);

		aService.insert(activity);

		return "redirect:/activity/allDataInBackEndPage";
	}

// --查詢全部活動資訊 (含照片&期別) (DTO) --------------------------------------------------------------------
	@GetMapping("/activity/allDataInBackEndPage")
	public String findAllActivityWithPictureAndPeriod(Model model) {

		List<Activity> activityList = aService.findAllActivity();

		List<ActivityPeriod> activityPeriodList = actPeriodService.findAllActPeriods();

		List<ActivityDtoForBackEndPage> dtos = new ArrayList<>();

		for (Activity activity : activityList) {
			ActivityDtoForBackEndPage activityDto = new ActivityDtoForBackEndPage();
			activityDto.setActivityNo(activity.getActivityNo());
			activityDto.setActivityType(activity.getActivityType());
			activityDto.setActivityName(activity.getActivityName());

//			Integer activityNo = actPicService.findByActivityNo(activity.getActivityNo());
//			activityDto.setActivityPicNo(activityNo);

			for (ActivityPeriod activityPeriod : activityPeriodList) {
				if (activityPeriod.getActivityNo().equals(activity.getActivityNo())) {
					activityDto.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
					activityDto.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
					activityDto.setActivityReturnDate(activityPeriod.getActivityReturnDate());
					activityDto.setSignupDeadline(activityPeriod.getSignupDeadline());
					activityDto.setSignupQuantity(activityPeriod.getSignupQuantity());
					activityDto.setActivityPeriodQuota(activityPeriod.getActivityPeriodQuota());
					activityDto.setActivityPeriodPrice(activityPeriod.getActivityPeriodPrice());
					break;
				}
			}
			dtos.add(activityDto);
		}

		model.addAttribute("activity", dtos);

		return "activity/managerActivityLists";

	}

// --查詢單筆活動資訊(透過活動編號) --------------------------------------------------------------------
	@GetMapping("/activity/findActivityWithPicPeriodByActivityNo")
	public String findOneActivityWithPictureAndPeriod(@RequestParam("activityNo") Integer activityNo, Model model) {

		Activity activity = aService.findActivityByActId(activityNo);
		List<ActivityPicture> activityPictures = activity.getActivityPictures();
		List<ActivityPeriod> activityPeriods = activity.getActivityPeriods();

		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);
		model.addAttribute("activityPeriods", activityPeriods);

		return "activity/manageOneActivity";

	}

// --查詢單筆活動資料(透過期別編號) --------------------------------------------------------------------
	@GetMapping("/activity/findActivityWithPicPeriodByActivityPeriodNo")
	public String findOneActivityPeriodByPeriodNo(@RequestParam("activityPeriodNo") Integer activityPeriodNo,
			Model model) {

		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);

		Activity activity = activityPeriod.getActivity();
		List<ActivityPicture> activityPictures = activity.getActivityPictures();

		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);
		model.addAttribute("activityPeriod", activityPeriod);

		return "activity/manageOneActivity";

	}

	// 修改活動資料
	@ResponseBody
	@PutMapping("/activity/update")
	public String updateActivity(@RequestParam("activityNo")Integer activityNo,
								 @RequestParam("activityType")String activityType,
								 @RequestParam("activityName")String activityName,
								 @RequestParam("activityLocation")String activityLocation,
								 @RequestParam("activityInfo")String activityInfo,
								 @RequestParam("activityQuota")Integer activityQuota,
								 @RequestParam("activityPrice")Integer activityPrice
								 ,Model model) {

		Activity activity = aService.updateActivityById(activityNo, activityType, activityName,
				activityLocation, activityInfo, activityQuota,
				activityPrice);

		model.addAttribute("activity", activity);

		return "成功修改";
	}

//		修改活動 POSTMAN TEST  "activityDepartureDate":"2023-06-26T00:00:00+08:00" OK  HTML還沒寫
	@ResponseBody
	@PutMapping("/activity/updateActivityDto")
	public String updateActivityWithPeriod(@RequestBody ActivityDtoForBackEndPage activity, Model model) {
		//
//			aService.updateActivityDtoById(activity.getActivityNo(), activity.getActivityType(), activity.getActivityName(),
//					activity.getActivityLocation(), activity.getActivityInfo());

		actPeriodService.updateActivityPeriodById(activity.getActivityPeriodNo(), activity.getActivityNo(),
				activity.getActivityDepartureDate(), activity.getActivityReturnDate(), activity.getSignupDeadline(),
				activity.getSignupQuantity(), activity.getActivityPeriodPrice(), activity.getActivityPeriodQuota());

		model.addAttribute("activity", activity);

		return "activity/manageOneActivity";
	}

//		刪除活動資料+照片
	@DeleteMapping("/activity/delete")
	public String deleteActivity(@RequestParam("activityNo") Integer activityNo) {
		aService.deleteById(activityNo);
		actPicService.deletePicByActNo(activityNo);
		return "activity/activityFindAll";
	}

// ==使用者頁面==========================================================================================

// --顯示全部活動+第一張照片 (DTO) --------------------------------------------------------------------
	@GetMapping("/activity/memberActivityHomePage")
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
			
			List<ActivityPeriod> activityPeriods = activity.getActivityPeriods();
			if(activityPeriods != null && !activityPeriods.isEmpty()) { 
			 for (ActivityPeriod activityPeriod : activityPeriods) {
		            activityDTO.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
		            activityDTO.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
		            activityDTO.setActivityReturnDate(activityPeriod.getActivityReturnDate());
		            activityDTO.setSignupDeadline(activityPeriod.getSignupDeadline());
		        	int totalOrders = actSignupService.findTotalOrdersByActivityPeriodNo(activityPeriod.getActivityPeriodNo());
				    int remainingQuota = activity.getActivityQuota() - totalOrders;
				    if (remainingQuota < 0) {
				        remainingQuota = 0;
				    }
				    activityDTO.setRemainingQuota(remainingQuota);
			    }
			 
			Integer activityNo = actPicService.findByActivityNo(activity.getActivityNo());

			actPicService.findByActivityNo(activity.getActivityNo());
			activityDTO.setActivityPicNo(activityNo);

			dtos.add(activityDTO);
			}
		}
		model.addAttribute("activity", dtos);
		return "activity/memberActivityHomePage";
	}

// --顯示單筆資料 (前台) --------------------------------------------------------------------


	@GetMapping("/activity/findByIdOnFrontpage")
	public String findActivityWithPictureAndPeriod(@RequestParam("activityNo") Integer activityNo,  
													Model model) {
		Activity activity = aService.findActivityByActId(activityNo);
		List<ActivityPicture> activityPictures = activity.getActivityPictures();
		List<ActivityPeriod> activityPeriods = activity.getActivityPeriods();
		List<Integer> quota = new ArrayList<>();
		for (ActivityPeriod period : activityPeriods) {
			int totalquota = actSignupService.findTotalOrdersByActivityPeriodNo(period.getActivityPeriodNo());
			quota.add((period.getActivityPeriodQuota() - totalquota));
		}
		
		System.out.println("quota:"+quota);
		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);
		model.addAttribute("activityPeriods", activityPeriods);
		model.addAttribute("quotas", quota);
		return "activity/memberActivityPreview";
	}
	
	
//	@GetMapping("/activity/findByPeriodIdOnFrontpage")
//	public String findActivityWithPictureAndPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo,
//			Model model) {
//
//		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);
//		if (activityPeriod != null) {
//			Activity activity = activityPeriod.getActivity();
//			if (activity != null) {
//				List<ActivityPicture> activityPictures = activity.getActivityPictures();
//
//				model.addAttribute("activity", activity);
//				model.addAttribute("activityPictures", activityPictures);
//				return "activity/previewpage";
//			}
//		}
//		return "redirect:/activity/frontpage";
//	}

	@GetMapping("activity/testperiod")
	public String findActivityByActivityPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo,
			Model model) {

		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);

		model.addAttribute("activityPeriod", activityPeriod);
		return "activity/testActivityPeriod";
	}

// ==新增==========================================================================================

// 新增活動資料+活動照片 整理後可刪除
//	@PostMapping("/activity/insertData")
//	public String InsertActivity(
//			@RequestParam("activityType") String activityType,
//			@RequestParam("activityName") String activityName,
//			@RequestParam("activityLocation") String activityLocation,
//			@RequestParam("activityInfo") String activityInfo, 
//			@RequestParam("activityQuota") Integer activityQuota,
//			@RequestParam("activityPrice") Integer activityPrice,
//			@RequestParam("activityPicture") MultipartFile[] files, 
//			Model model) throws IOException {
//		
//		Activity activity = new Activity();
//
//		activity.setActivityType(activityType);
//		activity.setActivityName(activityName);
//		activity.setActivityLocation(activityLocation);
//		activity.setActivityInfo(activityInfo);
//		activity.setActivityQuota(activityQuota);
//		activity.setActivityPrice(activityPrice);
//
//		List<ActivityPicture> activityPictureList = new ArrayList<>();
//
//		for (MultipartFile file : files) {
//			ActivityPicture actPicture = new ActivityPicture();
//			byte[] actPictureByte = file.getBytes();
//			actPicture.setActivityFileName(file.getOriginalFilename());
//			actPicture.setActivityPicture(actPictureByte);
//			actPicture.setActivity(activity);
//
//			activityPictureList.add(actPicture);
//		}
//
//		activity.setActivityPictures(activityPictureList);
//
//		aService.insert(activity);
//
//		return "redirect:/activity/showdata";
//	}

// ==查詢==========================================================================================

// 查詢所有活動資料(不含照片) 整理後可刪除
	@GetMapping("/activity/showdata")
	public String findAllActivities(Model model) {
		List<Activity> activityList = aService.findAllActivity();
		model.addAttribute("activity", activityList);
		return "activity/activityFindAll";
	}

// 查詢單筆資料  整理後可刪除
	@GetMapping("/activity/findByNo")
	public String findActivityWithPictures(@RequestParam("activityNo") Integer activityNo, Model model) {
		Activity activity = aService.findActivityByActId(activityNo);
		List<ActivityPicture> activityPictures = activity.getActivityPictures();

		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);

		return "activity/activityFindById";
	}

}