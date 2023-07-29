package tw.hicamp.activity.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

// ==管理者頁面 Outline ======================================================================================

// 1.新增活動資料(含照片&期別)  http://localhost:8080/HiCamp/activity/insertActivity
// 2.查詢所有活動資訊(含照片&期別) http://localhost:8080/HiCamp/activity/allDataInBackEndPage
// 3.查詢單筆活動資訊(透過活動編號)  http://localhost:8080/HiCamp/activity/findActivityWithPicPeriodByActivityNo?ActivityNo=
// 4.查詢單筆活動資料(透過期別編號查詢) http://localhost:8080/HiCamp/activity/findActivityWithPicPeriodByActivityPeriodNo?ActivityPeriodNo=
// 5.修改單筆活動資料 http://localhost:8080/HiCamp/activity/updateActivity

// ==========================================================================================================	

// 1. 新增活動資訊 (含照片&期別) ---------------------------------------------------------------------------
	@PostMapping("/activity/insertActivity")
	public String InsertActivityWithPictureAndPeriod(@RequestParam("activityType") String activityType,
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

// 2. 查詢全部活動資訊 (含照片&期別) (BackEnd-DTO) ----------------------------------------------------------
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

// 3. 查詢單筆活動資訊 (透過活動編號查詢) --------------------------------------------------------------------
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

// 4. 查詢單筆活動資訊 (透過期別編號查詢) --------------------------------------------------------------------
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

// 5. 修改單筆活動資料 ----------------------------------------------------------------------------------------
	@ResponseBody
	@PutMapping("/activity/updateActivity")
	public String updateActivity(@RequestParam("activityNo") Integer activityNo,
			@RequestParam("activityType") String activityType, @RequestParam("activityName") String activityName,
			@RequestParam("activityLocation") String activityLocation,
			@RequestParam("activityInfo") String activityInfo, @RequestParam("activityQuota") Integer activityQuota,
			@RequestParam("activityPrice") Integer activityPrice, Model model) {

		Activity activity = aService.updateActivityById(activityNo, activityType, activityName, activityLocation,
				activityInfo, activityQuota, activityPrice);

		model.addAttribute("activity", activity);

		return "成功修改";
	}

// ==使用者頁面 Outline ==========================================================================================

// 1.顯示所有活動資料(含照片)  http://localhost:8080/HiCamp/activity/memberActivityHomePage
// 2.顯示單筆活動資訊(含照片&期別) http://localhost:8080/HiCamp/activity/findByIdOnFrontpage

// ==============================================================================================================	

// 1. 顯示全部活動+第一張照片 (ActivityDTO) --------------------------------------------------------------------
	@GetMapping("/activity/memberActivityHomePage")
	public String showActivityWithPictures(Model model) {
		List<Activity> activityList = aService.findAllActivity();

		List<ActivityDto> dtos = new ArrayList<>();

		for (Activity activity : activityList) {
			ActivityDto activityDTO = new ActivityDto(activity);

			List<ActivityPeriod> activityPeriods = activity.getActivityPeriods();
			if (activityPeriods != null && !activityPeriods.isEmpty()) {
				for (ActivityPeriod activityPeriod : activityPeriods) {
					activityDTO.ActivityDtoAddPeriod(activityPeriod);

//					計算剩餘名額=報名名額-訂單數量
					int totalOrders = actSignupService
							.findTotalOrdersByActivityPeriodNo(activityPeriod.getActivityPeriodNo());
					int remainingQuota = activity.getActivityQuota() - totalOrders;
					if (remainingQuota < 0) {
						remainingQuota = 0;
					}
					activityDTO.setRemainingQuota(remainingQuota);
				}

				Integer activityNo = actPicService.findByActivityNo(activity.getActivityNo());
				activityDTO.setActivityPicNo(activityNo);

				dtos.add(activityDTO);
			}
		}
		model.addAttribute("activity", dtos);
		return "activity/memberActivityHomePage";
	}

// 2. 顯示單筆資料 --------------------------------------------------------------------------------------------
	@GetMapping("/activity/findByIdOnFrontpage")
	public String previewActivityWithPictureAndPeriod(@RequestParam("activityNo") Integer activityNo, Model model) {
		Activity activity = aService.findActivityByActId(activityNo);

//	    if (activity == null) {
//	        return "error/errorActivityNotFound";
//	    }

		List<ActivityPicture> activityPictures = activity.getActivityPictures();
		List<ActivityPeriod> activityPeriods = activity.getActivityPeriods();
		List<Integer> quota = new ArrayList<>();
		for (ActivityPeriod period : activityPeriods) {
			int totalquota = actSignupService.findTotalOrdersByActivityPeriodNo(period.getActivityPeriodNo());
			quota.add((period.getActivityPeriodQuota() - totalquota));
		}

		System.out.println("quota:" + quota);
		model.addAttribute("activity", activity);
		model.addAttribute("activityPictures", activityPictures);
		model.addAttribute("activityPeriods", activityPeriods);
		model.addAttribute("quotas", quota);
		return "activity/memberActivityPreview";
	}

}