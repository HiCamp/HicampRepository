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
import org.springframework.web.bind.annotation.DeleteMapping;
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

//	後臺增刪改查=================================================================================
//	新增: 手動幫客戶新增報名訂單:  postman ok / 
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

//	查詢(全部) DTO  HTML OK	
//	<!-- (包含: 訂單編號: 報名日期:  / 會員編號: 會員姓名: / 活動編號(隱藏): 活動名稱: / 期別編號(隱藏): 出發日期: 回程日期: /訂單金額: 付款狀態: ) -->
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
				signupOrderDto.setMemberNo(member.getMemberNo());
				signupOrderDto.setMemberName(member.getMemberName());
				break;
			}

			for (Activity activity : activityList) {
				signupOrderDto.setActivityNo(activity.getActivityNo());
				signupOrderDto.setActivityName(activity.getActivityName());
				break;
			}

			for (ActivityPeriod activityPeriod : activityPeriodList) {
				signupOrderDto.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
				signupOrderDto.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
				signupOrderDto.setActivityReturnDate(activityPeriod.getActivityReturnDate());
				break;
			}
			signupOrderList.add(signupOrderDto);
		}
		model.addAttribute("signupOrderList", signupOrderList);

		return "activity/manageSignupOrderList";
	}

//	查詢(單筆): 訂單細項 (包含: 訂單資訊(訂單編號/報名日期/訂單總額/付款狀態)  PS. 一個人只會產生一張訂單, 不同人會有兩張訂單 (可以歸在同一個會員底下, 但若需幫家人報名必須填個資) 
//	                     會員資訊(姓名/電話/地址/手機/Email/身分證字號/生日(西元)/緊急聯絡人/緊急聯絡人電話)
//	                     活動資訊(活動名稱/活動地點/期別編號/活動期別/活動照片(小圖)/出發日期/回程日期/活動單價)
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

//	查詢所有會員資料
	@ResponseBody
	@GetMapping("/activity/searchMemberInfo")
	public List<Member> findAllMemberInfo(Model model) {
		List<Member> member = memberService.findAllMember();
		model.addAttribute("member", member);
		return member;
	}

//	更新訂單資料
	@ResponseBody
	@PutMapping("/activity/updateSignupOrder")
	public String editOneSignupOrder(@RequestParam("memberNo") Integer memberNo,
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

//	刪除 postman ok 
	@ResponseBody
	@DeleteMapping("/activity/deleteSignupOrder")
	public String deleteSignupOrder(@RequestParam("activitySignupNo") Integer activitySignupNo) {
		actSignupService.deleteActSignupOrderBySignupNo(activitySignupNo);
		return "刪除成功";
	}

//	前台報名活動=================================================================================

// 會員中心
//	@GetMapping("/membercenter")
//	public String memberCenter() {
//		return "member/memberCenter";
//	}

//	使用者確認報名資料(尚未付款&結單)
	@GetMapping("/activity/checkOrder")
	public String placeOrder(@RequestParam("activityPeriodNo") Integer activityPeriodNo, Model model,
			HttpSession session) {

		Integer memberNo = (Integer) session.getAttribute("memberNo");

		if (memberNo == null) {
			return "activity/activitylogin";
		}

		Member member = memberService.findByNo(memberNo);
		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);
		Activity activity = aService.findActivityById(activityPeriod.getActivityNo());

		Integer activityPictureNo = actPicService.findByActivityNo(activity.getActivityNo());
		System.out.println(activity.getActivityNo());
		
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

//	前端新增訂單
	@PostMapping("/activity/placeOrder")
	@ResponseBody
	public Integer placeOrder(@RequestParam("activityPeriodNo") Integer activityPeriodNo,
			@RequestParam("signupDate") Date signupDate, @RequestParam("signupTotalAmount") Integer signupTotalAmount,
			@RequestParam("signupQuantity") Integer signupQuantity,
			@RequestParam("signupPaymentStatus") String signupPaymentStatus, Model model, HttpSession session) {

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

//		String memberEmail = member.getMemberEmail();
//		mailService.prepareAndSend(userEmail, "您的訂單明細", activityName, activitySignupNo);

		return activitySignup.getActivitySignupNo();
	}

//	訂購成功跳轉訂單明細+寄信
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

		String message = "<html>" + "<head>" + "<style>" + ".email-container {" + "   border: 1px solid #ccc;"
				+ "   padding: 20px;" + "}" + "table {" + "   border-collapse: collapse;" + "   width: 50%;" + "}"
				+ "td, th {" + "   border: 1px solid #ccc;" + "   padding: 8px;" + "   text-align: left;" + "}" + "th {"
				+ "   background-color: #f9d9b7;" + "}" + "td {" + "   background-color: #fdefdd;" + "}" + "</style>"
				+ "</head>" + "<body>" + "親愛的" + member.getMemberName() + "您好,"
				+ "<p>感謝您報名參加 登露 HiCamp 的活動！您的報名已成功。以下是您的活動報名詳細資訊：</p>" + "<table>" + "<tr>" + "<td>活動名稱</td>" + "<td>"
				+ activity.getActivityName() + "</td>" + "</tr>" + "<tr>" + "<td>集合地點</td>" + "<td>"
				+ activity.getActivityLocation() + "</td>" + "</tr>" + "<tr>" + "<td>出發日期</td>" + "<td>"
				+ formattedDepartureDate + "</td>" + "</tr>" + "<tr>" + "<td>回程日期</td>" + "<td>" + formattedReturnDate
				+ "</td>" + "</tr>" + "<tr>" + "<td>訂單編號</td>" + "<td>" + activitySignup.getActivitySignupNo() + "</td>"
				+ "</tr>" + "<tr>" + "<td>訂購日期</td>" + "<td>" + activitySignup.getSignupDate() + "</td>" + "</tr>"
				+ "<tr>" + "<td>活動價格</td>" + "<td>" + activitySignup.getActivityPeriod().getActivityPeriodPrice()
				+ "</td>" + "</tr>" + "<tr>" + "<td>報名數量</td>" + "<td>" + activitySignup.getSignupQuantity() + "</td>"
				+ "</tr>" + "<tr>" + "<td>報名金額</td>" + "<td>" + activitySignup.getSignupTotalAmount() + "</td>"
				+ "</tr>" + "<tr>" + "<td>付款狀態</td>" + "<td>" + activitySignup.getSignupPaymentStatus() + "</td>"
				+ "</tr>" + "</table>" + "<p>如您有任何問題或需求，請隨時聯繫我們的客戶服務團隊。我們將竭誠為您提供支援。</p>"
				+ "<p>再次感謝您的參與！期待與您一同度過一個難忘的 HiCamp 活動。</p>" + "<p>登露 HiCamp</p>" + "</body>" + "</html>";
		String title = "登露HiCamp 活動報名訂單明細";

		mailService.prepareAndSend(member.getMemberEmail(), title, message);

		model.addAttribute("member", member);
		model.addAttribute("activity", activity);
		model.addAttribute("activitySignup", activitySignup);
		model.addAttribute("activityPeriod", activitySignup.getActivityPeriod());

		return "activity/memberCheckSignupOrder";
	}
	
