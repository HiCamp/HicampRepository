package tw.hicamp.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "shoppingCart")
@Data
public class ShoppingCart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;
	private int memberNo;
	private int productNo;
	private int productPrice;
	private int itemQuantity;
}
