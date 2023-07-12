package tw.hicamp.activity.dto;

import java.util.Date;

public class ActivityDtoForBackEndPage {

	private Integer activityNo;

	private String activityType;

	private String activityName;

	private String activityInfo;

	private String activityLocation;
	
	private Integer activityPeriodNo;

	private Date activityDepartureDate;

	private Date activityReturnDate;

	private Date signupDeadline;
	
	private Integer signupQuantity;
	
	private Integer activityPeriodQuota;
	
	private Integer activityPeriodPrice;
	
	public Integer getActivityNo() {
		return activityNo;
	}

	public void setActivityNo(Integer activityNo) {
		this.activityNo = activityNo;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(String activityInfo) {
		this.activityInfo = activityInfo;
	}

	public String getActivityLocation() {
		return activityLocation;
	}

	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
	}

	public Integer getActivityPeriodNo() {
		return activityPeriodNo;
	}

	public void setActivityPeriodNo(Integer activityPeriodNo) {
		this.activityPeriodNo = activityPeriodNo;
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

	public Integer getSignupQuantity() {
		return signupQuantity;
	}

	public void setSignupQuantity(Integer signupQuantity) {
		this.signupQuantity = signupQuantity;
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




}
