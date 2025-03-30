import com.schoolmanagement.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

       @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> request) {
        String studentId = (String) request.get("studentId");
        double amount = Double.parseDouble(request.get("amount").toString());
        String order = paymentService.createOrder(studentId, amount);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/process-payment")
    public ResponseEntity<String> processPayment(@RequestBody Map<String, Object> request) {
        String orderId = (String) request.get("orderId");
        String paymentId = (String) request.get("paymentId");
        String response = paymentService.processPayment(orderId, paymentId);
        return ResponseEntity.ok(response);
    }
}