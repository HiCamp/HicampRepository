package tw.hicamp.activity.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LinePayService {
	private static final String LINE_PAY_URI = "/v3/payments/request"; 
    private static final String API_URL = "https://api-pay.line.me";
    private static final String CHANNEL_ID = "2000067423";
    private static final String CHANNEL_SECRET_KEY = "e2fc3cb4ed732e3a0920126c86ffd739";

//  LinePay付款: 
    public String makePayment(String activityName, Integer activityPeriodPrice,Integer activitySignupNo, Integer signupTotalAmount, Integer activityPeriodNo, Integer signupQuantity) 
    		throws InvalidKeyException, NoSuchAlgorithmException {  //無效金鑰或無法找到所需的加密算法的錯誤

    	RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LINE-ChannelId", CHANNEL_ID);
        headers.set("X-LINE-ChannelSecret", CHANNEL_SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String nonce = UUID.randomUUID().toString();
        String requestBody = createRequestBody(activityName, activityPeriodPrice, activitySignupNo, signupTotalAmount, activityPeriodNo, signupQuantity);
        String dataToEncrypt = CHANNEL_SECRET_KEY + LINE_PAY_URI + requestBody + nonce;  //計算HMAC值

        headers.set("X-LINE-Authorization-Nonce", nonce);
        headers.set("X-LINE-Authorization", calculateHMAC(dataToEncrypt, CHANNEL_SECRET_KEY));
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL + LINE_PAY_URI, entity, String.class);
        
//      如果回應的HTTP狀態碼是OK，則解析回應內容，提取相關資訊, 反之，則輸出"連線失敗"。
        if (response.getStatusCode() == HttpStatus.OK) {
        	String responseBody = response.getBody();

        	ObjectMapper objectMapper = new ObjectMapper();
        	JsonNode rootNode;
			try {
			rootNode = objectMapper.readTree(responseBody);

        	String returnCode = rootNode.path("returnCode").asText();
        	String returnMessage = rootNode.path("returnMessage").asText();
        	JsonNode infoNode = rootNode.path("info");

        	String webPaymentUrl = infoNode.path("paymentUrl").path("web").asText();
        	String appPaymentUrl = infoNode.path("paymentUrl").path("app").asText();
        	long transactionId = infoNode.path("transactionId").asLong();
        	String paymentAccessToken = infoNode.path("paymentAccessToken").asText();

        	System.out.println("Return Code: " + returnCode);
        	System.out.println("Return Message: " + returnMessage);
        	System.out.println("Web Payment URL: " + webPaymentUrl);
        	System.out.println("App Payment URL: " + appPaymentUrl);
        	System.out.println("Transaction ID: " + transactionId);
        	System.out.println("Payment Access Token: " + paymentAccessToken);
        	System.out.println("requestBody:" + requestBody);
        	return webPaymentUrl;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
        } else {
        	System.out.println("連線失敗");
        }
        return "";
    }
    
   
    public String createRequestBody(String activityName, Integer activityPeriodPrice,Integer activitySignupNo, Integer signupTotalAmount, Integer activityPeriodNo, Integer signupQuantity
    		) {
    	  return "{"
    		        +  "\"productName\": \"" + activityName + "\","
    		        +  "\"amount\": " + signupTotalAmount + ","
    		        + "\"currency\": \"TWD\","
    		        + "\"confirmUrl\": \"http://localhost:8080/HiCamp/activity/memberCheckSignupOrder\","
    		        +  "\"orderId\": " + activitySignupNo + ","
    		        + "\"packages\": ["
    		            + "{"
    		            +  "\"id\": " + activitySignupNo + ","
    		            +  "\"amount\": " + signupTotalAmount + ","
    		            +  "\"name\": \"" + activityName + "\","
    		            + "\"products\": ["
    		                + "{"
    		                +  "\"id\": " + activitySignupNo + ","
    		                +  "\"name\": \"" + activityName + "\","
    		                +  "\"quantity\": " + signupQuantity + ","
    		                +  "\"price\": " + activityPeriodPrice 
    		                + "}"
    		            + "]"
    		            + "}"
    		        + "],"
            + "\"redirectUrls\": {"
                + "\"confirmUrl\": \"http://localhost:8080/HiCamp/activity/memberCheckSignupOrder\","
                + "\"cancelUrl\": \"http://localhost:8080/HiCamp/activity/memberCheckSignupOrder\""
            + "}"
        + "}";
    }
    

//  取得計算資料的HMAC（Hash-based Message Authentication Code）字串
    private String calculateHMAC(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256"); //創建SecretKeySpec物件，使用金鑰的位元組表示。
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac);
    }
    
//  LinePay退款處理 (尚未處理)
    public String refundPayment(Long transactionId, Integer refundAmount) 
            throws InvalidKeyException, NoSuchAlgorithmException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-LINE-ChannelId", CHANNEL_ID);
        headers.set("X-LINE-ChannelSecret", CHANNEL_SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String nonce = UUID.randomUUID().toString();
        String requestBody = createRefundBody(refundAmount);
        String refundUri = "/v3/payments/" + transactionId + "/refund";
        String dataToEncrypt = CHANNEL_SECRET_KEY + refundUri + requestBody + nonce; 

        headers.set("X-LINE-Authorization-Nonce", nonce);
        headers.set("X-LINE-Authorization", calculateHMAC(dataToEncrypt, CHANNEL_SECRET_KEY));
        
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL + refundUri, entity, String.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(responseBody);

                String returnCode = rootNode.path("returnCode").asText();
                String returnMessage = rootNode.path("returnMessage").asText();
                JsonNode infoNode = rootNode.path("info");

                long refundTransactionId = infoNode.path("refundTransactionId").asLong();
                String refundTransactionDate = infoNode.path("refundTransactionDate").asText();

                System.out.println("Return Code: " + returnCode);
                System.out.println("Return Message: " + returnMessage);
                System.out.println("Refund Transaction ID: " + refundTransactionId);
                System.out.println("Refund Transaction Date: " + refundTransactionDate);
                return returnCode;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("連線失敗");
        }
        return "";
    }

    public String createRefundBody(Integer refundAmount) {
        return "{ \"refundAmount\": " + refundAmount + " }";
    }

}