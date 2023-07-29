package tw.hicamp.activity.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.activity.dto.ActivitySignupOrderDto;
import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivitySignup;
import tw.hicamp.activity.service.ActivityPeriodService;
import tw.hicamp.activity.service.ActivityPictureService;
import tw.hicamp.activity.service.ActivityService;
import tw.hicamp.activity.service.ActivitySignupService;
import tw.hicamp.activity.service.MailService;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.MemberService;

@Controller
public class ActivitySignupController {

	@Autowired
	private ActivityService aService;

	@Autowired
	private ActivitySignupService actSignupService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ActivityPeriodService actPeriodService;

	@Autowired
	private ActivityPictureService actPicService;

	@Autowired
	private MailService mailService;

// ==管理者頁面 Outline ======================================================================================

// 1.新增單筆訂單(手動新增報名訂單)  http://localhost:8080/HiCamp/activity/insertOrder
// 2.顯示所有訂單資訊 http://localhost:8080/HiCamp/activity/findAllSignupOrdersDto
// 3.顯示單筆訂單資訊  http://localhost:8080/HiCamp/activity/findOneSignupOrder
// 4.更新訂單資料 http://localhost:8080/HiCamp/activity/updateSignupOrder

// ==========================================================================================================		
	
//	1. 新增單筆訂單(手動新增報名訂單)-----------------------------------------------------------------------
	@PostMapping("/activity/insertOrder")
	public String insertSignupOrder(@RequestParam("memberNo") Integer memberNo,
	        @RequestParam("activityPeriodNo") Integer activityPeriodNo,
	        @RequestParam("signupDate") Date signupDate,
	        @RequestParam("signupTotalAmount") Integer signupTotalAmount,
	        @RequestParam("signupPaymentStatus") String signupPaymentStatus,
	        @RequestParam("signupQuantity") Integer signupQuantity,Model model) {
		
	    List<Member> members = memberService.findAllMember();
	    ActivitySignup activitySignup = new ActivitySignup();

	    activitySignup.setMemberNo(memberNo);
	    activitySignup.setActivityPeriodNo(activityPeriodNo);
	    activitySignup.setSignupDate(signupDate);
	    activitySignup.setSignupTotalAmount(signupTotalAmount);
	    activitySignup.setSignupPaymentStatus(signupPaymentStatus);
	    activitySignup.setSignupQuantity(signupQuantity);

	    actSignupService.insertActivitySignup(activitySignup);
	    model.addAttribute("members", members);
	    model.addAttribute("activitySignup", activitySignup);

	    return "redirect:/activity/findAllSignupOrdersDto";
	}

//	2. 顯示所有訂單資訊-----------------------------------------------------------------------
//	包含: 訂單編號: 報名日期:  / 會員編號: 會員姓名: / 活動編號(隱藏): 活動名稱: / 期別編號(隱藏): 出發日期: 回程日期: /訂單金額: 付款狀態: 
	@GetMapping("/activity/findAllSignupOrdersDto")
	public String findAllSignupOrdersDto(ActivitySignupOrderDto activitySignupOrderDto, Model model) {

		List<Member> memberList = memberService.findAllMember();
		List<Activity> activityList = aService.findAllActivity();
		List<ActivityPeriod> activityPeriodList = actPeriodService.findAllActPeriods();
		List<ActivitySignup> activitySignupList = actSignupService.findAllSignupOrders();

		List<ActivitySignupOrderDto> signupOrderList = new ArrayList<>();

		for (ActivitySignup activitySignup : activitySignupList) {
			ActivitySignupOrderDto signupOrderDto = new ActivitySignupOrderDto();

			signupOrderDto.setActivitySignupNo(activitySignup.getActivitySignupNo());
			signupOrderDto.setSignupDate(activitySignup.getSignupDate());
			signupOrderDto.setSignupTotalAmount(activitySignup.getSignupTotalAmount());
			signupOrderDto.setSignupPaymentStatus(activitySignup.getSignupPaymentStatus());

			for (Member member : memberList) {
				if(member.getMemberNo() == (activitySignup.getMemberNo())) {
					signupOrderDto.setMemberNo(member.getMemberNo());
					signupOrderDto.setMemberName(member.getMemberName());
					break;
				}
			}

			for (Activity activity : activityList) {
				if(activity.getActivityNo().equals(activity.getActivityNo())) {
					signupOrderDto.setActivityNo(activity.getActivityNo());
					signupOrderDto.setActivityName(activity.getActivityName());
					break;
				}
			}

			for (ActivityPeriod activityPeriod : activityPeriodList) {
				if(activityPeriod.getActivityPeriodNo().equals(activityPeriod.getActivityPeriodNo())) {
					signupOrderDto.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
					signupOrderDto.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
					signupOrderDto.setActivityReturnDate(activityPeriod.getActivityReturnDate());
					break;
				}
			}
			signupOrderList.add(signupOrderDto);
		}
		model.addAttribute("signupOrderList", signupOrderList);

		return "activity/manageSignupOrderList";
	}

//	3. 顯示單筆訂單資訊-----------------------------------------------------------------------
	@GetMapping("/activity/findOneSignupOrder")
	public String findOneSignupOrder(@RequestParam("activitySignupNo") Integer activitySignupNo, Model model) {

		ActivitySignup signupOrder = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		Member member = signupOrder.getMember();
		ActivityPeriod activityPeriod = signupOrder.getActivityPeriod();
		Activity activity = activityPeriod.getActivity();

		ActivitySignupOrderDto signupOrderDto = new ActivitySignupOrderDto();
		signupOrderDto.setActivitySignupNo(activitySignupNo);
		signupOrderDto.setSignupDate(signupOrder.getSignupDate());
		signupOrderDto.setSignupTotalAmount(signupOrder.getSignupTotalAmount());
		signupOrderDto.setSignupQuantity(signupOrder.getSignupQuantity());
		signupOrderDto.setSignupPaymentStatus(signupOrder.getSignupPaymentStatus());

		signupOrderDto.setMemberNo(member.getMemberNo());
		signupOrderDto.setMemberGender(member.getMemberGender());
		signupOrderDto.setMemberPhone(member.getMemberPhone());
		signupOrderDto.setMemberAddress(member.getMemberAddress());
		signupOrderDto.setMemberEmail(member.getMemberEmail());
		signupOrderDto.setMemberId(member.getMemberId());
		signupOrderDto.setMemberBirthday(member.getMemberBirthday());
		signupOrderDto.setMemberEmergencyContact(member.getMemberEmergencyContact());
		signupOrderDto.setMemberEmergencyPhone(member.getMemberEmergencyPhone());

		signupOrderDto.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
		signupOrderDto.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
		signupOrderDto.setActivityReturnDate(activityPeriod.getActivityReturnDate());
		signupOrderDto.setActivityPeriodPrice(activityPeriod.getActivityPeriodPrice());

		signupOrderDto.setActivityNo(activity.getActivityNo());
		signupOrderDto.setActivityName(activity.getActivityName());
		signupOrderDto.setActivityType(activity.getActivityType());
		signupOrderDto.setActivityLocation(activity.getActivityLocation());

		model.addAttribute("signupOrderDto", signupOrderDto);
		return "activity/manageOneSingupOrder";
	}

//	4. 更新訂單資料---------------------------------------------------------------------------
	@ResponseBody
	@PutMapping("/activity/updateSignupOrder")
	public String editOneSignupOrder(
			@RequestParam("memberNo") Integer memberNo,
			@RequestParam("activitySignupNo") Integer activitySignupNo,
			@RequestParam("activityPeriodNo") Integer activityPeriodNo, @RequestParam("signupDate") String signupDate,
			@RequestParam("signupTotalAmount") Integer signupTotalAmount,
			@RequestParam("signupPaymentStatus") String signupPaymentStatus,
			@RequestParam("signupQuantity") Integer signupQuantity, Model model) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(signupDate);

