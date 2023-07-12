package tw.hicamp.activity.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.service.ActivityPeriodService;
import tw.hicamp.activity.service.ActivityService;

@Controller
public class ActivityPeriodController {

	@Autowired
	private ActivityService aService;

//	@Autowired
//	private ActivityPictureService actPicService;

	@Autowired
	private ActivityPeriodService actPeriodService;

	@GetMapping("/activity/testteeeeeeeeeest")
	public String test() {
		return "activity/testpage";
	}
	
//	新增期別
	@PostMapping("/activity/insertNewPeriod")
	@ResponseBody
	public String insertNewPeriod(@RequestParam("activityNo")Integer activityNo,
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
		return "新增成功";
	}

//	查詢單筆期別
	@GetMapping("/activity/findActivityPeriodById")
	public String findActivityByActivityPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo, Model model) {
		
		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);
		
		model.addAttribute("activityPeriod", activityPeriod);
		return "activity/testActivityPeriod";
	}
	
//	刪除期別
	@DeleteMapping("/activity/deleteActivityPeriod")
	public String deleteActivityPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo) {
		actPeriodService.deleteActPeriodById(activityPeriodNo);
		return "刪除成功";
	}

}