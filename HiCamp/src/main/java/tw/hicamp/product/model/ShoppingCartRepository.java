package tw.hicamp.product.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

	@Query(value = "select * from shoppingCart where memberNo = :memberNo", nativeQuery = true)
	List<ShoppingCart> findcartIdBymemberNo(@Param("memberNo") int memberNo);
	
	@Query(value = "select * from shoppingCart where memberNo = :memberNo and productNo = :productNo", nativeQuery = true)
	ShoppingCart findByProductNo(@Param("productNo") int productNo, @Param("memberNo")int memberNo);
	
	@Query(value = "delete from shoppingCart where memberNo = :memberNo", nativeQuery = true)
	boolean delCartByMember(@Param("memberNo") int memberNo);
	
	@Query(value = "select count(itemQuantity) as countCart from shoppingCart where memberNo = :memberNo", nativeQuery = true)
	Integer countCartByMemberNo(@Param("memberNo") int memberNo);

	
	
	
}