		ActivitySignup activitySignup = actSignupService.updateActivitySignupOrderByNo(activitySignupNo, memberNo,
				activityPeriodNo, date, signupQuantity, signupTotalAmount, signupPaymentStatus);

		model.addAttribute("activitySignup", activitySignup);

		return "redirect:activity/manageOneSingupOrder";
	}

	
//	chart.js
//  進入分析圖表--------------------------------------------------------------------------------------
	@GetMapping("/activity/signupOrderAnalysis")
	public String signupOrderAnalysis() {
		return "activity/managerSignupOrderChart";
	}
	
// Chart 每月報名量
	@ResponseBody
	@GetMapping("/activity/singupOrderDataPerMonth")
	public List<Map<String, Object>> getSignupDataPerMonth() {
	    return actSignupService.getSignupDataPerMonth();
	}
	
//// Chart 類別報名數量
//	@ResponseBody
//	@GetMapping("/activity/singupOrderanalysisByType")
//	public List<Map<String, Object>> getSignupDataByType() {
//	    return actSignupService.getSignupDataByType();
//	}
	
// ==使用者頁面 Outline ======================================================================================

// 1.確認報名資料(尚未付款&結單)  http://localhost:8080/HiCamp/activity/checkOrder
// 2.新增一筆訂單 http://localhost:8080/HiCamp/activity/placeOrder
// 3.跳轉訂單明細+寄信  http://localhost:8080/HiCamp/activity/memberCheckSignupOrder
// 4.查詢訂單列表 http://localhost:8080/HiCamp/activity/memberCheckSignupOrderList
// 5.查詢單筆活動訂單 http://localhost:8080/HiCamp/activity/memberCenterFindAllSignupOrders
// 6.用訂單日期查詢一年內訂單	http://localhost:8080/HiCamp/activity/memberCenterFindSignupOrderBySignupDate
// 7.用關鍵字查詢訂單 http://localhost:8080/HiCamp/activity/memberCenterFindSignupOrderByKeyword
// 8.更新訂單狀態 http://localhost:8080/HiCamp/activity/updateSignupPaymentStatus