//	查訂單列表
	@GetMapping("/activity/memberCheckSignupOrderList")
	public String memberCheckSignupOrderList(@RequestParam("orderId") Integer activitySignupNo, HttpSession session, Model model) {
		Integer memberNo = (Integer) session.getAttribute("memberNo");
		Member member = memberService.findByNo(memberNo);
		ActivitySignup activitySignup = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		Activity activity = aService.findActivityById(activitySignup.getActivityPeriod().getActivityNo());
		
		Date departureDate = activitySignup.getActivityPeriod().getActivityDepartureDate();
		Date returnDate = activitySignup.getActivityPeriod().getActivityReturnDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		model.addAttribute("member", member);
		model.addAttribute("activity", activity);
		model.addAttribute("activitySignup", activitySignup);
		model.addAttribute("activityPeriod", activitySignup.getActivityPeriod());
		
		return "activity/memberCheckSignupOrder";
	}

// 會員中心: 

// 查詢活動訂單
	@GetMapping("/activity/memberCenterFindAllSignupOrders")
	public String findAllSignupOrdersByMemberId(HttpSession session, Model model) {

		Integer memberNo = (Integer) session.getAttribute("memberNo");
		List<ActivitySignup> signupOrders = actSignupService.findSignupOrdersBymemberNo(memberNo);
		List<Activity> activities = aService.findAllActivity();
		List<ActivitySignupOrderDto> dtos = new ArrayList<>();
		
		for (ActivitySignup signup : signupOrders) {
			for (Activity activity : activities) {
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
		
		Member member = memberService.findByNo(memberNo);

		model.addAttribute("signupOrders", dtos);
		model.addAttribute("member", member);
		return "activity/memberSignupOrderList";
	}

// 用訂單日期查詢訂單
	@GetMapping("/activity/memberCenterFindSignupOrderBySignupDate")
	@ResponseBody
	public List<ActivitySignupOrderDto> findSignupOrderBySignupDate(@RequestParam("signupDate") String signupDate, Model model, HttpSession session) {
		Object memberNo = session.getAttribute("memberNo");
		
		if (memberNo != null) {
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date signupDated;
			
			try {
				signupDated = dateFormat.parse(signupDate);
				List<ActivitySignup> signupOrderList = actSignupService.findSignupOrderBySignupDate(signupDated, (int)memberNo);
				List<Activity> activities = aService.findAllActivity();
				List<ActivitySignupOrderDto> dtos = new ArrayList<>(); 
				
				for (ActivitySignup signup : signupOrderList) {
					for (Activity activity : activities) {
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
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@ResponseBody
	@GetMapping("/activity/memberCenterFindSignupOrderByKeyword")
	public List<ActivitySignupOrderDto> searchByKeyword(@RequestParam("keyword") String keyword, HttpSession session) {
		int memberNo = (int)session.getAttribute("memberNo");
		
		List<ActivitySignup> signupOrderList = actSignupService.findByKeyword(keyword, memberNo);
		List<Activity> activities = aService.findAllActivity();
		List<ActivitySignupOrderDto> dtos = new ArrayList<>(); 
		
		for (ActivitySignup signup : signupOrderList) {
			for (Activity activity : activities) {
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
	 
	
	
//	chart
	
	//進入分析圖表
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
	

	// Chart 類別報名數量
	@ResponseBody
	@GetMapping("/activity/singupOrderanalysisByType")
	public List<Map<String, Object>> getSignupDataByType() {
	    return actSignupService.getSignupDataByType();
	}

// 取消訂單(修改訂單狀態, 未付款的訂單狀態可以用前端判斷即可, 已付款則訂單成立. )

// 付款後取消

}
