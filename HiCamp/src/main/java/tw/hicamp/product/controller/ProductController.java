package tw.hicamp.product.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.product.model.Product;
import tw.hicamp.product.model.ProductDTO;
import tw.hicamp.product.model.ProductPicture;
import tw.hicamp.product.service.ProductService;
import tw.hicamp.product.service.ShoppingCartService;
import tw.hicamp.product.service.ProductPictureService;

@Controller
public class ProductController {

	@Autowired
	private ProductService pService;
	@Autowired
	private ProductPictureService pPicService;
	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping("/productHome.test")
	@ResponseBody
	public List<Product> getAllProductsTest(Model m) {
		List<Product> product = pService.getAllProduct();
		m.addAttribute("products", product);
		return product;
	}

	// 後台主頁
	@GetMapping("/productHome")
	public String getAllProducts(Model m) {
		List<Product> product = pService.getAllProduct();
		m.addAttribute("products", product);
		return "product/productHome";
	}

	// 後台取一筆資料
	@GetMapping("/product/aProduct")
	public String getProduct(@RequestParam("productNo") int productNo, Model m) {

		Product product = pService.getProduct(productNo);

		List<Integer> productPic = pPicService.getProductPic(productNo);

		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductNo(product.getProductNo());
		productDTO.setProductType(product.getProductType());
		productDTO.setProductName(product.getProductName());
		productDTO.setProductPrice(product.getProductPrice());
		productDTO.setProductQuantity(product.getProductQuantity());
		productDTO.setProductInfo(product.getProductInfo());
		productDTO.setProductStutas(product.getProductStutas());
		productDTO.setProductBigPicture(product.getProductBigPicture());
		productDTO.setProductPisNos(productPic);

		System.out.println(productPic);

		m.addAttribute("product", productDTO);

		return "product/editProduct";
	}

	// 新增
	@ResponseBody
	@PostMapping("/product/addProduct")
	public String insertProduct2(@RequestParam("productName") String productName,
			@RequestParam("productType") String productType, @RequestParam("productInfo") String productInfo,
			@RequestParam("productPrice") int productPrice, @RequestParam("productQuantity") int productQuantity,
			@RequestParam("productStutas") String productStutas, @RequestParam("bigPic") MultipartFile bigPic,
			@RequestParam("files") MultipartFile[] files) throws IOException {

		Product product = new Product();
		product.setProductName(productName);
		product.setProductType(productType);
		product.setProductInfo(productInfo);
		product.setProductPrice(productPrice);
		product.setProductQuantity(productQuantity);
		product.setProductStutas(productStutas);
		product.setProductBigPicture(bigPic.getBytes());

		List<ProductPicture> photos = new ArrayList<>();

		for (MultipartFile file : files) {
			ProductPicture productPicture = new ProductPicture();
			productPicture.setProductPicture(file.getBytes());
			productPicture.setProduct(product);
			photos.add(productPicture);
		}

		product.setPruductPictures(photos);

		pService.addProduct(product);

		return "成功"; // 返回成功響應
	}

	// 修改
	@ResponseBody
	@PostMapping("/product/updateProduct")
	public Product updateProduct(@RequestParam("productNo") int productNo,
			@RequestParam("productName") String productName, @RequestParam("productType") String productType,
			@RequestParam("productInfo") String productInfo, @RequestParam("productPrice") int productPrice,
			@RequestParam("productQuantity") int productQuantity, @RequestParam("productStutas") String productStutas)
			throws IOException {

		Product product = new Product();
		product.setProductNo(productNo);
		product.setProductName(productName);
		product.setProductType(productType);
		product.setProductInfo(productInfo);
		product.setProductPrice(productPrice);
		product.setProductQuantity(productQuantity);
		product.setProductStutas(productStutas);

		pService.updateProductByNo(product);

		return product;
	}

	// 取照片s
	@ResponseBody
	@GetMapping("/product/pictures")
	public ResponseEntity<byte[]> getPics(@RequestParam("picID") Integer picId) {
		ResponseEntity<byte[]> productPics = pPicService.getProductPics(picId);
		return productPics;
	}

