package tw.hicamp.activity.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.hicamp.activity.dto.ActivityDtoForBackEndPage;
import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.service.ActivityPeriodService;
import tw.hicamp.activity.service.ActivityService;

@Controller
public class ActivityPeriodController {

	@Autowired
	private ActivityService aService;

	@Autowired
	private ActivityPeriodService actPeriodService;

// ==管理者頁面 Outline ======================================================================================

// 1.新增活動期別  http://localhost:8080/HiCamp/activity/insertNewPeriod
// 2.修改活動期別 http://localhost:8080/HiCamp/activity/updateActivityDto
// 3.刪除單筆期別 http://localhost:8080/HiCamp/activity/deleteActivityPeriod

// ==========================================================================================================	
	
//	1. 新增活動期別-------------------------------------------------------------------------------------------
	@PostMapping("/activity/insertNewPeriod")
	@ResponseBody
	public ActivityPeriod insertNewPeriod(@RequestParam("activityNo")Integer activityNo,
								@RequestParam("activityDepartureDate") Date activityDepartureDate,
								@RequestParam("activityReturnDate") Date activityReturnDate,
								@RequestParam("signupDeadline") Date signupDeadline, 
								@RequestParam("signupQuantity") Integer signupQuantity,
								@RequestParam("activityPeriodQuota") Integer activityPeriodQuota,
								@RequestParam("activityPeriodPrice") Integer activityPeriodPrice,
								Model model) {
		ActivityPeriod activityPeriod = new ActivityPeriod();
		Activity activity = aService.findActivityByActId(activityNo);
		activityPeriod.setActivity(activity);
		activityPeriod.setActivityNo(activityNo);
		activityPeriod.setActivityDepartureDate(activityDepartureDate);
		activityPeriod.setActivityReturnDate(activityReturnDate);
		activityPeriod.setSignupDeadline(signupDeadline);
		activityPeriod.setSignupQuantity(signupQuantity);
		activityPeriod.setActivityPeriodQuota(activityPeriodQuota);
		activityPeriod.setActivityPeriodPrice(activityPeriodPrice);
		
		actPeriodService.insertOnePeriod(activityPeriod);
		return activityPeriod;
	}

//	2. 修改活動期別-------------------------------------------------------------------------------------------
	@ResponseBody
	@PutMapping("/activity/updateActivityDto")
	public String updateActivityWithPeriod(@RequestBody ActivityDtoForBackEndPage activity, Model model) {

		actPeriodService.updateActivityPeriodById(activity.getActivityPeriodNo(), activity.getActivityNo(),
				activity.getActivityDepartureDate(), activity.getActivityReturnDate(), activity.getSignupDeadline(),
				activity.getSignupQuantity(), activity.getActivityPeriodPrice(), activity.getActivityPeriodQuota());

		model.addAttribute("activity", activity);

		return "activity/manageOneActivity";
	}
	
//	3. 刪除單筆期別-------------------------------------------------------------------------------------------
	@DeleteMapping("/activity/deleteActivityPeriod")
	public String deleteActivityPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo) {
		actPeriodService.deleteActPeriodById(activityPeriodNo);
		return "刪除成功";
	}

}