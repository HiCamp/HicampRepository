package tw.hicamp.campsite.model;

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
@Data
@Table(name="campsitePicture")
public class CampsitePicture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="campsitePictureNo")
	private Integer campsitePictureNo;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="campsiteNo")
	private Campsite campsite;
	
	@Lob
	@JsonIgnore
	@Column(name="campsitePicture")
	private byte[] campsitePicture;
	
}
