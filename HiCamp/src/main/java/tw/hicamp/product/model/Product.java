package tw.hicamp.product.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productNo;
	private String productType;
	private String productName;
	private int productPrice;
	private int productQuantity;
	private String productInfo;
	private String productStutas;
	private byte[] productBigPicture;

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductPicture> pruductPictures = new ArrayList<ProductPicture>();

	@Override
	public String toString() {
		return "Product [productName=" + productName + "]";
	}

	

}