// ==========================================================================================================	

//	1. 確認報名資料(尚未付款&結單)-----------------------------------------------------------------------
	@GetMapping("/activity/checkOrder")
	public String checkSignUpOrder(@RequestParam("activityPeriodNo") Integer activityPeriodNo, Model model,
			HttpSession session) {

		Integer memberNo = (Integer) session.getAttribute("memberNo");

		if (memberNo == null) {
			return "/Member/login";
		}

		Member member = memberService.findByNo(memberNo);
		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);
		Activity activity = aService.findActivityById(activityPeriod.getActivityNo());

		Integer activityPictureNo = actPicService.findByActivityNo(activity.getActivityNo());
		
		ActivitySignupOrderDto activitySignupOrderDto = new ActivitySignupOrderDto();
		activitySignupOrderDto.setMemberNo(member.getMemberNo());
		activitySignupOrderDto.setMemberName(member.getMemberName());
		activitySignupOrderDto.setMemberGender(member.getMemberGender());
		activitySignupOrderDto.setMemberId(member.getMemberId());
		activitySignupOrderDto.setMemberBirthday(member.getMemberBirthday());
		activitySignupOrderDto.setMemberEmail(member.getMemberEmail());
		activitySignupOrderDto.setMemberPhone(member.getMemberPhone());
		activitySignupOrderDto.setMemberAddress(member.getMemberAddress());
		activitySignupOrderDto.setMemberEmergencyContact(member.getMemberEmergencyContact());
		activitySignupOrderDto.setMemberEmergencyPhone(member.getMemberEmergencyPhone());

		activitySignupOrderDto.setActivityName(activityPeriod.getActivity().getActivityName());
		activitySignupOrderDto.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
		activitySignupOrderDto.setActivityPeriodPrice(activityPeriod.getActivityPeriodPrice());
		activitySignupOrderDto.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
		activitySignupOrderDto.setActivityReturnDate(activityPeriod.getActivityReturnDate());
		
		activitySignupOrderDto.setActivityPictureNo(activityPictureNo); 

		model.addAttribute("activitySignupOrderDto", activitySignupOrderDto);
		model.addAttribute("member", member);
		model.addAttribute("activityPeriod", activityPeriod);

		return "activity/memberPlaceSignupOrder";
	}

//	2. 新增一筆訂單-------------------------------------------------------------------------------------
	@PostMapping("/activity/placeOrder")
	@ResponseBody
	public Integer placeSignUpOrder(
			@RequestParam("activityPeriodNo") Integer activityPeriodNo,
			@RequestParam("signupDate") Date signupDate, 
			@RequestParam("signupTotalAmount") Integer signupTotalAmount,
			@RequestParam("signupQuantity") Integer signupQuantity,
			@RequestParam("signupPaymentStatus") String signupPaymentStatus, 
			Model model, HttpSession session) {

		Integer memberNo = (Integer) session.getAttribute("memberNo");

		Member member = memberService.findByNo(memberNo);
		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);

		ActivitySignup activitySignup = new ActivitySignup();
		activitySignup.setMemberNo(memberNo);
		activitySignup.setActivityPeriodNo(activityPeriodNo);
		activitySignup.setSignupDate(signupDate);
		activitySignup.setSignupTotalAmount(signupTotalAmount);
		activitySignup.setSignupQuantity(signupQuantity);
		activitySignup.setSignupPaymentStatus(signupPaymentStatus);

		ActivitySignup activitySignupOrder = actSignupService.insertActivitySignup(activitySignup);

		model.addAttribute("member", member);
		model.addAttribute("activityPeriod", activityPeriod);
		model.addAttribute("activitySignupOrder", activitySignupOrder);

		return activitySignup.getActivitySignupNo();
	}

