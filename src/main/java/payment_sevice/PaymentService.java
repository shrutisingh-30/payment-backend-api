package payment_sevice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String processPayment(PaymentRequest request) {


        Payment payment = new Payment();

        payment.setCustomerId(request.getCustomerId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        Payment savedPayment = paymentRepository.save(payment);

        return "Payment saved successfully! Payment ID: "
                + savedPayment.getId();
    }
    public java.util.List<Payment> getAllPayments() {
    return paymentRepository.findAll();
}
public Payment getPaymentById(Long id) {
    return paymentRepository.findById(id)
            .orElseThrow(() ->
                new PaymentNotFoundException("Payment not found with ID: " + id));
}
public Payment updatePayment(Long id, PaymentRequest request) {

    Payment payment = paymentRepository.findById(id)
            .orElseThrow(() ->
                new PaymentNotFoundException(
                    "Payment not found with ID: " + id));

    payment.setCustomerId(request.getCustomerId());
    payment.setAmount(request.getAmount());
    payment.setPaymentMethod(request.getPaymentMethod());

    return paymentRepository.save(payment);
}
@Transactional
public Payment updatePaymentStatus(Long id, PaymentStatus status) {

    Payment payment = paymentRepository.findById(id)
            .orElseThrow(() ->
                new PaymentNotFoundException(
                    "Payment not found with ID: " + id));

    // Check whether payment is already completed
    if (payment.getStatus() != PaymentStatus.PENDING) {
        throw new RuntimeException(
            "Payment status cannot be changed once it is completed."
        );
    }

    payment.setStatus(status);

    return paymentRepository.save(payment);
}
public void deletePayment(Long id) {

    Payment payment = paymentRepository.findById(id)
            .orElseThrow(() ->
                new PaymentNotFoundException(
                    "Payment not found with ID: " + id));

    paymentRepository.delete(payment);
}
}