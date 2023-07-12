package tw.hicamp.forum.controller;

import java.util.Date;

import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.MemberService;
import tw.hicamp.forum.dto.PostCommentDTO;
import tw.hicamp.forum.dto.PostLikeDTO;
import tw.hicamp.forum.dto.PostReportDTO;
import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.model.PostLike;
import tw.hicamp.forum.model.PostReport;
import tw.hicamp.forum.service.PostCommentService;
import tw.hicamp.forum.service.PostLikeService;
import tw.hicamp.forum.service.PostReportService;
import tw.hicamp.forum.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;


@Controller
public class InsertPostController {

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
    
    @Autowired
    private HttpSession session;
    
    // 新增貼文
    @GetMapping("/forum/add")
    public String insertPostMain(Model model) {
       	if (session.getAttribute("memberNo") == null) {
    		return "redirect:/projectmemberlogin"; 
    	}
    	
    	model.addAttribute("post",new Post());
        return "/forum/InsertPost";
    }

    @PostMapping("/forum/added")
    public String insertPost(@ModelAttribute("post")Post post,Model model) {
    	Integer memberNo = (Integer) session.getAttribute("memberNo");
    	
    	Member member = memberService.findByNo(memberNo);
    	String memberName = member.getMemberName();
    	
    	post.setMember(member);
    	post.setPostDate(new Date());
    	
    	postService.insertPost(post);    
             
    	model.addAttribute("memberName", memberName);

        return "redirect:/forum/showall";
    }
    
    // 新增留言
    @ResponseBody
    @PostMapping("/forum/addcomment")
    public PostComment insertPostComment(@RequestBody PostCommentDTO postCommentDTO) {
      	Integer memberNo = (Integer) session.getAttribute("memberNo");
    	if (memberNo == null) {
    		 throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "請登入會員");
        }
    	
    	Member member = memberService.findByNo(memberNo);
    	
        Post post = postService.getPostbyNo(postCommentDTO.getPostNo());
        
        PostComment postComment = new PostComment();
        postComment.setPost(post);
        postComment.setMember(member);
        postComment.setPostComment(postCommentDTO.getPostCommentText());
        postComment.setPostCommentDate(new Date());
        
        postCommentService.insertComment(postComment);
        
        return postComment;
    }
    
    // 喜歡貼文
    @ResponseBody
    @PostMapping("/forum/addlike")
    public PostLike insertPostLike(@RequestBody PostLikeDTO postLikeDTO) {
        Integer memberNo = (Integer) session.getAttribute("memberNo");
        if (memberNo == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "請登入會員");
        }

        Member member = memberService.findByNo(memberNo);
        Post post = postService.getPostbyNo(postLikeDTO.getPostNo());
        
        PostLike postLike = new PostLike();
        postLike.setMember(member);
        postLike.setPost(post);
        
        postLikeService.addLike(postLike);
        
        return postLike;
    }
    
    // 檢舉貼文
    @ResponseBody
    @PostMapping("/forum/addreport")
    public PostReport insertPostReport(@RequestBody PostReportDTO postReportDTO) {
        Integer memberNo = (Integer) session.getAttribute("memberNo");
        if (memberNo == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "請登入會員");
        }

        Member member = memberService.findByNo(memberNo);
        Post post = postService.getPostbyNo(postReportDTO.getPostNo());

        PostReport postReport = new PostReport();
        postReport.setMember(member);
        postReport.setPost(post);
        postReport.setPostReportReason(postReportDTO.getPostReportReason());
        postReport.setPostReportStatus("待審核"); 

        postReportService.addReport(postReport);

        return postReport;
    }
}
