package tw.hicamp.activity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "activityPicture")
@Data
public class ActivityPicture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activityPictureNo")
	private Integer activityPictureNo;

	@Column(name = "activityNo", insertable = false, updatable = false)
	private Integer activityNo;

	@Column(name = "activityFileName")
	private String activityFileName;

	@Lob
	@JsonIgnore
	@Column(name = "activityPicture")
	private byte[] activityPicture;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "activityNo")
	private Activity activity;

	public ActivityPicture() {
	}
}