	// 取大照片
	@ResponseBody
	@GetMapping("/product/getBigPic")
	public ResponseEntity<byte[]> getMemberPhoto(@RequestParam("productNo") int productNo) {
		Product product = pService.getProduct(productNo);
		byte[] bigPic = product.getProductBigPicture();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bigPic);
	}

	// 更新大頭照
	@ResponseBody
	@PostMapping("/product/updateBigPic")
	public Integer updateBigPic(@RequestParam("productNo") int productNo,
			@RequestParam("bigPic") MultipartFile bigPic) {
		return pService.updateBicPic(productNo, bigPic);
	}

	// 新增照片
	@ResponseBody
	@PostMapping("/product/addPictures")
	public String addPic(@RequestParam("files") MultipartFile[] files) throws IOException {
		pPicService.addproductPics(files);

		return pPicService.addproductPics(files);
	}

	// 刪除照片
	@ResponseBody
	@GetMapping("/product/delPicture")
	public String delPicture(@RequestParam("picID") Integer picNo) {
		pPicService.delProductPic(picNo);
		return "刪除OK!!";
	}

	// 更新商品介紹照片
	@ResponseBody
	@PostMapping("/product/resetPic")
	public String resetPic(@RequestParam("productNo") Integer productNo,
			@RequestParam("resetPics") MultipartFile[] files) throws IOException {

		Product product = pService.getProduct(productNo);

		List<ProductPicture> photos = new ArrayList<>();
		for (MultipartFile file : files) {
			ProductPicture productPicture = new ProductPicture();
			productPicture.setProductPicture(file.getBytes());
			productPicture.setProduct(product);
			photos.add(productPicture);
		}
		product.setPruductPictures(photos);
		pService.addProduct(product);
		return "更新照片完成";

	}

	// 前台主頁
	@GetMapping("/shopping")
	public String getShopProducts(Model m, HttpSession session) {
		List<Product> productList = pService.getAllProduct();

		List<Product> upPorduct = new ArrayList<>();
		for (Product apro : productList) {
			if (apro.getProductStutas().equals("上架")) {
				upPorduct.add(apro);
			}
		}
		Object memberObj = session.getAttribute("memberNo");
		if (memberObj != null) {
			int memberNo = (int)memberObj;
			Integer countCart = shoppingCartService.countCart(memberNo);
			session.setAttribute("countCart", countCart);
		}
		m.addAttribute("productList", upPorduct);

		return "product/shopping";
	}
	// 前台主頁ajax
	@ResponseBody
	@GetMapping("/shopping/allproducts")
	public List<Product> getAllprProducts(){
		List<Product> productList = pService.getAllProduct();
		List<Product> upPorduct = new ArrayList<>();
		for (Product apro : productList) {
			if (apro.getProductStutas().equals("上架")) {
				upPorduct.add(apro);
			}
		}
		return upPorduct;
	}

	// 前台取一筆資料
	@GetMapping("/shopping/shopProduct")
	public String getShopProduct(@RequestParam("productNo") int productNo, Model m) {

		Product product = pService.getProduct(productNo);

		List<Integer> productPic = pPicService.getProductPic(productNo);

		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductNo(product.getProductNo());
		productDTO.setProductType(product.getProductType());
		productDTO.setProductName(product.getProductName());
		productDTO.setProductPrice(product.getProductPrice());
		productDTO.setProductQuantity(product.getProductQuantity());
		productDTO.setProductInfo(product.getProductInfo());
		productDTO.setProductStutas(product.getProductStutas());
		productDTO.setProductBigPicture(product.getProductBigPicture());
		productDTO.setProductPisNos(productPic);

		System.out.println(productPic);

		m.addAttribute("product", productDTO);

		return "product/shopProduct";
	}

	// 前台搜尋
	@GetMapping("/shopping/getProducts")
	public String getProducts(@RequestParam("productType") String productType, Model model) {
		List<Product> productList = pService.findByType(productType);

		List<Product> typePorduct = new ArrayList<>();
		for (Product products : productList) {
			if (products.getProductStutas().equals("上架") ) {
					typePorduct.add(products);
			}
		}
		System.out.println(typePorduct);
		
		model.addAttribute("typePorductList", typePorduct);
		return "product/selectByType";
	}
	
	// 前台依金額小到大排序
	@ResponseBody
	@GetMapping("/shopping/orderByPrice")
	public List<Product> orderByPrice(){
		List<Product> productList = pService.orderByPrice();
		List<Product> typePorduct = new ArrayList<>();
		for (Product products : productList) {
			if (products.getProductStutas().equals("上架") ) {
					typePorduct.add(products);
			}
		}
		return typePorduct;
	}
	// 前台依金額大到小排序
	@ResponseBody
	@GetMapping("/shopping/orderByPriceDESC")
	public List<Product> orderByPriceDESC(){
		return pService.orderByPriceDESC();
	}
	
	// 分頁功能
	public Page<Product> showProducts(@RequestParam(name = "p", defaultValue = "1") Integer pageNum){
		Page<Product> products = pService.findByPage(pageNum);
		return products;
	}

}
