package mvc.codejava.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import mvc.codejava.entity.PaymentHistoryEntity;
import mvc.codejava.repository.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PaymentController {
    @Autowired
    PaymentHistoryRepository paymentHistoryRepository;
    @RequestMapping("/payment")
    public String viewPayment(Model model) {
        return "payment/index";
    }

    @RequestMapping("/payment-error")
    public String viewPaymentError(Model model) {
        return "payment/error";
    }

    @RequestMapping(value = "/thank-you", method = RequestMethod.GET)
    public String viewPaymentThanks(Model model) {
        return "payment/thank-you";
    }

    @RequestMapping(value = "/payment-success", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PaymentHistoryEntity> paymentSuccess(@RequestBody JsonNode fullResponse) {
        try {
            PaymentHistoryEntity paymentEntity = decodeJsonToPaymentEntity(fullResponse);
            if (paymentEntity != null) {
                paymentHistoryRepository.save(paymentEntity);
                return ResponseEntity.ok(paymentEntity);
            } else {
                return new ResponseEntity<>(new PaymentHistoryEntity(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new PaymentHistoryEntity(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private PaymentHistoryEntity decodeJsonToPaymentEntity(JsonNode jsonNode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //  JsonNode jsonNode = objectMapper.readTree(json);

            PaymentHistoryEntity paymentEntity = new PaymentHistoryEntity();
            paymentEntity.setOrderID(jsonNode.get("orderID").asText());
            paymentEntity.setPayerID(jsonNode.get("payerID").asText());
            paymentEntity.setPayerName(jsonNode.get("details").get("payer").get("name").get("given_name").asText());
            paymentEntity.setPayerAddress(jsonNode.get("details").get("payer").get("address").get("country_code").asText());
            paymentEntity.setPayerEmail(jsonNode.get("details").get("payer").get("email_address").asText());
            paymentEntity.setPaymentAmount(jsonNode.get("details").get("purchase_units").get(0).get("amount").get("value").asText());
            //paymentEntity.setPaymentCurrency(jsonNode.get("details").get("purchase_units").get(0).get("amount").get("currency_code").asText());
            paymentEntity.setPaymentStatus(jsonNode.get("details").get("status").asText());

            return paymentEntity;
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý exception nếu có
            return null;
        }
    }
}
