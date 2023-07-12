package tw.hicamp.product.model;

import java.util.*;

import lombok.Data;

public class OrderDTO {
	
	private String memberName;
	private String memberEmail;
	private String memberPhone;
	
	private int orderNo;
	private String orderDate;
	private String orderName;
	private String orderPhone;
	private String orderShipAddress;
	private String orderMessage;
	private int orderTotalPrice;
	private String orderPayWay;
	private String orderShipping;
	private String orderStatus;
	
	private List<OrderItemDTO> orderItemDTO;

	public OrderDTO() {
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getOrderShipAddress() {
		return orderShipAddress;
	}

	public void setOrderShipAddress(String orderShipAddress) {
		this.orderShipAddress = orderShipAddress;
	}

	public String getOrderMessage() {
		return orderMessage;
	}

	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}

	public int getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(int orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public String getOrderPayWay() {
		return orderPayWay;
	}

	public void setOrderPayWay(String orderPayWay) {
		this.orderPayWay = orderPayWay;
	}

	public String getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(String orderShipping) {
		this.orderShipping = orderShipping;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderItemDTO> getOrderItemDTO() {
		return orderItemDTO;
	}

	public void setOrderItemDTO(List<OrderItemDTO> orderItemDTO) {
		this.orderItemDTO = orderItemDTO;
	}
	
	
	
	
}
