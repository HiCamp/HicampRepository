package tw.hicamp.product.model;

public class AanlysisDTO {

	private String productType;
	private String totalQuantity;
	private String subtotalPrice;

	public AanlysisDTO() {
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getSubtotalPrice() {
		return subtotalPrice;
	}

	public void setSubtotalPrice(String subtotalPrice) {
		this.subtotalPrice = subtotalPrice;
	}

}
