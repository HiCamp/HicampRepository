package tw.hicamp.activity.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name="activitySignup")
@Data
public class ActivitySignup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activitySignupNo")
	private Integer activitySignupNo;
	
	@Column(name = "memberNo")
	private Integer memberNo;
	
	@Column(name = "activityPeriodNo")
	private Integer activityPeriodNo;
	
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="signupDate")
	private Date signupDate;
	
	@Column(name = "signupTotalAmount")
	private Integer signupTotalAmount;
	
	@Column(name = "signupPaymentStatus")
	private String signupPaymentStatus;
	
	@Column(name = "signupQuantity")
	private Integer signupQuantity;
	
	@ManyToOne
	@JoinColumn(name="activityPeriodNo", insertable = false, updatable = false)
	@JsonBackReference
	private ActivityPeriod activityPeriod;
	
	@ManyToOne
	@JoinColumn(name="memberNo", insertable=false, updatable=false)
	@JsonBackReference
	private Member member;
	
	public ActivitySignup() {
	}

}



