package payment_sevice;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PaymentRequest {

    @Positive(message = "Customer ID must be greater than 0")
    private int customerId;

    @Positive(message = "Payment amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Payment method cannot be empty")
    private String paymentMethod;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
    return amount;
}

public void setAmount(BigDecimal amount) {
    this.amount = amount;
}

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}