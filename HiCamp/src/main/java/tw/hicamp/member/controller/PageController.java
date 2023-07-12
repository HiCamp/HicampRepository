package tw.hicamp.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	//到前台首頁
	@RequestMapping("projecthomepage")
	public String projectHomePage() {
		return "/Member/newIndex";
	}
	
	//註冊畫面
	@RequestMapping("register")
	public String register() {
		return "/Member/register";
	}

	//忘記密碼畫面
	@RequestMapping("forgetpassword")
	public String forgetpassword() {
		return "member/forgetPassword";
	}
	
	//---------------------------------------
	
	//後臺首頁
	@RequestMapping("projectmanagerpage")
	public String projectManagerPage() {
		return "/Member/ProjectManagerPage";
	}

	//圖表內容
	@RequestMapping("manageMemberChart")
	public String projectManagerChart() {
		return "/Member/manageMemberChart";
	}
	
	//管理會員資料畫面
	@RequestMapping("projectmanagemember")
	public String projectManageMember() {
		return "/Member/ProjectManageMember";
	}
	
	//test
	@RequestMapping("testmanagemember")
	public String testManageMember() {
		return "/member/manageMemberPage";
	}

	//修改會員資料畫面
	@RequestMapping("projectmanagememberupdate")
	public String projectManageMemberUpdate() {
		return "/Member/ProjectManageMemberUpdate";
	}
}
