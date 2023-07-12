package tw.hicamp.product.model;

import lombok.Data;

@Data
public class OrderItemDTO {
	
	private int itemQuantity;
	private int unitPrice;
	private String productName;
}
