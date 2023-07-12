package tw.hicamp.forum.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import tw.hicamp.member.model.Member;

@Data
@Entity
@Table(name = "postComment")
public class PostComment{
	
	@Id
	@Column(name = "postCommentNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postCommentNo;
	
	@Column(name = "postNo",insertable = false,updatable = false)
	private Integer postNo;
	
	@Column(name = "memberNo",insertable = false,updatable = false)
	private Integer memberNo;
	
	@Column(name = "postComment")
	private String postComment;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "postCommentDate")
	private Date postCommentDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postNo")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberNo")
	private Member member;
	
}
