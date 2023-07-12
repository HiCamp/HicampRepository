package tw.hicamp.product.controller;

import java.io.IOException;
import java.security.PublicKey;
import java.util.*;
import java.text.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.MemberService;
import tw.hicamp.product.model.OrderDTO;
import tw.hicamp.product.model.OrderItem;
import tw.hicamp.product.model.OrderItemDTO;
import tw.hicamp.product.model.Orders;
import tw.hicamp.product.model.Product;
import tw.hicamp.product.model.ShoppingCart;
import tw.hicamp.product.model.AanlysisDTO;
import tw.hicamp.product.service.OrderItemService;
import tw.hicamp.product.service.OrdersService;
import tw.hicamp.product.service.ProductService;
import tw.hicamp.product.service.ShoppingCartService;

@Controller
public class OrderController {

	@Autowired
	private OrdersService oService;
	@Autowired
	private MemberService mService;
	@Autowired
	private ShoppingCartService sCartService;
	@Autowired
	private ProductService pService;
	@Autowired
	private OrderItemService oItemService;

	// 訂單後台主頁
	@GetMapping("/orderHome")
	public String getAllOrders(Model m) {
		List<Orders> allOrders = oService.getAllOrders();
		
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for (Orders order : allOrders) {
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setMemberName(order.getMember().getMemberName());
			
			SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String orserDate = ft.format(order.getOrderDate()).toString();
			orderDTO.setOrderDate(orserDate);
			orderDTO.setOrderNo(order.getOrderNo());
			orderDTO.setOrderTotalPrice(order.getOrderTotalPrice());
			orderDTO.setOrderStatus(order.getOrderStatus());
			orderDTOs.add(orderDTO);
		}
		
		m.addAttribute("orderDTOs", orderDTOs);
		return "product/orderHome";
	}

	// 新增訂單+訂單明細
	@ResponseBody
	@PostMapping("/orders/addOrder")
	public String addOrder(HttpSession session, @RequestParam("orderName") String orderName,
			@RequestParam("orderPhone") String orderPhone, @RequestParam("orderShipAddress") String orderShipAddress,
			@RequestParam("orderMessage") String orderMessage, @RequestParam("orderTotalPrice") int orderTotalPrice,
			@RequestParam("orderPayWay") String orderPayWay, @RequestParam("orderShipping") String orderShipping)
			throws IOException {
		

		Object memberNoObj = session.getAttribute("memberNo");
		if (memberNoObj != null) {
			int memberNo = (int) memberNoObj;
			Integer countCart = sCartService.countCart(memberNo);
			session.setAttribute("countCart", countCart);
//		int memberNo = 1;
		// 取得當前時間
		Date date = new Date();

		Member member = mService.findByNo(memberNo);

		ArrayList<Orders> orderslist = new ArrayList<>();
		Orders order = new Orders();
		order.setOrderDate(date);
		order.setOrderName(orderName);
		order.setOrderPhone(orderPhone);
		order.setOrderShipAddress(orderShipAddress);
		order.setOrderMessage(orderMessage);
		order.setOrderTotalPrice(orderTotalPrice);
		order.setOrderPayWay(orderPayWay);
		order.setOrderShipping(orderShipping);
		order.setOrderStatus("未付款");
		order.setMember(member);
		orderslist.add(order);
		member.setOrders(orderslist);

		oService.addmemberOder(member);

		Orders neworder = oService.findnewOrderByMember(memberNo);

		List<ShoppingCart> memberCart = sCartService.getMemberCart(memberNo);
		System.out.println("memberCart" + memberCart);

		List<OrderItem> newItemList = new ArrayList<>();

		for (ShoppingCart shoppingCart : memberCart) {
			OrderItem newitem = new OrderItem();
			newitem.setUnitPrice(shoppingCart.getProductPrice());
			newitem.setItemQuantity(shoppingCart.getItemQuantity());
			newitem.setProductNo(shoppingCart.getProductNo());
			newitem.setOrders(neworder);
			newItemList.add(newitem);
		}
		neworder.setOrderItems(newItemList);
		oService.addOrder(neworder);
		System.out.println("加入訂單成功");
		oService.delCartBymemberNo(memberNo);
		
		}

		return "加入訂單成功";
	}

