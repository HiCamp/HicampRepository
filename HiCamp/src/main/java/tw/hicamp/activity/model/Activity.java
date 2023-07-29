package tw.hicamp.activity.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import lombok.Data;

@Entity
@Table(name = "activity")
@Data
public class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activityNo")
	private Integer activityNo;
	
	@Column(name = "activityType")
	private String activityType;
	
	@Column(name = "activityName")
	private String activityName;
	
	@Column(name = "activityLocation")
	private String activityLocation;
	
	@Column(name = "activityInfo")
	private String activityInfo;
	
	@Column(name = "activityQuota")
	private Integer activityQuota;
	
	@Column(name = "activityPrice")
	private Integer activityPrice;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ActivityPicture> activityPictures = new ArrayList<>();	
	
	@JsonManagedReference
	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ActivityPeriod> activityPeriods = new ArrayList<>();	
	
	public Activity() {
	}

}
