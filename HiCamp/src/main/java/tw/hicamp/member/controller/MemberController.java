package tw.hicamp.member.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import tw.hicamp.member.config.RandomStringGenerator;
import tw.hicamp.member.model.Manager;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.EmailService;
import tw.hicamp.member.service.ManagerService;
import tw.hicamp.member.service.MemberService;
import tw.hicamp.product.service.ShoppingCartService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	//登入畫面
	@RequestMapping("projectmemberlogin")
	public String projectMemberLogin(HttpSession session, Model m) {
		String captcha = RandomStringGenerator.generateRandomString(6);
		session.setAttribute("captcha", captcha);
		m.addAttribute("captchaCode", captcha);
		return "/Member/login";
	}
	
	//進入登入頁面
	@GetMapping("changecaptcha")
	@ResponseBody
	public String changeCaptcha(HttpSession session) {
		String captcha = RandomStringGenerator.generateRandomString(6);
		// 將新的captcha存到session中
		session.setAttribute("captcha", captcha);
		return captcha;
	}

//	登入判斷
	@PostMapping("/login")
	public String login(@RequestParam("memberEmail")String email,
						@RequestParam("memberPassword")String password,
						@RequestParam("captcha")String insertCaptcha,
						HttpSession session,
						Model m) {
		Member member = memberService.findByEmail(email);
		Manager manager = managerService.findbyaccount(email);
		String captcha = (String)session.getAttribute("captcha");
		
		if (!captcha.equals(insertCaptcha)) {
			m.addAttribute("statusError", "驗證碼錯誤");
			captcha = RandomStringGenerator.generateRandomString(6); // 使用Apache Commons Lang生成隨機字串
			session.setAttribute("captcha", captcha);
			m.addAttribute("captchaCode", captcha);
			return "member/login";
		}
		if (manager != null) {
			//管理員登入成功
			if (password.equals(manager.getManagerPassword())) {
				session.setAttribute("name", manager.getManagerName());
				session.setAttribute("managerAccount", manager.getManagerName());
				System.out.println("管理員登入成功");
				return "redirect:/testsearchallmember";
			} else {
				m.addAttribute("passwordFalse", "密碼錯誤");
				return "member/login";
			}
		} else if (member != null) {
			//會員登入成功
			if (member.getMemberStatus() == 0) {
				m.addAttribute("statusError", "狀態異常");
				return "member/login";
			} else if (member.getMemberStatus() == 1) {
				if (passwordEncoder.matches(password, member.getMemberPassword())) {
					System.out.println(member.getMemberName());
					session.setAttribute("name", member.getMemberName());
					session.setAttribute("memberNo", member.getMemberNo());
					Integer countCart = shoppingCartService.countCart(member.getMemberNo());
					session.setAttribute("countCart", countCart);
					System.out.println("會員登入成功");
					return "member/newIndex";
				} else {
					m.addAttribute("passwordFalse", "密碼錯誤");
					return "member/login";
				}
			} else if (member.getMemberStatus() == 2) {
				m.addAttribute("statusError", "請先進行信箱驗證");
				return "member/login";
			}
			
		} else {
			m.addAttribute("statusError", "信箱錯誤");
			return "member/login";
		}
		return "member/newIndex";
	}
	
	//登出會員
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "member/newIndex";
	}
	
	//會員個人資料畫面
	@RequestMapping("/projectmembersetting")
	public String projectMemberSettint(HttpSession session,Model m) {
		Member member = memberService.findByNo((int) session.getAttribute("memberNo"));
		m.addAttribute("memberInfo", member);
		return "member/setting";
	}
	
	//會員中心
	@GetMapping("/membercenter")
	public String memberCenter() {
		return "member/memberCenter";
	}

	//會員修改自己的資料
	@PutMapping("/projectmemberupdate")
	@ResponseBody
	public Member ProjectMemberUpdate(HttpSession session,
									  @RequestBody Member member,
									  Model m) {
		int memberNo = (int) session.getAttribute("memberNo");
		member.setMemberNo(memberNo);
		member.setMemberStatus(1);
		Member updatedMember = memberService.updateMember(member);
		
		m.addAttribute("memberInfo", updatedMember);
		
		return updatedMember;
	}
	
	//會員自己修改密碼
	@PutMapping("/changememberpassword")
	@ResponseBody
	public void changeMemberPassword(@RequestParam("memberPassword")String password, HttpSession session) {
		Member member = memberService.findByNo((int)session.getAttribute("memberNo"));
		memberService.changeMemberPassword(member, password);
	}
	
	//會員修改自己的照片
	@ResponseBody
	@PostMapping("/insertphoto")
	public boolean insertPhoto(@RequestParam("memberPhoto")MultipartFile memberPhoto,HttpSession session) {
		return memberService.updatePhoto((int)session.getAttribute("memberNo"), memberPhoto);
	}
	
	//取得照片
	@ResponseBody
	@GetMapping("/getphoto")
	public ResponseEntity<byte[]> getMemberPhoto(@RequestParam("memberNo")int memberNo){
		Member member = memberService.findByNo(memberNo);
		byte[] memberPhoto = member.getMemberPhoto();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(memberPhoto);
	}
	
	//取得註冊會員資料
	@PostMapping("/getmemberinformation")
	@Async
	public String getMemberInformation(@RequestParam("memberName") String memberName,
									   @RequestParam("memberGender")String memberGender,
									   @RequestParam("memberEmail")String memberEmail,
									   @RequestParam("memberPassword")String memberPassword,
									   @RequestParam("memberPhone")String memberPhone,
									   @RequestParam("memberAddress") String memberAddress,
									   @RequestParam("memberId") String memberId,
									   @RequestParam("memberBirthday") String memberBirthday,
									   @RequestParam("memberEmergencyContact") String memberEmergencyContact,
									   @RequestParam("memberEmergencyPhone") String memberEmergencyPhone,
									   HttpSession session) {
		Member member = new Member();
		member.setMemberName(memberName);
		member.setMemberGender(memberGender);
		member.setMemberEmail(memberEmail);
		member.setMemberPassword(memberPassword);
		member.setMemberPhone(memberPhone);
		member.setMemberAddress(memberAddress);
		member.setMemberId(memberId);
		member.setMemberBirthday(memberBirthday);
		member.setMemberEmergencyContact(memberEmergencyContact);
		member.setMemberEmergencyPhone(memberEmergencyPhone);
		member.setMemberStatus(2);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date signupDate = new Date();
		String formattedDate = dateFormat.format(signupDate);
		member.setMemberSignupDate(formattedDate);
		
		memberService.createMember(member);
		
		//清除不要的session
		session.removeAttribute("memberName");
		session.removeAttribute("memberEmail");
		session.removeAttribute("memberBirthday");
		
		try {
        	String subject = "感謝您註冊成為'登露'會員";
            String message = "您好!! " + member.getMemberName() + " 請點擊下方連結來開通此帳號\r\n";
            String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            session.setAttribute("token", uuid);
            message += "http://localhost:8080/HiCamp/ckeckEmailStatus?token="+uuid+"&no="+member.getMemberNo();
            boolean isMailSent = emailService.prepareAndSend(member.getMemberEmail(), subject, message);
            if (isMailSent) {
				System.out.println("信件送出");
			} else {
				System.out.println("沒有送出");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return "member/newIndex";
	}
	
	//google登入驗證
	@PostMapping("/googlelogin")
	@ResponseBody
	public boolean googleLogin(@RequestParam("memberName")String memberName,
							   @RequestParam("memberEmail")String memberEmail,
							   @RequestParam("memberGender")String memberGender,
							   @RequestParam("memberBirthday")String memberBirthday,
							   HttpSession session) {
		Member member = memberService.findByEmail(memberEmail);
		if (member != null) {
			session.setAttribute("name", member.getMemberName());
			session.setAttribute("memberNo", member.getMemberNo());
			return true;
		} else {
			session.setAttribute("memberName", memberName);
			session.setAttribute("memberEmail", memberEmail);
			session.setAttribute("memberBirthday", memberBirthday);
			if (memberGender.equals("Male")) {
				session.setAttribute("memberGender", "男");
			} else {
				session.setAttribute("memberGender", "女");
			}
			return false;
		}
	}
	
	//google帳號第一次登入頁面
	@GetMapping("/googleAccount")
	public String googleAccountInfo() {
		return "member/googleAccountInfo";
	}
	
	//開通信箱
	@GetMapping("/ckeckEmailStatus")
	public String checkEmailPage(@RequestParam("token")String token, @RequestParam("no")int memberNo, HttpSession session, Model m) {
		String ckeckToken = (String) session.getAttribute("token");
		if (ckeckToken != null) {
			if (ckeckToken.equals(token)) {
				m.addAttribute("emailStatus", "驗證成功，恭喜您已成功開通！");
				Member member = memberService.findByNo(memberNo);
				memberService.checkMemberStatus(member);
				session.removeAttribute("token");
			} else {
				m.addAttribute("emailStatus", "驗證失敗");
			}
		}else {
			m.addAttribute("emailStatus", "驗證失敗");
		}
		return "member/ckeckEmail";
	}

	//註冊時判斷信箱是否重複
	@GetMapping("/checkemail")
	@ResponseBody
	public Boolean checkEmail(@RequestParam String email) {
		Member findByEmail = memberService.findByEmail(email);
		if (findByEmail != null) {
			return false;
		}
		return true;
	}
	
	//找回密碼的驗證信
	@GetMapping("/sendcheckemail")
	@ResponseBody
	public boolean sendEmailToCheckPassword(@RequestParam("memberEmail")String memberEmail, HttpSession session) {
		if(memberService.findByEmail(memberEmail) != null) {
			try {
	        	String subject = "找回密碼驗證碼";
	            String message = "以下是您的驗證碼，請在網頁上輸入\r\n";
	            Random random = new Random();
	            int randomNumber = random.nextInt(1000000);//產生0-999999的亂數
	            String verificationCode = String.format("%06d", randomNumber);//把多的數字變成0
	            message += verificationCode;
	            session.setAttribute("verify", verificationCode);
	            boolean isMailSent = emailService.prepareAndSend(memberEmail, subject, message);
	            if (isMailSent) {
					System.out.println("信件送出");
				} else {
					System.out.println("沒有送出");
				}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
			return true;
		}
		return false;
	}
	
	//確認驗證碼
	@GetMapping("/checkVerify")
	@ResponseBody
	public boolean checkVerify(@RequestParam("verify")String virify, HttpSession session) {
		String verifyCode = (String)session.getAttribute("verify");
		if (verifyCode != null) {
			if (verifyCode.equals(virify)) {
				session.removeAttribute("verify");
				return true;
			}
			return false;
		}
		return false;
	}
	
	//找回密碼中的更新會員密碼
	@GetMapping("/changepassword")
	@ResponseBody
	public void createNewPassword(@RequestParam("memberEmail")String email,@RequestParam("memberPassword")String password) {
		Member member = memberService.findByEmail(email);
		memberService.changeMemberPassword(member, password);
	}
	
	//------------------------------------------------------
	
	//取得所有會員資料
	@GetMapping("/testsearchallmember")
//	@ResponseBody
	public String searchAllMember(Model m) {
		List<Member> members = memberService.findAllMember();
		m.addAttribute("members", members);
		return "member/manageMemberPage";
	}
	
	//在管理頁面查詢單筆會員資料
	@GetMapping("/searchonemember")
	@ResponseBody
	public Member searchOneMember(@RequestParam("memberNo")Integer no, Model m) {
		Member member = memberService.findByNo(no);
		return member;
	}
	
	//管理葉面修改會員資料
	@PutMapping("/projectmanagememberupdate")
	@ResponseBody
	public Member projectManageMemberUpdate(@RequestBody Member member) {
		System.out.println("更新誰的資料?"+ member.getMemberName());
		System.out.println();
		Member updatedMember = memberService.updateMember(member);
		
		return updatedMember;
	}
	
	//管理修改的照片
	@ResponseBody
	@PostMapping("/managerinsertphoto")
	public boolean managerUpdatePhoto(@RequestParam("memberPhoto")MultipartFile memberPhoto,@RequestParam("memberNo") int memberNo) {
		return memberService.updatePhoto(memberNo, memberPhoto);
	}
	
	//取得會員註冊圖表資料
	@GetMapping("/findbysignupdate")
	@ResponseBody
	public List<Map<Integer, Integer>> memberFindBySignupDate(@RequestParam("year")int year){
		return memberService.findBySignupDate(year);
	}
	
	//把圖表輸出成圖片
	@PostMapping("/getchartjspicture")
	@ResponseBody
	public String chartjsPicture(@RequestBody Map<String, String> chartPicture) throws IOException {
		String base64Image = chartPicture.get("img");
		String base64ImageContent = base64Image.split(",")[1];

        // 将Base64编码的字符串解码成字节数组
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageContent);

        // 将字节数组转换为BufferedImage
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // 将BufferedImage保存到文件
        File outputFile = new File("C:/Users/User/Desktop/test/memberSignup.png");
        ImageIO.write(image, "png", outputFile);
	    return "成功輸出";
	}
	
}
