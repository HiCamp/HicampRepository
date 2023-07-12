package tw.hicamp.forum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import tw.hicamp.member.model.Member;

@Data
@Entity 
@Table(name = "postReport")
public class PostReport {

    @Id 
    @Column(name = "postReportNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postReportNo;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNo")
    private Member member;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postNo")
    private Post post;
	
    @Column(name = "postReportReason")
    private String postReportReason;
	
    @Column(name = "postReportStatus")
    private String postReportStatus;
    
}
