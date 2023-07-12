package tw.hicamp.activity.dto;

import java.util.Date;

public class ActivitySignupOrderDto {
	
//	  訂單細項 (包含: 訂單資訊(訂單編號/報名日期/訂單總額/付款狀態)  PS. 一個人只會產生一張訂單, 不同人會有兩張訂單 (可以歸在同一個會員底下, 但若需幫家人報名必須填個資) 
//    會員資訊(姓名/性別/電話/地址/Email/身分證字號/生日(西元)/緊急聯絡人/緊急聯絡人電話)
//    活動資訊(活動類型/活動名稱/活動地點/期別編號/活動期別/活動照片(小圖)/出發日期/回程日期/活動單價)
	
	private Integer activitySignupNo;
	private Integer memberNo;
	private Integer activityPeriodNo;
	private Date signupDate;
	private Integer signupTotalAmount;
	private String signupPaymentStatus;
	private Integer signupQuantity;
	
	private String memberName;
	private String memberGender;
	private String memberPhone;
	private String memberAddress;
	private String memberEmail;
	private String memberId;
	private String memberBirthday;
	private String memberEmergencyContact;
	private String memberEmergencyPhone;	
	
	private Integer activityNo;
	private String activityType;
	private String activityName;
	private String activityLocation;

	private Date activityDepartureDate;
	private Date activityReturnDate;
	private Integer activityPeriodPrice;
		
	private Integer activityPictureNo;
	private byte[] activityPicture;
	
	
	public Integer getActivitySignupNo() {
		return activitySignupNo;
	}

	public void setActivitySignupNo(Integer activitySignupNo) {
		this.activitySignupNo = activitySignupNo;
	}

	public Integer getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(Integer memberNo) {
		this.memberNo = memberNo;
	}

	public Integer getActivityPeriodNo() {
		return activityPeriodNo;
	}

	public void setActivityPeriodNo(Integer activityPeriodNo) {
		this.activityPeriodNo = activityPeriodNo;
	}

	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

	public Integer getSignupTotalAmount() {
		return signupTotalAmount;
	}

	public void setSignupTotalAmount(Integer signupTotalAmount) {
		this.signupTotalAmount = signupTotalAmount;
	}

	public String getSignupPaymentStatus() {
		return signupPaymentStatus;
	}

	public void setSignupPaymentStatus(String signupPaymentStatus) {
		this.signupPaymentStatus = signupPaymentStatus;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberGender() {
		return memberGender;
	}

	public void setMemberGender(String memberGender) {
		this.memberGender = memberGender;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberBirthday() {
		return memberBirthday;
	}

	public void setMemberBirthday(String memberBirthday) {
		this.memberBirthday = memberBirthday;
	}

	public String getMemberEmergencyContact() {
		return memberEmergencyContact;
	}

	public void setMemberEmergencyContact(String memberEmergencyContact) {
		this.memberEmergencyContact = memberEmergencyContact;
	}

	public String getMemberEmergencyPhone() {
		return memberEmergencyPhone;
	}

	public void setMemberEmergencyPhone(String memberEmergencyPhone) {
		this.memberEmergencyPhone = memberEmergencyPhone;
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

	public String getActivityLocation() {
		return activityLocation;
	}

	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
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

	public Integer getActivityPeriodPrice() {
		return activityPeriodPrice;
	}

	public void setActivityPeriodPrice(Integer activityPeriodPrice) {
		this.activityPeriodPrice = activityPeriodPrice;
	}

	public Integer getActivityPictureNo() {
		return activityPictureNo;
	}

	public void setActivityPictureNo(Integer activityPictureNo) {
		this.activityPictureNo = activityPictureNo;
	}

	public byte[] getActivityPicture() {
		return activityPicture;
	}

	public void setActivityPicture(byte[] activityPicture) {
		this.activityPicture = activityPicture;
	}

	public Integer getSignupQuantity() {
		return signupQuantity;
	}

	public void setSignupQuantity(Integer signupQuantity) {
		this.signupQuantity = signupQuantity;
	}

	public ActivitySignupOrderDto() {
	}

}