	// 撈訂單資訊顯示於訂單完成頁面
	@GetMapping("/orders/getOrder")
	public String getOrder(HttpSession session, Model model) {
//		int memberNo = 11;
		Object memberNoObj = session.getAttribute("memberNo"); //
		if (memberNoObj != null) { //
			int memberNo = (int) memberNoObj; //

		OrderDTO orderDTO = new OrderDTO();
		Member member = mService.findByNo(memberNo);
		Orders memberOrder = oService.findnewOrderByMember(memberNo);
		System.out.println(member.getMemberName());

		orderDTO.setMemberName(member.getMemberName());
		orderDTO.setMemberEmail(member.getMemberEmail());
		orderDTO.setMemberPhone(member.getMemberPhone());

		Date orderDate = memberOrder.getOrderDate();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		orderDTO.setOrderDate(ft.format(orderDate));
		orderDTO.setOrderName(memberOrder.getOrderName());
		orderDTO.setOrderPhone(memberOrder.getOrderPhone());
		orderDTO.setOrderShipAddress(memberOrder.getOrderShipAddress());
		orderDTO.setOrderMessage(memberOrder.getOrderMessage());
		orderDTO.setOrderTotalPrice(memberOrder.getOrderTotalPrice());
		orderDTO.setOrderPayWay(memberOrder.getOrderPayWay());
		orderDTO.setOrderShipping(memberOrder.getOrderShipping());

		List<OrderItem> orderItemList = memberOrder.getOrderItems();

		ArrayList<OrderItemDTO> OderItemDTOList = new ArrayList<>();
		for (OrderItem orderItem : orderItemList) {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setItemQuantity(orderItem.getItemQuantity());
			orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
			Product product = pService.getProduct(orderItem.getProductNo());
			orderItemDTO.setProductName(product.getProductName());
			OderItemDTOList.add(orderItemDTO);
		}
		orderDTO.setOrderItemDTO(OderItemDTOList);
		model.addAttribute("orderDTO", orderDTO);
		
		} //

		return "product/newOrder";
	}

	// 更改訂單狀態
	@PostMapping("/orders/updateStutas")
	public boolean updateOrderStutas(int orderNo, String stutas) {
		return oService.updateOrderStutas(orderNo, stutas);
	}
	//ECsucc
	@PostMapping("/orders/ECsucc")
	public String ectest(@RequestParam("order") int order,HttpSession session, Model model) {
		System.out.println(order);
		oService.updateOrderStutas(order, "已付款");
//		int memberNo = 1;
		Object memberNoObj = session.getAttribute("memberNo"); //
		if (memberNoObj != null) { //
			int memberNo = (int) memberNoObj; //

		OrderDTO orderDTO = new OrderDTO();
		Member member = mService.findByNo(memberNo);
		Orders memberOrder = oService.findnewOrderByMember(memberNo);
		System.out.println("MemberName"+ member.getMemberName());

		orderDTO.setMemberName(member.getMemberName());
		orderDTO.setMemberEmail(member.getMemberEmail());
		orderDTO.setMemberPhone(member.getMemberPhone());

		Date orderDate = memberOrder.getOrderDate();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		orderDTO.setOrderDate(ft.format(orderDate));
		orderDTO.setOrderName(memberOrder.getOrderName());
		orderDTO.setOrderPhone(memberOrder.getOrderPhone());
		orderDTO.setOrderShipAddress(memberOrder.getOrderShipAddress());
		orderDTO.setOrderMessage(memberOrder.getOrderMessage());
		orderDTO.setOrderTotalPrice(memberOrder.getOrderTotalPrice());
		orderDTO.setOrderPayWay(memberOrder.getOrderPayWay());
		orderDTO.setOrderShipping(memberOrder.getOrderShipping());

		List<OrderItem> orderItemList = memberOrder.getOrderItems();

		ArrayList<OrderItemDTO> OderItemDTOList = new ArrayList<>();
		for (OrderItem orderItem : orderItemList) {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setItemQuantity(orderItem.getItemQuantity());
			orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
			Product product = pService.getProduct(orderItem.getProductNo());
			orderItemDTO.setProductName(product.getProductName());
			OderItemDTOList.add(orderItemDTO);
		}
		orderDTO.setOrderItemDTO(OderItemDTOList);
		model.addAttribute("orderDTO", orderDTO);
//			model.addAttribute("OderItemDTOList", OderItemDTOList);
		} //
		
		
		return "product/ECpaySucc";
	}
	// 取一筆訂單+明細 orderHome
	@ResponseBody
	@GetMapping("/orders/editOrder")
	public OrderDTO editOrder(int orderNo) {
		
		Orders memberOrder = oService.getOrder(orderNo);
		
		ArrayList<OrderItemDTO> OderItemDTOList = new ArrayList<>();
		for (OrderItem orderItem : memberOrder.getOrderItems()) {
			OrderItemDTO orderItemDTO = new OrderItemDTO();
			orderItemDTO.setItemQuantity(orderItem.getItemQuantity());
			orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
			Product product = pService.getProduct(orderItem.getProductNo());
			orderItemDTO.setProductName(product.getProductName());
			OderItemDTOList.add(orderItemDTO);
		}
		
		OrderDTO editOrderDTO = new OrderDTO();
		editOrderDTO.setOrderNo(orderNo);
		
		editOrderDTO.setOrderName(memberOrder.getOrderName());
		editOrderDTO.setOrderPhone(memberOrder.getOrderPhone());
		editOrderDTO.setOrderShipping(memberOrder.getOrderShipping());
		editOrderDTO.setOrderShipAddress(memberOrder.getOrderShipAddress());
		editOrderDTO.setOrderMessage(memberOrder.getOrderMessage());
		editOrderDTO.setOrderPayWay(memberOrder.getOrderPayWay());
		editOrderDTO.setOrderTotalPrice(memberOrder.getOrderTotalPrice());
		editOrderDTO.setOrderStatus(memberOrder.getOrderStatus());
		
		editOrderDTO.setOrderItemDTO(OderItemDTOList);
		
		return editOrderDTO;
	}
	
