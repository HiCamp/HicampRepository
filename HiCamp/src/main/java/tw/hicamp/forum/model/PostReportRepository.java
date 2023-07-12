package tw.hicamp.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Integer> {
	
	List<PostReport> findByPost_PostNoOrderByPostReportNoDesc(Integer postNo);
}
