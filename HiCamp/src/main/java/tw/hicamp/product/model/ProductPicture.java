package tw.hicamp.product.model;

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
@Table(name = "productPicture")
public class ProductPicture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productPicNo")
	private int productPicNo;

	@Lob
	@JsonIgnore
	@Column(name = "productPicture")
	private byte[] productPicture;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "productNo")
	private Product product;

}