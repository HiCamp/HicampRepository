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

@Entity
@Table(name = "activityPeriod")
@Data
public class ActivityPeriod {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="activityPeriodNo")
	private Integer activityPeriodNo;
	
	@Column(name = "activityNo", insertable = false, updatable = false)
	private Integer activityNo;
	
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
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
	
	@ManyToOne
	@JoinColumn(name="activityNo")
	@JsonBackReference
	private Activity activity;
		
	public ActivityPeriod() {
	}

}
