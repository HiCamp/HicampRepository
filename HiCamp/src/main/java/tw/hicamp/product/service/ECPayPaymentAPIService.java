package tw.hicamp.product.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class ECPayPaymentAPIService {
	
	
public String ecpayCheckout(String orderDate, String totalPrice, String itemName, int orderNo) {
	
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
		
	
		AllInOne all = new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo("2023" + "hicamp" + uuId);
		obj.setMerchantTradeDate(orderDate);
		obj.setTotalAmount(totalPrice);
		obj.setTradeDesc("交易描述");
		obj.setItemName(itemName);
	    // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok	
         obj.setReturnURL("http://localhost:8080/HiCamp/product/ECsucc");
		obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("http://localhost:8080/HiCamp/shopping");
        obj.setOrderResultURL("http://localhost:8080/HiCamp/orders/ECsucc?order="+ orderNo);
		String form = all.aioCheckOut(obj, null);
		
		return form;
	}
	
}
