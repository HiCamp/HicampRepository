package tw.hicamp.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductPages {
	
	//首頁
//	("productHome")
//	public String productHome() {
//		return "Product/productHome";
//	}
	
	
	//新增
	@GetMapping("/addProductData")
	public String processInsertProduct() {
		return "Product/addProduct";
	}
		
	//新增成功
	@RequestMapping("/succInsert")
	public String succInsert(Model m) {
		m.addAttribute("status","succeeded!");
		return "Product/insertProduct";
	}
	
	//查詢一筆
	@RequestMapping("/GetProductData")
	public String getProductData() {
		return "Product/getProduct";
	}
	
	
	
}
