package tw.hicamp.campsite.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="campsite")
public class Campsite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="campsiteNo")
	private Integer campsiteNo;
	
	@Column(name="campsiteName")
	private String campsiteName;
	
	@Column(name="campsiteQuantity")
	private Integer campsiteQuantity;
	
	@Column(name="campsiteLocation")
	private String campsiteLocation;
	
	@Column(name="campsiteInfo")
	private String campsiteInfo;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "campsite",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<CampsitePicture> campsitePictures = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "campsite",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Campspace> campspaces = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "campsite",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<CampsiteBooking> campsiteBookings = new ArrayList<>();
}
