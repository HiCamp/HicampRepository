package tw.hicamp.activity.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "activityPeriod")
//@Data
public class ActivityPeriod {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="activityPeriodNo")
	private Integer activityPeriodNo;
	
	@Column(name = "activityNo", insertable = false, updatable = false)
	private Integer activityNo;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="activityDepartureDate")
	private Date activityDepartureDate;
	
	
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="activityReturnDate")
	private Date activityReturnDate;
	
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="signupDeadline")
	private Date signupDeadline;
	
	@Column(name="activityQuota")
	private Integer activityPeriodQuota;
	
	@Column(name="activityPrice")
	private Integer activityPeriodPrice;
	
	@Column(name="signupQuantity")
	private Integer signupQuantity;
	
	@ManyToOne
	@JoinColumn(name="activityNo")
	@JsonBackReference
	private Activity activity;
	
	@OneToMany(mappedBy = "activityPeriod", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<ActivitySignup> activitySignup = new ArrayList<>();	
	
		
	public ActivityPeriod() {
	}


	public Integer getActivityPeriodNo() {
		return activityPeriodNo;
	}


	public void setActivityPeriodNo(Integer activityPeriodNo) {
		this.activityPeriodNo = activityPeriodNo;
	}


	public Integer getActivityNo() {
		return activityNo;
	}


	public void setActivityNo(Integer activityNo) {
		this.activityNo = activityNo;
	}


	public Date getActivityDepartureDate() {
		return activityDepartureDate;
	}


	public void setActivityDepartureDate(Date activityDepartureDate) {
		this.activityDepartureDate = activityDepartureDate;
	}


	public Date getActivityReturnDate() {
		return activityReturnDate;
	}


	public void setActivityReturnDate(Date activityReturnDate) {
		this.activityReturnDate = activityReturnDate;
	}


	public Date getSignupDeadline() {
		return signupDeadline;
	}


	public void setSignupDeadline(Date signupDeadline) {
		this.signupDeadline = signupDeadline;
	}


	public Integer getActivityPeriodQuota() {
		return activityPeriodQuota;
	}


	public void setActivityPeriodQuota(Integer activityPeriodQuota) {
		this.activityPeriodQuota = activityPeriodQuota;
	}


	public Integer getActivityPeriodPrice() {
		return activityPeriodPrice;
	}


	public void setActivityPeriodPrice(Integer activityPeriodPrice) {
		this.activityPeriodPrice = activityPeriodPrice;
	}


	public Integer getSignupQuantity() {
		return signupQuantity;
	}


	public void setSignupQuantity(Integer signupQuantity) {
		this.signupQuantity = signupQuantity;
	}


	public Activity getActivity() {
		return activity;
	}


	public void setActivity(Activity activity) {
		this.activity = activity;
	}


	public List<ActivitySignup> getActivitySignup() {
		return activitySignup;
	}


	public void setActivitySignup(List<ActivitySignup> activitySignup) {
		this.activitySignup = activitySignup;
	}

}
