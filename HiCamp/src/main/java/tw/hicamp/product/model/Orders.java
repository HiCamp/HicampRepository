package tw.hicamp.product.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import tw.hicamp.member.model.Member;

@Entity @Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderNo;

	private Date orderDate;
	
	private String orderName;
	private String orderPhone;
	private String orderShipAddress;
	private String orderMessage;
	
	private int orderTotalPrice;
	private String orderPayWay;
	private String orderShipping;
	
	private String orderStatus;
	
	
	
	@ManyToOne
	@JoinColumn(name = "memberNo")
	private Member member;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	@Override
	public String toString() {
		return "Orders [orderNo=" + orderNo + ", orderDate=" + orderDate + ", orderName=" + orderName + ", orderPhone="
				+ orderPhone + ", orderShipAddress=" + orderShipAddress + ", orderMessage=" + orderMessage
				+ ", orderTotalPrice=" + orderTotalPrice + ", orderPayWay=" + orderPayWay + ", orderShipping="
				+ orderShipping + ", orderStatus=" + orderStatus + ", orderItems=" + orderItems + "]";
	}

	public Orders() {
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
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

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	
	
	

	


	

}