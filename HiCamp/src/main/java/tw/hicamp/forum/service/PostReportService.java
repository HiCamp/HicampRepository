package tw.hicamp.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.hicamp.forum.model.PostReport;
import tw.hicamp.forum.model.PostReportRepository;

@Service
public class PostReportService {
	
	@Autowired
	private PostReportRepository postReportRepository;
	
	public PostReport addReport(PostReport postReport) {
		return postReportRepository.save(postReport);
	}
	
	public List<PostReport> getAllReports() {
		return postReportRepository.findAll();	
	}
	
	public List<PostReport> getReportsByPostNo(Integer postNo) {
		return postReportRepository.findByPost_PostNoOrderByPostReportNoDesc(postNo);
	}
	
	@Transactional
	public PostReport updateReportStatus(PostReport postReport) {
		Optional<PostReport> optional = postReportRepository.findById(postReport.getPostReportNo());
		
		if(optional.isPresent()) {
			PostReport existingReport = optional.get();
			existingReport.setPostReportStatus(postReport.getPostReportStatus());
			return postReportRepository.save(existingReport);
		}
		
		return null;
	}
	
	public boolean deleteReport(Integer postReportNo) {
		if(postReportRepository.existsById(postReportNo)) {
			postReportRepository.deleteById(postReportNo);
			return true;
		}
		
		return false;
	}
}

