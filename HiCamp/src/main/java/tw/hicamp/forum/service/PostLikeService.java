package tw.hicamp.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.hicamp.forum.model.PostLike;
import tw.hicamp.forum.model.PostLikeRepository;

@Service
public class PostLikeService {
	
	@Autowired
	private PostLikeRepository postLikeRepository;
	
	public PostLike addLike(PostLike postLike) {
		return postLikeRepository.save(postLike);
	}

	public void removeLike(Integer postLikeId) {
		postLikeRepository.deleteById(postLikeId);
	}
	
	public int getLikeByPostNo(Integer postNo) {
		return postLikeRepository.countLikeByPostNo(postNo);
	}
	
	public boolean hasUserLikedPost(Integer memberNo, Integer postNo) {
	    return postLikeRepository.findByMemberNoAndPostNo(memberNo, postNo) != null;
	}
}
