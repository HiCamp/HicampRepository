package tw.hicamp.product.model;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class ProductDTO {
	
	private int productNo;
	private String productType;
	private String productName;
	private int productPrice;
	private int productQuantity;
	private String productInfo;
	private String productStutas;
	private byte[] productBigPicture;
	
	private int productPicNo;
	private List<Integer> productPisNos;
	private byte[] productPicture;
	private Product product;
	
	public ProductDTO() {
	}
	public ProductDTO(int productNo, String productType, String productName, int productPrice, int productQuantity,
			String productInfo, String productStutas, int productPicNo, byte[] productPicture, Product product) {
		this.productNo = productNo;
		this.productType = productType;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productQuantity = productQuantity;
		this.productInfo = productInfo;
		this.productStutas = productStutas;
		this.productPicNo = productPicNo;
		this.productPicture = productPicture;
		this.product = product;
	}
	
	
	
	
}
