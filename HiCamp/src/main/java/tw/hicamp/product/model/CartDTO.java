package tw.hicamp.product.model;

import lombok.Data;

@Data
public class CartDTO {
	
	private int productNo;
//	private String productType;
	private String productName;
	private int productPrice;
	private int productQuantity;
	private String productInfo;
	
	private int cartId;
	private int memberNo;
	private int itemQuantity;
	
	private String memberName;
	private String memberEmail;
	private String memberPhone;
	private String memberAddress;
}
