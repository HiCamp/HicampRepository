package tw.hicamp.activity.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.hicamp.activity.model.Activity;

@Controller
public class OSMController {
	 	 @PostMapping("/activity/mapOsmLeafLet")
	 	 public String locationSubmit(@ModelAttribute Activity activity, 
	 			 					  BindingResult bindingResult, 
	 			 					  @RequestParam("activityLocation") String activityLocation, Model model) {
	 	
	 			RestTemplate restTemplate = new RestTemplate();
	 		    
	 		    String encodedAddress = URLEncoder.encode(activityLocation, StandardCharsets.UTF_8);
	 		   
	 			ResponseEntity<String> response  = restTemplate.exchange("https://nominatim.openstreetmap.org/?addressdetails=1&q="+encodedAddress+"&format=json&limit=1", 
	 					HttpMethod.GET, null, String.class );
	 					 
	 		    String responseBody = response.getBody();
	 	        System.out.println(responseBody);
	 	        activity.setActivityLocation(activityLocation);

	 	        return "/activity/osmMap";
	 	      }
	 	     
	 @GetMapping("activity/getMap")
	 public String locationSubmit(Model model) {
	 	         
	  model.addAttribute("activity", new Activity());
	 	        
	 return "/activity/osmMap";
	 }
	 

	  
	public OSMController() {
	}

}

