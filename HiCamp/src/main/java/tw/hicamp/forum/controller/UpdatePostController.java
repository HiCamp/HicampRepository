package tw.hicamp.forum.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.service.PostCommentService;
import tw.hicamp.forum.service.PostService;


@Controller
public class UpdatePostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostCommentService postcommentService;
	
	@Autowired
	private HttpSession session;
	
	// 修改貼文
	@GetMapping("/forum/update/{postNo}")
	public String updatePostMain(@PathVariable("postNo") Integer postNo, Model model) {
		Post originalForum = postService.getPostbyNo(postNo);
		model.addAttribute("originalForum", originalForum);
		model.addAttribute("post", originalForum);
		return "/forum/UpdatePost";
	}
	
	@PostMapping("/forum/updated/{postNo}")
	public String updatePost(@ModelAttribute("post") Post post,Model model) {
		Integer memberNo = (Integer) session.getAttribute("memberNo");
		post.setMemberNo(memberNo);
		post.setPostDate(new Date());
		
		postService.updatePost(post);
		
		model.addAttribute("result", "修改成功");
		model.addAttribute("post", post);
		return "redirect:/forum/showallmanager";
	}
	
	// 修改留言
	@ResponseBody
	@PutMapping("/forum/{postNo}/comments/{postCommentNo}")
	public ResponseEntity<Void> updatePostComment(
	    @PathVariable("postNo") Integer postNo,
	    @PathVariable("postCommentNo") Integer postCommentNo,
	    @RequestBody PostComment updatedPostComment
	    ) {
	    
	    session.getAttribute("memberNo");
	    Date postCommentDate = new Date();
	    
	    postService.getPostbyNo(postNo);
	    
	    PostComment existingPostComment = postcommentService.getCommentByCommentNo(postCommentNo);
	    
	    existingPostComment.setPostComment(updatedPostComment.getPostComment());
	    existingPostComment.setPostCommentDate(postCommentDate);
	    
	    postcommentService.updateComment(existingPostComment);

	    return ResponseEntity.ok().build();
	}

}
