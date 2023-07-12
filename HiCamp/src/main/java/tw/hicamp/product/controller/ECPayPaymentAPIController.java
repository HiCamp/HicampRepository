package tw.hicamp.product.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.product.model.OrderItem;
import tw.hicamp.product.model.Orders;
import tw.hicamp.product.model.Product;
import tw.hicamp.product.service.ECPayPaymentAPIService;
import tw.hicamp.product.service.OrdersService;
import tw.hicamp.product.service.ProductService;

@RestController
public class ECPayPaymentAPIController {

	@Autowired
	ECPayPaymentAPIService payService;
	@Autowired
	OrdersService ordersService;
	@Autowired
	ProductService productService;
	
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout(HttpSession session) {
//		int memberNo =11;
		Object memberNoObj = session.getAttribute("memberNo"); //
		int memberNo = (int) memberNoObj; //
		
		Orders memberNewOrder = ordersService.findnewOrderByMember(memberNo);
		Date orderDate= memberNewOrder.getOrderDate();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String payDate = ft.format(orderDate).toString();
		String totalPrice = Integer.toString(memberNewOrder.getOrderTotalPrice());
		List<OrderItem> orderItems = memberNewOrder.getOrderItems();
		StringJoiner productNameJoiner = new StringJoiner(", ");
		
		for (OrderItem orderItem : orderItems) {
			
			Product product = productService.getProduct(orderItem.getProductNo());
			String productName= product.getProductName();
			productNameJoiner.add(productName);
		}
		
		String productNames = productNameJoiner.toString();
		
		String aioCheckOutALLForm = payService.ecpayCheckout(payDate, totalPrice, productNames, memberNewOrder.getOrderNo());
		
		return aioCheckOutALLForm;
	}
}
