package tw.hicamp.activity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    public String getWeatherData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(getApiUrl(), String.class);
        return response.getBody();
    }

    private String getApiUrl() {
        return apiUrl + "?Authorization=" + apiKey + "&format=JSON";
    }
}