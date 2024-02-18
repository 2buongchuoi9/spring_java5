package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;

import daden.shopaa.config.VnpayConfig;
import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.req.CheckoutReq;
import daden.shopaa.entity.Order;
import daden.shopaa.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class VnpayController {
  private final OrderService orderService;

  // use test
  @GetMapping("/create-payment")
  public ResponseEntity<MainResponse<?>> createPayment(HttpServletRequest req) throws UnsupportedEncodingException {

    String vnp_Version = "2.1.0";
    String vnp_Command = "pay";
    String orderType = "other";
    // long amount = Integer.parseInt(req.getParameter("amount")) * 100;
    long amount = 100000 * 100;
    String bankCode = "NCB";
    // String bankCode = req.getParameter("bankCode");

    String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
    String vnp_IpAddr = VnpayConfig.getIpAddress(req);

    String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

    Map<String, String> vnp_Params = new HashMap<>();
    vnp_Params.put("vnp_Version", vnp_Version);
    vnp_Params.put("vnp_Command", vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(amount));
    vnp_Params.put("vnp_CurrCode", "VND");

    if (bankCode != null && !bankCode.isEmpty()) {
      vnp_Params.put("vnp_BankCode", bankCode);
    }
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
    vnp_Params.put("vnp_OrderType", orderType);
    vnp_Params.put("vnp_Locale", "vn");

    vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    List fieldNames = new ArrayList(vnp_Params.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator itr = fieldNames.iterator();
    while (itr.hasNext()) {
      String fieldName = (String) itr.next();
      String fieldValue = (String) vnp_Params.get(fieldName);
      if ((fieldValue != null) && (fieldValue.length() > 0)) {
        // Build hash data
        hashData.append(fieldName);
        hashData.append('=');
        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        // Build query
        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
        query.append('=');
        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        if (itr.hasNext()) {
          query.append('&');
          hashData.append('&');
        }
      }
    }
    String queryUrl = query.toString();
    String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;

    Map<String, String> result = new HashMap<>();
    result.put("url", paymentUrl);

    return ResponseEntity.ok().body(MainResponse.oke(result));
  }

  @GetMapping("/payment-info")
  public ResponseEntity<MainResponse<?>> transaction(
      @RequestParam(value = "vnp_TransactionStatus") String cc,
      @RequestParam(value = "vnp_OrderInfo") String orderId) throws JsonMappingException, JsonProcessingException {

    // check status payment
    String mes = VnpayConfig.getMessageIPN().keySet().stream()
        .map(v -> {
          if (v.equals(cc))
            return VnpayConfig.getMessageIPN().get(v);
          return "";
        })
        .filter(v -> !v.equals(""))
        .collect(Collectors.joining(" "));

    // giao dich that bai xoa order va tra lai du lieu cua product & discount
    if (cc.equals("00") && cc.equals("07")) {
      orderService.removeOrderAndReturnProductQuantityBecausePaymentFail(orderId);
      return ResponseEntity.ok().body(new MainResponse<>(HttpStatus.PAYMENT_REQUIRED, mes));
    }

    return ResponseEntity.ok().body(MainResponse.oke(mes, orderService.findOrderById(orderId)));
  }

}

// http://localhost:8080/vnpay_jsp/vnpay_return.jsp?
// vnp_Amount=10000000
// &vnp_BankCode=NCB
// &vnp_BankTranNo=VNP14300574
// &vnp_CardType=ATM
// &vnp_OrderInfo=Thanh+toan+don+hang%3A87296767
// &vnp_PayDate=20240218112724
// &vnp_ResponseCode=00
// &vnp_TmnCode=UD5RWEFZ
// &vnp_TransactionNo=14300574
// &vnp_TransactionStatus=00
// &vnp_TxnRef=87296767
// &vnp_SecureHash=8dbb14e9b4bcc4d4bd6b2e2cb451a641b80b122704e5d1967c0c399bf2a9e39bf75fad1b34b2f56da3d0caeddb50073a5cd1360b1ce6e77b0ea21ee14b12aae1
