package tw.hicamp.product.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Query(value = "select top (1) * from orders where orderNo= :orderNo order by orderItem DESC", nativeQuery = true)
	OrderItem findItemByOrderNo(@Param("orderNo") int orderNo);
	
	//類別分析購買數量
	@Query(value = "SELECT product.productType, SUM(orderItem.itemQuantity) AS totalQuantity FROM product JOIN orderItem ON product.productNo = orderItem.productNo GROUP BY product.productType;", nativeQuery = true)
	List<Map<String,Integer>> findtotalQuantityGroupByType();
	
	//類別分析銷售金額
	@Query(value = "SELECT product.productType, SUM(orderItem.unitPrice) AS subtotalPrice FROM product JOIN orderItem ON product.productNo = orderItem.productNo GROUP BY product.productType;", nativeQuery = true)
	List<Map<String,Integer>> findsubtotalPriceGroupByType();
	
	
	//類別排序分析購買數量及金額
	@Query(value = "  SELECT SUM(orderItem.itemQuantity) AS totalQuantity,SUM(orderItem.unitPrice) AS subtotalPrice FROM product JOIN orderItem ON product.productNo = orderItem.productNo GROUP BY product.productType order by productType", nativeQuery = true)
	List<Map<Integer,Integer>> findSubtotalQuantityGroupByType();
	
	@Query(value = "  select * from orderItem where orderNo = :orderNo", nativeQuery = true)
	List<OrderItem> findOrderItemsByOrderNo(@Param("orderNo") int orderNo);
}
