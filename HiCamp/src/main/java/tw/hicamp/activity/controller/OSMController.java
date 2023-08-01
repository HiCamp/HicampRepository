package tw.hicamp.activity.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.service.OSMService;

@Controller
public class OSMController {
	
	@Autowired
	private OSMService osmService;
	
	@GetMapping("/activity/mapOsmLeafLet")
	public String searchLocation(@RequestParam("activityNo")Integer activityNo, @RequestParam("activityLocation") String activityLocation, Model model) {
		Map<String, Double> coordinates = osmService.getGeocodingFromAddress(activityLocation);

		model.addAttribute("activityNo", activityNo);
		model.addAttribute("coordinates", coordinates);
		model.addAttribute("activityLocation", activityLocation);
		System.out.println("activityNo:" + activityNo);
		System.out.println("coordinates:" + coordinates);
		System.out.println("activityLocation:" + activityLocation);
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

