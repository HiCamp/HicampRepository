package tw.hicamp.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.hicamp.activity.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class WeatherController {

		@Autowired
		private WeatherService weatherService;
	    
	    @GetMapping("/activity/weather")
	    public String getWeatherData(Model model) {
	        String weatherDataJson =  weatherService.getWeatherData();
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            Object weatherData = mapper.readValue(weatherDataJson, Object.class);
	            model.addAttribute("weatherData", weatherData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "/activity/weather";
	    }
	
}

