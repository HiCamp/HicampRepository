package tw.hicamp.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "productReview")
public class PruductReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productRevNo;
	private String productRev;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "productNo")
//	private Product product;

	
	
	

}