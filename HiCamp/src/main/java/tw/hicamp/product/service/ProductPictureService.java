package tw.hicamp.product.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tw.hicamp.product.model.Product;
import tw.hicamp.product.model.ProductRepository;
import tw.hicamp.product.model.ProductPicture;
import tw.hicamp.product.model.ProductPictureRepository;

@Service
public class ProductPictureService {

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private ProductPictureRepository productPictureRepo;

	// 新增照片
	public String addproductPics(MultipartFile[] files) throws IOException {
		
		Product product = new Product();

		List<ProductPicture> piclist = new ArrayList<>();

		for (MultipartFile pic : files) {
			ProductPicture pPic = new ProductPicture();
			byte[] picByte = pic.getBytes();
			pPic.setProductPicture(picByte);
			pPic.setProduct(product);
			piclist.add(pPic);
		}
		product.setPruductPictures(piclist); // 一set多

		productRepo.save(product);

		return "上傳OK!";
	}

	// 取照片
	public ResponseEntity<byte[]> getProductPics(Integer picId) {
		Optional<ProductPicture> optional = productPictureRepo.findById(picId);

		if (optional.isPresent()) {
			ProductPicture pic = optional.get();
			byte[] picFile = pic.getProductPicture();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picFile);
		}
		return null;
	}
	
	public List<Integer> getProductPic(int productNo){
		return productPictureRepo.findProductPictureByProductNo(productNo);
	}
	
	// 刪除照片
	public boolean delProductPic(Integer picId) {
		Optional<ProductPicture> optinal = productPictureRepo.findById(picId);
		if (optinal.isPresent()) {
			productPictureRepo.deleteById(picId);
			return true;
		}
		return false;
	}
}