	// 編輯訂單+明細
	@ResponseBody
	@PostMapping("/orders/setOrder")
	public boolean setOrder(@RequestParam("orderNo") int orderNo,
							@RequestParam("orderName") String orderName,
							@RequestParam("orderShipAddress") String orderShipAddress,
							@RequestParam("orderPhone") String orderPhone,
							@RequestParam("orderStatus") String orderStatus) {
//		System.out.println(orderNo+","+orderName+","+orderShipAddress+","+orderPhone+","+orderStatus);
		oService.updateOrder(orderNo, orderName, orderShipAddress, orderPhone, orderStatus);
		return true;
	}
	
	//進入分析圖表
	@GetMapping("/orders/productAnalysis")
	public String goAnalysis() {
		return "product/productAnalysis";
	}
	
	//分析圖表 每月銷售額
	@ResponseBody
	@GetMapping("/orders/analysis")
	public List<Map<Integer, Integer>> getAnalysis(Model model) {
		return oService.getAnalysisService();
	}
	
	//分析圖表 以類別分析
	@ResponseBody
	@GetMapping("/orders/analysisByType")
	public List<Map<Integer, Integer>> analysisQangSubtotalByType(){
		return oItemService.findSubtotalQuantityGroupByType();
	}
	
	// 會員取個人All訂單
	@GetMapping("/orders/memberOrders")
	@ResponseBody
	public List<OrderDTO> getMemberOrders(HttpSession session){
		Object memberNoObj = session.getAttribute("memberNo"); //
		if (memberNoObj != null) { //
			int memberNo = (int) memberNoObj; //
			List<Orders> memberOrders = oService.getMemberOrders(memberNo);
			
			List<OrderDTO> memberOrdersDTOlist = new ArrayList<>();
			for (Orders memberOrder : memberOrders) {
				OrderDTO orderDTO = new OrderDTO();
				Date orderDate = memberOrder.getOrderDate();
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
				orderDTO.setOrderDate(ft.format(orderDate));
				orderDTO.setOrderNo(memberOrder.getOrderNo());
				orderDTO.setOrderStatus(memberOrder.getOrderStatus());
				orderDTO.setOrderName(memberOrder.getOrderName());
				orderDTO.setOrderShipAddress(memberOrder.getOrderShipAddress());
				orderDTO.setOrderTotalPrice(memberOrder.getOrderTotalPrice());
				
				ArrayList<OrderItemDTO> OderItemDTOList = new ArrayList<>();
				for (OrderItem orderItem : memberOrder.getOrderItems()) {
					OrderItemDTO orderItemDTO = new OrderItemDTO();
					orderItemDTO.setItemQuantity(orderItem.getItemQuantity());
					orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
					Product product = pService.getProduct(orderItem.getProductNo());
					orderItemDTO.setProductName(product.getProductName());
					OderItemDTOList.add(orderItemDTO);
				}
				orderDTO.setOrderItemDTO(OderItemDTOList);
				memberOrdersDTOlist.add(orderDTO);
				
			}
			return memberOrdersDTOlist;
		}
		
		return null;
	}
	
	
	

}