//	3. 跳轉訂單明細+寄信------------------------------------------------------------------------------
	@GetMapping("/activity/memberCheckSignupOrder")
	public String memberCheckSignupOrder(@RequestParam("orderId") Integer activitySignupNo, HttpSession session, Model model) {
		Integer memberNo = (Integer) session.getAttribute("memberNo");
		Member member = memberService.findByNo(memberNo);
		ActivitySignup activitySignup = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		Activity activity = aService.findActivityById(activitySignup.getActivityPeriod().getActivityNo());

		Date departureDate = activitySignup.getActivityPeriod().getActivityDepartureDate();
		Date returnDate = activitySignup.getActivityPeriod().getActivityReturnDate();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDepartureDate = dateFormat.format(departureDate);
		String formattedReturnDate = dateFormat.format(returnDate);

		String message = "<html>" 
			    + "<head>" 
			        + "<style>" 
			            + ".email-container {" 
			                + "border: 1px solid #ccc;"
			                + "padding: 20px;" 
			            + "}" 
			            + "table {" 
			                + "border-collapse: collapse;" 
			                + "width: 50%;" 
			            + "}"
			            + "td, th {" 
			                + "border: 1px solid #ccc;" 
			                + "padding: 8px;" 
			                + "text-align: left;" 
			            + "}" 
			            + "th {"
			                + "background-color: #f9d9b7;" 
			            + "}" 
			            + "td {"
			                + "background-color: #fdefdd;" 
			            + "}" 
			        + "</style>"
			    + "</head>" 
			    + "<body>" 
			        + "親愛的" + member.getMemberName() + "您好,"
			        + "<p>感謝您報名參加 登露 HiCamp 的活動！您的報名已成功。以下是您的活動報名詳細資訊：</p>" 
			        + "<table>" 
			            + "<tr>" 
			                + "<td>活動名稱</td>" 
			                + "<td>" + activity.getActivityName() + "</td>" 
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>集合地點</td>" 
			                + "<td>" + activity.getActivityLocation() + "</td>" 
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>出發日期</td>" 
			                + "<td>" + formattedDepartureDate + "</td>" 
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>回程日期</td>" 
			                + "<td>" + formattedReturnDate + "</td>" 
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>訂單編號</td>" 
			                + "<td>" + activitySignup.getActivitySignupNo() + "</td>"
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>訂購日期</td>" 
			                + "<td>" + activitySignup.getSignupDate() + "</td>" 
			            + "</tr>"
			            + "<tr>" 
			                + "<td>活動價格</td>" 
			                + "<td>" + activitySignup.getActivityPeriod().getActivityPeriodPrice() + "</td>" 
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>報名數量</td>" 
			                + "<td>" + activitySignup.getSignupQuantity() + "</td>"
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>報名金額</td>" 
			                + "<td>" + activitySignup.getSignupTotalAmount() + "</td>"
			            + "</tr>" 
			            + "<tr>" 
			                + "<td>付款狀態</td>" 
			                + "<td>" + activitySignup.getSignupPaymentStatus() + "</td>"
			            + "</tr>" 
			        + "</table>" 
			        + "<p>如您有任何問題或需求，請隨時聯繫我們的客戶服務團隊。我們將竭誠為您提供支援。</p>"
			        + "<p>再次感謝您的參與！期待與您一同度過一個難忘的 HiCamp 活動。</p>" 
			        + "<p>登露 HiCamp</p>" 
			    + "</body>" 
			+ "</html>";

		String title = "登露HiCamp 活動報名訂單明細";

		mailService.prepareAndSend(member.getMemberEmail(), title, message);

		model.addAttribute("member", member);
		model.addAttribute("activity", activity);
		model.addAttribute("activitySignup", activitySignup);
		model.addAttribute("activityPeriod", activitySignup.getActivityPeriod());

		return "activity/memberCheckSignupOrder";
	}
	
//	4. 查詢訂單列表-----------------------------------------------------------------------------------
	@GetMapping("/activity/memberCheckSignupOrderList")
	public String memberCheckSignupOrderList(@RequestParam("orderId") Integer activitySignupNo, HttpSession session, Model model) {
		
		Integer memberNo = (Integer) session.getAttribute("memberNo");
		Member member = memberService.findByNo(memberNo);
		ActivitySignup activitySignup = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		Activity activity = aService.findActivityById(activitySignup.getActivityPeriod().getActivityNo());
		
		model.addAttribute("member", member);
		model.addAttribute("activity", activity);
		model.addAttribute("activitySignup", activitySignup);
		model.addAttribute("activityPeriod", activitySignup.getActivityPeriod());
		
		return "activity/memberCheckSignupOrder";
	}

