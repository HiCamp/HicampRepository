package tw.hicamp.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
	
	List<PostComment> findByPostOrderByPostCommentNo(Post post);

	int countCommentByPostNo(Integer postNo);

}
