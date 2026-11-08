package payment_sevice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentSeviceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;
@Test
void testGetPaymentById() {

    Payment payment = new Payment();
    payment.setCustomerId(101);
    payment.setAmount(new java.math.BigDecimal("500.50"));
    payment.setPaymentMethod("UPI");
    payment.setStatus(PaymentStatus.PENDING);

    when(paymentRepository.findById(1L))
            .thenReturn(java.util.Optional.of(payment));

    Payment result = paymentService.getPaymentById(1L);

    assertEquals(101, result.getCustomerId());
    assertEquals(new java.math.BigDecimal("500.50"), result.getAmount());
    assertEquals("UPI", result.getPaymentMethod());
}
@Test
void testGetPaymentByIdNotFound() {

    when(paymentRepository.findById(999L))
            .thenReturn(java.util.Optional.empty());

    assertThrows(
        PaymentNotFoundException.class,
        () -> paymentService.getPaymentById(999L)
    );
}
@Test
void testUpdatePaymentStatus() {

    Payment payment = new Payment();
    payment.setCustomerId(101);
    payment.setAmount(new java.math.BigDecimal("500.50"));
    payment.setPaymentMethod("UPI");
    payment.setStatus(PaymentStatus.PENDING);

    when(paymentRepository.findById(1L))
            .thenReturn(java.util.Optional.of(payment));

    when(paymentRepository.save(any(Payment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    Payment result = paymentService.updatePaymentStatus(
            1L, PaymentStatus.SUCCESS);

    assertEquals(PaymentStatus.SUCCESS, result.getStatus());
}
@Test
void testCannotChangeCompletedPaymentStatus() {

    Payment payment = new Payment();
    payment.setStatus(PaymentStatus.FAILED);

    when(paymentRepository.findById(2L))
            .thenReturn(java.util.Optional.of(payment));

    assertThrows(
        RuntimeException.class,
        () -> paymentService.updatePaymentStatus(
                2L, PaymentStatus.SUCCESS)
    );

    verify(paymentRepository, never()).save(any(Payment.class));
}
@Test
void testCreatePayment() {

    PaymentRequest request = new PaymentRequest();
    request.setCustomerId(101);
    request.setAmount(new java.math.BigDecimal("500.50"));
    request.setPaymentMethod("UPI");
    when(paymentRepository.save(any(Payment.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    paymentService.processPayment(request);

    org.mockito.ArgumentCaptor<Payment> captor =
            org.mockito.ArgumentCaptor.forClass(Payment.class);

    verify(paymentRepository).save(captor.capture());

    Payment savedPayment = captor.getValue();

    assertEquals(101, savedPayment.getCustomerId());
    assertEquals(new java.math.BigDecimal("500.50"),
            savedPayment.getAmount());
    assertEquals("UPI", savedPayment.getPaymentMethod());
    assertEquals(PaymentStatus.PENDING, savedPayment.getStatus());
}
@Test
void testGetAllPayments() {

    Payment payment1 = new Payment();
    payment1.setCustomerId(101);
    payment1.setAmount(new java.math.BigDecimal("500.50"));
    payment1.setPaymentMethod("UPI");

    Payment payment2 = new Payment();
    payment2.setCustomerId(102);
    payment2.setAmount(new java.math.BigDecimal("1000.00"));
    payment2.setPaymentMethod("CARD");

    when(paymentRepository.findAll())
            .thenReturn(java.util.List.of(payment1, payment2));

    java.util.List<Payment> payments =
            paymentService.getAllPayments();

    assertEquals(2, payments.size());
    verify(paymentRepository).findAll();
}
@Test
void testUpdatePayment() {

    Payment existingPayment = new Payment();
    existingPayment.setCustomerId(101);
    existingPayment.setAmount(new java.math.BigDecimal("500.00"));
    existingPayment.setPaymentMethod("UPI");
    existingPayment.setStatus(PaymentStatus.PENDING);

    PaymentRequest request = new PaymentRequest();
    request.setCustomerId(102);
    request.setAmount(new java.math.BigDecimal("1000.00"));
    request.setPaymentMethod("CARD");

    when(paymentRepository.findById(1L))
            .thenReturn(java.util.Optional.of(existingPayment));

    when(paymentRepository.save(any(Payment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    Payment updatedPayment = paymentService.updatePayment(1L, request);

    assertEquals(102, updatedPayment.getCustomerId());
    assertEquals(new java.math.BigDecimal("1000.00"),
            updatedPayment.getAmount());
    assertEquals("CARD", updatedPayment.getPaymentMethod());

    verify(paymentRepository).save(existingPayment);
}
@Test
void testDeletePayment() {

    Payment payment = new Payment();
    payment.setCustomerId(101);
    payment.setAmount(new java.math.BigDecimal("500.00"));
    payment.setPaymentMethod("UPI");

    when(paymentRepository.findById(1L))
            .thenReturn(java.util.Optional.of(payment));

    paymentService.deletePayment(1L);

    verify(paymentRepository).findById(1L);
    verify(paymentRepository).delete(payment);
}
@Test
void testDeletePaymentNotFound() {

    when(paymentRepository.findById(99L))
            .thenReturn(java.util.Optional.empty());

    assertThrows(
            PaymentNotFoundException.class,
            () -> paymentService.deletePayment(99L)
    );

    verify(paymentRepository, never()).delete(any(Payment.class));
}
}
