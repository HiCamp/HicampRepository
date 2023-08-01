package tw.hicamp.activity.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.hicamp.activity.dto.ActivityOSMDto;

@Service
public class OSMService {

    public Map<String, Double> getGeocodingFromAddress(String activityLocation) {
    	
		RestTemplate restTemplate = new RestTemplate();
	    
	    String encodedAddress = URLEncoder.encode(activityLocation, StandardCharsets.UTF_8);
	   
		ResponseEntity<String> response  = restTemplate.exchange("https://nominatim.openstreetmap.org/?addressdetails=1&q="+encodedAddress+"&format=json&limit=1", 
				HttpMethod.GET, null, String.class );
				 
	    String responseBody = response.getBody();
        System.out.println("responsebody"+responseBody);

        ObjectMapper mapper = new ObjectMapper();
        List<ActivityOSMDto> locationData = null;
        try {
        	   locationData = mapper.readValue(responseBody, new TypeReference<List<ActivityOSMDto>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (locationData == null || locationData.isEmpty()) {
        	throw new RuntimeException("No location data received from OpenStreetMap API for address: " + activityLocation);
        }
        ActivityOSMDto firstLocation = locationData.get(0);
        double lat = Double.parseDouble(firstLocation.getLat());
        double lon = Double.parseDouble(firstLocation.getLon());

        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("latitude", lat);
        coordinates.put("longitude", lon);

        return coordinates;
    }
}
