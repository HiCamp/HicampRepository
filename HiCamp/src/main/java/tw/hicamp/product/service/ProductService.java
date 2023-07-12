package tw.hicamp.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import tw.hicamp.product.model.Product;
import tw.hicamp.product.model.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository pRepo;

	public Product addProduct(Product product) {
		return pRepo.save(product);
	}

	public Product getProduct(Integer productNo) {
		Optional<Product> optinal = pRepo.findById(productNo);
		
		if (optinal.isPresent()) {
			return optinal.get();
		}
		return null;
		
	}

	public List<Product> getAllProduct() {
		return pRepo.findAll();
	}
	
	//修改
	@Transactional
	public Product updateProductByNo(Product product) {
		Optional<Product> optinal = pRepo.findById(product.getProductNo());
		
		if (optinal.isPresent()) {
			Product product1 = optinal.get();
			product1.setProductName(product.getProductName());
			product1.setProductPrice(product.getProductPrice());
			product1.setProductType(product.getProductType());
			product1.setProductQuantity(product.getProductQuantity());
			product1.setProductInfo(product.getProductInfo());
			product1.setProductStutas(product.getProductStutas());
			return product1;
		}
		System.out.println("no update data");
		return null;
	}
	
	//分頁功能
	public Page<Product> findByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber-1, 3, Sort.Direction.DESC, "productNo");
		
		Page<Product> page = pRepo.findAll(pgb);
		
		return page;
	}
	
	//更新首圖
	@Transactional
	public Integer updateBicPic(Integer productNo,MultipartFile productBigPic) {
		Product product = pRepo.findById(productNo).get();
		
		try {
			product.setProductBigPicture(productBigPic.getBytes());
			return productNo;
		} catch (IOException e) {
			System.out.println("updateBicPic false");
			e.printStackTrace();
			return productNo;
		}
	}
	
	// 前台分類搜尋
	public List<Product> findByType(String productType)	{
		return pRepo.findByProductType(productType);
	}
	
	// 金額小到大
	public List<Product> orderByPrice(){
		return pRepo.orderByproductPrice();
	}
	
	// 金額大到小
	public List<Product> orderByPriceDESC(){
		return pRepo.orderByproductPriceDESC();
	}

}
