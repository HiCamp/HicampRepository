package tw.hicamp.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.hicamp.activity.service.MailService;

@Controller
public class MailController {

	@Autowired
	private MailService mailService;

//	測試用
	@GetMapping("/activity/sendMail")
	@ResponseBody
	public String sendEmail() {
		mailService.prepareAndSend("sally820828@gmail.com", "title", "Sample mail subject");
		return "Mail sent";
	}

}
