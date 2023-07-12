package tw.hicamp.forum.model;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import tw.hicamp.member.model.Member;

@Data
@Entity 
@Table(name = "post")
public class Post{
	
	@Id 
	@Column(name = "postNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postNo;
	
	@Column(name = "memberNo",insertable = false,updatable = false)
	private Integer memberNo;
	
	@Column(name = "postType")
	private String postType;
	
	@Column(name = "postTitle")
	private String postTitle;
	
	@Column(name = "postContent")
	private String postContent;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "postDate")
	private Date postDate;
	
	@Column(name = "postAlbum")
	private byte[] postAlbum;
	
	@Column(name = "postStatus")
	private String postStatus;
	
	@Column(name = "postViewCount")
	private int postViewCount;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private Set<PostComment> postComments = new HashSet<PostComment>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private Set<PostLike> postLikes = new HashSet<PostLike>(); 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private Set<PostReport> postReports = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberNo")
	private Member member;
	
	public Post() {
		this.postStatus = "正常";
	}
	
	public String getPostAlbumBase64() {
	    return (this.postAlbum != null) ? Base64.getEncoder().encodeToString(this.postAlbum) : null;
	}
	public byte[] getPostAlbum() {
	    return postAlbum;
	}
	public void setPostAlbum(String postAlbum) {
	    if (postAlbum != null && !postAlbum.trim().isEmpty() && postAlbum.contains(",")) {
	        String[] split = postAlbum.split(",");
	        if (split.length > 1 && split[1] != null && !split[1].trim().isEmpty()) {
	            String imageDataBytes = split[1];
	            this.postAlbum = Base64.getDecoder().decode(imageDataBytes);
	        }
	    }
	}
}
