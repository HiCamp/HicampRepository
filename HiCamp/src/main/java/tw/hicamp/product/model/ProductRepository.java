package tw.hicamp.product.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query(value = "select * from product where productType =:productType", nativeQuery = true)
	List<Product> findByProductType(String productType);
	
	@Query(value = "  select * from product order by productPrice", nativeQuery = true)
	List<Product> orderByproductPrice();
	
	@Query(value = "  select * from product order by productPrice DESC", nativeQuery = true)
	List<Product> orderByproductPriceDESC();
	
}
