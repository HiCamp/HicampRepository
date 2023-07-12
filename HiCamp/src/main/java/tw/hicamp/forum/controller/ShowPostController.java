package tw.hicamp.forum.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.model.PostReport;
import tw.hicamp.forum.service.PostCommentService;
import tw.hicamp.forum.service.PostLikeService;
import tw.hicamp.forum.service.PostReportService;
import tw.hicamp.forum.service.PostService;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.MemberService;


@Controller
public class ShowPostController{
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostCommentService postCommentService;
	
	@Autowired
	private PostLikeService postLikeService;
	
	@Autowired
	private PostReportService postReportService;
	
	@Autowired
	private MemberService memberService;
	
	// 查全部貼文 (管理者頁面)
	@GetMapping("/forum/showallmanager")
    public String getAllPostMain(Model model) {
		List<Post> posts = postService.getAllPosts();
	    List<Map<String, Object>> postDetails = new ArrayList<>();

	    for (Post post : posts) {
	        Map<String, Object> detail = new HashMap<>();
	        int likecounts = postLikeService.getLikeByPostNo(post.getPostNo());
	        List<PostComment> comments = postCommentService.getCommentsByPostSortedByNo(post);
	        
	        detail.put("post", post);
	        detail.put("likesCount", likecounts);
	        detail.put("commentsCount", comments.size());
	        detail.put("viewsCount", post.getPostViewCount());

	        postDetails.add(detail);
	    }

	    model.addAttribute("postDetails", postDetails);
	    return "/forum/ManagerHomepage";
    }
	
	// 查全部貼文 
	@GetMapping("/forum/showall")
	public String showAllPostMain(Model model) {
		List<Post> posts = postService.getAllPosts();
		model.addAttribute("posts", posts);
		return "/forum/UserHomepage";
	}
	
	// 查全部貼文by文章類別 
	@GetMapping("/forum/showall/{postType}")
	public String showAllPostMainByType(@PathVariable("postType") String postType, Model model) {
	    List<Post> posts = postService.getPostsByType(postType);
	    model.addAttribute("posts", posts);
	    return "/forum/UserHomepage";
	}
	
	// 查全部貼文by關鍵字查詢
	@PostMapping("/forum/showall/search")
    public String searchPosts(@RequestParam("keyword") String keyword, Model model) {
        List<Post> posts = postService.getPostsByKeyword(keyword);
        model.addAttribute("posts", posts);
        return "/forum/UserHomepage";  
    }
   
	// 查單一貼文
	@GetMapping("/forum/showpostbyno/{postNo}")
	public String getForumPostDetail1(@PathVariable("postNo") Integer postNo, Model model,HttpSession session) {
		Post post = postService.getPostbyNo(postNo);
		List<PostComment> comments = postCommentService.getCommentsByPostSortedByNo(post);
		
		Integer currentMemberNo = (Integer) session.getAttribute("memberNo");

		if (currentMemberNo != null) {
			Member currentMember = memberService.findByNo(currentMemberNo);
			model.addAttribute("currentMember", currentMember);
		}
		
		postService.updatePostViewCount(post);
		
		List<Post> topPosts = postService.getTop5PostsByViews();
		
		model.addAttribute("post", post);
		model.addAttribute("comments", comments);
		model.addAttribute("topPosts", topPosts);
		
		return "/forum/PostContentByNo";
	}
	
	// 查作者貼文
	@GetMapping("/forum/showpostbymember/{memberNo}")
	public String showPostsByMember(@PathVariable("memberNo") int memberNo, Model model) {
	    Member member = memberService.findByNo(memberNo);
	    List<Post> posts = postService.getPostsByMember(member);
	    model.addAttribute("posts", posts);
	    return "/forum/PostContentByMember"; 
	}
	
	// 查貼文讚數
	@GetMapping("/forum/post/{postNo}/likesCount")
	public ResponseEntity<Map<String, Integer>> showLikesCount(@PathVariable("postNo") Integer postNo) {
	    int likesCount = postLikeService.getLikeByPostNo(postNo);
	    Map<String, Integer> response = new HashMap<>();
	    response.put("likesCount", likesCount);
	    return ResponseEntity.ok(response);
	}
	
	// 查檢舉貼文
	@GetMapping("/forum/showreports")
	public String getAllReports(Model model) {
		List<PostReport> reports = postReportService.getAllReports();
	    model.addAttribute("reports", reports);
	    return "/forum/ManagerReport";
	}
	
	// 隱藏貼文
	@PostMapping("/forum/hide/{postNo}")
	@ResponseBody
	public ResponseEntity<Void> hidePost(@PathVariable("postNo") Integer postNo) {
	    postService.hidePost(postNo);
	    return ResponseEntity.ok().build();
	}
}
