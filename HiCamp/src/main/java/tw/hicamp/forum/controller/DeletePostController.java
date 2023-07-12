package tw.hicamp.forum.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tw.hicamp.forum.service.PostCommentService;
import tw.hicamp.forum.service.PostService;


@Controller 
public class DeletePostController{
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostCommentService postcommentService;
	
	// 刪除貼文
	@GetMapping("/forum/delete/{postNo}")
	public String deletePostMain(@PathVariable Integer postNo, RedirectAttributes redirectAttributes) {
		 boolean isDeleted = postService.deletePost(postNo);
		 redirectAttributes.addFlashAttribute("isDeleted", isDeleted);
	     return "redirect:/forum/showallmanager";
	}
	
	// 刪除留言
	@ResponseBody
	@DeleteMapping("/forum/deleteComment")
	public Map<String, Object> deletePostComment(@RequestBody Map<String, Integer> params) {
		Integer postCommentNo = params.get("postCommentNo");
        Map<String, Object> result = new HashMap<>();
        try {
        	postcommentService.deleteComment(postCommentNo);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
	
}
