package tw.hicamp.campsite.model;

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
import lombok.Data;

@Data
@Entity
@Table(name="campsiteBooking")
public class CampsiteBooking {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bookingNo")
    private Integer bookingNo;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="campsiteNo")
    private Campsite campsite;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="campspaceNo")
    private Campspace campspace;
	
	@Column(name="booker")
	private String booker;
	
	@Column(name="bookerPhone")
	private String bookerPhone;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Column(name="bookingDate")
    private Date bookingDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name="checkinDate")
    private Date checkinDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name="checkoutDate")
    private Date checkoutDate;
	
	@Column(name="bookingCampspaceQuantity")
    private Integer bookingCampspaceQuantity;
	
	@Column(name="bookingAmount")
    private Integer bookingAmount;

}
