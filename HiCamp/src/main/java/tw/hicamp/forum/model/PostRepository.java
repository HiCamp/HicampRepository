package tw.hicamp.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.hicamp.member.model.Member;


public interface PostRepository extends JpaRepository<Post, Integer> {

	List<Post> findAllByOrderByPostNoDesc();
	
	List<Post> findByPostTypeOrderByPostNoDesc(String postType);
	
	List<Post> findByPostTitleContainingOrderByPostNoDesc(String keyword);

	List<Post> findByMemberOrderByPostNoDesc(Member member);
	
	List<Post> findTop5ByOrderByPostViewCountDesc();

}
