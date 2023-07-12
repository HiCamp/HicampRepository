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
@Table(name = "postLike")
public class PostLike {
	
	@Id
	@Column(name = "postLikeNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postLikeNo;
	
	@Column(name = "memberNo",insertable = false,updatable = false)
	private Integer memberNo;
	
	@Column(name = "postNo",insertable = false,updatable = false)
	private Integer postNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postNo")
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberNo")
	private Member member;
}
