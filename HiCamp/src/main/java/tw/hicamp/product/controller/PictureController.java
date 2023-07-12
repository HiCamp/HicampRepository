package tw.hicamp.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import tw.hicamp.product.service.ProductPictureService;

@Controller
public class PictureController {
	
	@Autowired
	private ProductPictureService pPicService;
}
