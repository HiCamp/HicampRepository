package tw.hicamp.product.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import jakarta.transaction.Transactional;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.model.MemberRepository;
import tw.hicamp.product.model.OrderItem;
import tw.hicamp.product.model.Orders;
import tw.hicamp.product.model.OrdersRepository;
import tw.hicamp.product.model.ShoppingCartRepository;


@Service
public class OrdersService {

	@Autowired
	private OrdersRepository oRepo;
	@Autowired
	public MemberRepository mRepo;
	@Autowired
	public ShoppingCartRepository sCartRepo;

	// 新增一筆訂單
	public Orders addOrder(Orders order) {
		return oRepo.save(order);
	}
	
	// 新增訂單明細
	public boolean newOrderItem(int orderNo, List<OrderItem> itemList) {
		Orders order = oRepo.findById(orderNo).get();
		order.setOrderItems(itemList);
		return true;
	}
	
	//查詢會員訂單
	public List<Orders> getMemberOrders(int memberNo){
		return oRepo.findOrdersByMemberNo(memberNo);
	}

	// 查詢一筆訂單
	public Orders getOrder(Integer orderNo) {
		return oRepo.findById(orderNo).get();
		
	}

	// 查詢全部訂單
	public List<Orders> getAllOrders() {
		return oRepo.findAll();
	}

	// 修改訂單狀態
	@Transactional
	public boolean updateOrderStutas(int orderNo, String stutas) {
		Orders order = oRepo.findById(orderNo).get();
		order.setOrderStatus(stutas);
		return true;
	}
	
	// 修改訂單 收件人、電話、地址、狀態
	@Transactional
	public boolean updateOrder(int orderNo, String orderName,String orderShipAddress, String orderPhone,String orderStatus) {
		Orders order = oRepo.findById(orderNo).get();
		order.setOrderName(orderName);
		order.setOrderShipAddress(orderShipAddress);
		order.setOrderPhone(orderPhone);
		order.setOrderStatus(orderStatus);
		
		return true;
	}

	// 刪除訂單
	public void delOrder(Integer orderNo) {
		oRepo.deleteById(orderNo);
	}
	
	// 刪除購物車
	public void delCartBymemberNo(int memberNo) {
		sCartRepo.delCartByMember(memberNo);
	}
	
	// 取會員最近一筆訂單
	public Orders findnewOrderByMember(int memberNo) {
		return oRepo.findlastOrderByMember(memberNo);
	}
	
	
	public Page<Orders> findByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber-1, 3, Sort.Direction.DESC, "added");
		
		Page<Orders> page = oRepo.findAll(pgb);
		
		return page;
	}
	
	public Member addmemberOder(Member member) {
		return mRepo.save(member);
	}
	
	// 分析月份金額
	public List<Map<Integer, Integer>> getAnalysisService(){
		return oRepo.findsumPriceGroupBymonth();
	}

}
