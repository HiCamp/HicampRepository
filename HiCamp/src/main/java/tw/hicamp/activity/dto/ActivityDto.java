package tw.hicamp.activity.dto;

import java.util.Date;

import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;

public class ActivityDto {

	private Integer activityNo;

	private String activityType;

	private String activityName;

	private String activityInfo;

	private Integer activityQuota;

	private Integer activityPrice;

	private Integer activityPicNo;

	private Integer activityPeriodNo;

	private Date activityDepartureDate;

	private Date activityReturnDate;

	private Date signupDeadline;
	
	private Integer RemainingQuota;
	
	public ActivityDto(Activity activity) {
		setActivityNo(activity.getActivityNo());
	    setActivityType(activity.getActivityType());
	    setActivityName(activity.getActivityName());
	    setActivityInfo(activity.getActivityInfo());
	    setActivityQuota(activity.getActivityQuota());
	    setActivityPrice(activity.getActivityPrice());
	}
	
	public void ActivityDtoAddPeriod(ActivityPeriod activityPeriod) {
	    setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
	    setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
	    setActivityReturnDate(activityPeriod.getActivityReturnDate());
	    setSignupDeadline(activityPeriod.getSignupDeadline());
	}
	
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

	public Integer getActivityQuota() {
		return activityQuota;
	}

	public void setActivityQuota(Integer activityQuota) {
		this.activityQuota = activityQuota;
	}

	public Integer getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(Integer activityPrice) {
		this.activityPrice = activityPrice;
	}

	public Integer getActivityPicNo() {
		return activityPicNo;
	}

	public void setActivityPicNo(Integer activityPicNo) {
		this.activityPicNo = activityPicNo;
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

	public Integer getRemainingQuota() {
		return RemainingQuota;
	}

	public void setRemainingQuota(Integer remainingQuota) {
		RemainingQuota = remainingQuota;
	}


}
