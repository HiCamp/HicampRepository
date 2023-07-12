package tw.hicamp.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.hicamp.activity.service.LinePayService;

@Controller
public class LinePayController {

	@Autowired
	private LinePayService linePayService;
	
	@ResponseBody
	@PostMapping("/activity/makePaymentViaLinePay")
	public String makePaymentViaLinePay(@RequestParam("activityName") String activityName,
										@RequestParam("activityPeriodPrice") Integer activityPeriodPrice,
										@RequestParam("activitySignupNo") Integer activitySignupNo,
										@RequestParam("signupTotalAmount") Integer signupTotalAmount,
										@RequestParam("activityPeriodNo") Integer activityPeriodNo,
										@RequestParam("signupQuantity") Integer signupQuantity,
										Model model) {
		  try {
			  String paymentUrl = linePayService.makePayment(activityName, activityPeriodPrice, activitySignupNo, signupTotalAmount, activityPeriodNo, signupQuantity);
			  model.addAttribute("paymentUrl", paymentUrl);
			  model.addAttribute("activitySignupNo", activitySignupNo);
		      return paymentUrl;
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("message", "結帳失敗。請稍後再試。");
	          return "/activity/errorPage";
	        }

	    }
	
}