//  5. 查詢單筆活動訂單--------------------------------------------------------------------------------
	@GetMapping("/activity/memberCenterFindAllSignupOrders")
	public String findAllSignupOrdersByMemberId(HttpSession session, Model model) {

		Integer memberNo = (Integer) session.getAttribute("memberNo");
		List<ActivitySignup> signupOrders = actSignupService.findSignupOrdersBymemberNo(memberNo);
		List<ActivitySignupOrderDto> dtos = searchSignupOrderListDtos(signupOrders);
		
		Member member = memberService.findByNo(memberNo);

		model.addAttribute("signupOrders", dtos);
		model.addAttribute("member", member);
		return "activity/memberSignupOrderList";
	}

//  6. 用訂單日期查詢一年內訂單--------------------------------------------------------------------------------
	@GetMapping("/activity/memberCenterFindSignupOrderBySignupDate")
	@ResponseBody
	public List<ActivitySignupOrderDto> findSignupOrderBySignupDate(@RequestParam("endDate") String endDate,
			Model model, HttpSession session) {
		Integer memberNo = (Integer) session.getAttribute("memberNo");

		if (memberNo == null) {
			throw new RuntimeException("No memberNo in session");
		}

		if (endDate == null || endDate.trim().isEmpty()) {
			throw new RuntimeException("Date must be provided");
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date endDated;

		try {
			endDated = dateFormat.parse(endDate);
			List<ActivitySignup> signupOrderList = actSignupService.findSignupOrderBySignupDate(endDated,
					(int) memberNo);

			return searchSignupOrderListDtos(signupOrderList);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing signup date", e);
		}
	}
	
//	7. 用關鍵字查詢訂單--------------------------------------------------------------------------------
	@ResponseBody
	@GetMapping("/activity/memberCenterFindSignupOrderByKeyword")
	public List<ActivitySignupOrderDto> searchByKeyword(@RequestParam("keyword") String keyword, HttpSession session) {
		Integer memberNo = (Integer) session.getAttribute("memberNo");
		
		if (memberNo == null) {
			throw new RuntimeException("No memberNo in session");
		}
		
		List<ActivitySignup> signupOrderList = actSignupService.findByKeyword(keyword, memberNo);

		return searchSignupOrderListDtos(signupOrderList);
	}
	
//	查詢訂單列表
	private List<ActivitySignupOrderDto> searchSignupOrderListDtos(List<ActivitySignup> signupOrderList) {
	    List<Activity> activityList = aService.findAllActivity();
	    List<ActivitySignupOrderDto> dtos = new ArrayList<>(); 

	    for (ActivitySignup signup : signupOrderList) {
	        for (Activity activity : activityList) {
	            if (activity.getActivityNo().equals(signup.getActivityPeriod().getActivityNo())) {
	                ActivitySignupOrderDto signupOrderDto = new ActivitySignupOrderDto();

	                signupOrderDto.setActivityName(activity.getActivityName());
	                signupOrderDto.setActivityDepartureDate(signup.getActivityPeriod().getActivityDepartureDate());
	                signupOrderDto.setActivityReturnDate(signup.getActivityPeriod().getActivityReturnDate());
	                signupOrderDto.setSignupDate(signup.getSignupDate());
	                signupOrderDto.setActivitySignupNo(signup.getActivitySignupNo());
	                signupOrderDto.setSignupTotalAmount(signup.getSignupTotalAmount());
	                signupOrderDto.setSignupPaymentStatus(signup.getSignupPaymentStatus());
	                dtos.add(signupOrderDto);
	            }
	        }
	    }

	    return dtos;
	}
	 
//	8. 更新訂單狀態-----------------------------------------------------------------------------------
	@ResponseBody
	@PutMapping("/activity/updateSignupPaymentStatus")
	public String editSignupOrderPaymentStatus(
			@RequestParam("activitySignupNo") Integer activitySignupNo,
			@RequestParam("signupPaymentStatus") String signupPaymentStatus, Model model) throws ParseException {
		
		ActivitySignup findactivitySignup = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		if(findactivitySignup !=null) {
			ActivitySignup activitySignup = actSignupService.updateActivitySignupOrderPaymentStatusByNo(activitySignupNo, signupPaymentStatus);
			model.addAttribute("activitySignup", activitySignup);
			return "更新成功";
		}else {
			return "查無此筆資料";
		}
	}
	
}
