import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import com.schoolmanagement.exception.PaymentException;
import com.schoolmanagement.model.StudentPayment;
import com.schoolmanagement.repository.StudentPaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private final StudentPaymentRepository paymentRepository;

    public PaymentService(StudentPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional(rollbackFor = {Exception.class})
    public String createOrder(String studentId, double amount) {
        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
            JSONObject options = new JSONObject();
            options.put("amount", (int) (amount * 100)); // Convert to paise
            options.put("currency", "INR");
            options.put("receipt", "txn_" + studentId);

            Order order = razorpay.orders.create(options);

       StudentPayment payment = new StudentPayment();
            payment.setStudentId(studentId);
            payment.setOrderId(order.get("id"));
            payment.setAmount(amount);
            payment.setStatus("PENDING");
            payment.setTimestamp(LocalDateTime.now());

            paymentRepository.save(payment);
            return order.toString();
        } catch (RazorpayException e) {
            throw new PaymentException("Error creating Razorpay order", e);
        }
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public String processPayment(String orderId, String paymentId) {
        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
            Payment payment = razorpay.payments.fetch(paymentId);

            if (!payment.get("status").equals("captured")) {
                throw new PaymentException("Payment not successful");
            }

            StudentPayment studentPayment = paymentRepository
                    .findAll()
                    .stream()
                    .filter(p -> p.getOrderId().equals(orderId))
                    .findFirst()
                    .orElseThrow(() -> new PaymentException("Order not found"));

            studentPayment.setPaymentId(paymentId);
            studentPayment.setStatus("SUCCESS");
            paymentRepository.save(studentPayment);

     return "Payment Successful";
        } catch (RazorpayException e) {
            throw new PaymentException("Payment processing failed", e);
        }
    }
}