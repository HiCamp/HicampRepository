package tw.hicamp.product.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

	@Query(value = "select top (1) * from orders where memberNo= :memberNo order by orderDate DESC", nativeQuery = true)
	Orders findlastOrderByMember(@Param("memberNo") int memberNo);

	// 金額分析
	@Query(value = "select month(orderdate) as 'month', sum(orderTotalPrice) as 'sumPrice' from orders group by month(orderdate)", nativeQuery = true)
	List<Map<Integer, Integer>> findsumPriceGroupBymonth();

	@Query(value = "select * from orders where memberNo = :memberNo", nativeQuery = true)
	List<Orders> findOrdersByMemberNo(@Param("memberNo") int memberNo);
}
