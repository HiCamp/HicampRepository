package tw.hicamp.campsite.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="campspace")
public class Campspace {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="campspaceNo")
	private Integer campspaceNo;
	
	@Column(name="campspaceArea")
	private String campspaceArea;
	
	@Column(name="campspacePrice")
	private Integer campspacePrice;
	
	@Column(name="campspaceQuantity")
	private Integer campspaceQuantity;
	
	@Column(name="status")
	private String status;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="campsiteNo")
	private Campsite campsite;
	
	
}
