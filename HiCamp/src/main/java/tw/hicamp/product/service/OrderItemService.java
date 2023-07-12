package tw.hicamp.product.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.hicamp.product.model.OrderItem;
import tw.hicamp.product.model.OrderItemRepository;

@Service
public class OrderItemService {
	
	@Autowired
	private OrderItemRepository oItemRepo;
	
	
	//取訂單明細
	public OrderItem getOrderItem(int orderNo) {
		return oItemRepo.findItemByOrderNo(orderNo);
	}
	
	//類別分析購買數量
	public List<Map<String, Integer>> findQuantityByType(){
		return oItemRepo.findtotalQuantityGroupByType();
	}
	
	//類別分析銷售金額
	public List<Map<String, Integer>> findSubtotalByType(){
		return oItemRepo.findsubtotalPriceGroupByType();
	}
	
	//類別排序分析購買數量及金額(取數量及金額)
	public List<Map<Integer, Integer>> findSubtotalQuantityGroupByType(){
		return oItemRepo.findSubtotalQuantityGroupByType();
	}
	
	//依據訂單編號查訂單明細list
	public List<OrderItem> getOrderItemsByOrderNo(int orderNo){
		return oItemRepo.findOrderItemsByOrderNo(orderNo);
	}
	

}
