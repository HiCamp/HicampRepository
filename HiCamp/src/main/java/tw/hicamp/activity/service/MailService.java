package tw.hicamp.activity.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

@Service
public class MailService {

	private JavaMailSender mailSender;

	@Autowired
	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void prepareAndSend(String memberEmail,
							   String title,
							   String message) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("sally820828@gmail.com");
			messageHelper.setTo(memberEmail);
			messageHelper.setSubject(title);
			messageHelper.setText(message,true);
		};
		try {
			mailSender.send(messagePreparator);
			 System.out.println("sent");
		} catch (MailException e) {
			 System.out.println(e);
		}
	}

}