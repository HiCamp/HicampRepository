package tw.hicamp.product.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductPictureRepository extends JpaRepository<ProductPicture, Integer> {

	@Query(value = "select ProductPicNo from ProductPicture where ProductNo = :productNo", nativeQuery = true)
	List<Integer> findProductPictureByProductNo(@Param("productNo") int productNo);
	
}
