package tw.hicamp.forum.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
	
	int countLikeByPostNo(Integer postNo);
	
	PostLike findByMemberNoAndPostNo(Integer memberNo, Integer postNo);
}
