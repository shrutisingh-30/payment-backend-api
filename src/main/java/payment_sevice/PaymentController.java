package payment_sevice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Payment Service!";
    }

    @PostMapping("/payments")
public ResponseEntity<String> createPayment(@Valid @RequestBody PaymentRequest request) {

    String result = paymentService.processPayment(request);

    if (result.startsWith("Invalid")) {
        return ResponseEntity.badRequest().body(result);
    }

    return ResponseEntity.status(201).body(result);
}
    @GetMapping("/payments")
    public java.util.List<Payment> getAllPayments() {
    return paymentService.getAllPayments();
}
    @GetMapping("/payments/{id}")
public Payment getPaymentById(
        @org.springframework.web.bind.annotation.PathVariable("id") Long id) {

    return paymentService.getPaymentById(id);
}
@PutMapping("/payments/{id}")
public Payment updatePayment(
        @org.springframework.web.bind.annotation.PathVariable("id") Long id,
        @Valid @RequestBody PaymentRequest request) {

    return paymentService.updatePayment(id, request);
}
@DeleteMapping("/payments/{id}")
public ResponseEntity<Void> deletePayment(
        @PathVariable("id") Long id) {

    paymentService.deletePayment(id);
    return ResponseEntity.noContent().build();
}
@PatchMapping("/payments/{id}/status")
public Payment updatePaymentStatus(
        @org.springframework.web.bind.annotation.PathVariable("id") Long id,
        @org.springframework.web.bind.annotation.RequestParam PaymentStatus status) {

    return paymentService.updatePaymentStatus(id, status);
}
}

