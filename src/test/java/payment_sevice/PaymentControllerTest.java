
package payment_sevice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from Payment Service!"));
    }

    @Test
    void testCreatePaymentAPI() throws Exception {

        String requestBody = """
                {
                    "customerId": 101,
                    "amount": 500.50,
                    "paymentMethod": "UPI"
                }
                """;

        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString(
                                "Payment saved successfully"
                        )
                ));
    }

    @Test
    void testCreatePaymentWithInvalidAmount() throws Exception {

        String requestBody = """
                {
                    "customerId": 101,
                    "amount": -500,
                    "paymentMethod": "UPI"
                }
                """;

        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString(
                                "Payment amount must be greater than 0"
                        )
                ));
    }

    @Test
    void testDeletePaymentAPI() throws Exception {

        String requestBody = """
                {
                    "customerId": 101,
                    "amount": 500.50,
                    "paymentMethod": "UPI"
                }
                """;

        String response = mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = response.replaceAll("[^0-9]", "");

        mockMvc.perform(delete("/payments/" + id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
    @Test
void testGetPaymentByIdAPI() throws Exception {

    String requestBody = """
            {
                "customerId": 101,
                "amount": 500.50,
                "paymentMethod": "UPI"
            }
            """;

    String response = mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String id = response.replaceAll("[^0-9]", "");

    mockMvc.perform(get("/payments/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerId").value(101))
            .andExpect(jsonPath("$.paymentMethod").value("UPI"))
            .andExpect(jsonPath("$.status").value("PENDING"));
}
@Test
void testGetAllPaymentsAPI() throws Exception {

    String requestBody = """
            {
                "customerId": 101,
                "amount": 500.50,
                "paymentMethod": "UPI"
            }
            """;

    mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated());

    mockMvc.perform(get("/payments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(
                    org.hamcrest.Matchers.greaterThan(0)
            ));
}
@Test
void testGetPaymentNotFound() throws Exception {

    mockMvc.perform(get("/payments/999999999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString("Payment not found")
            ));
}
@Test
void testUpdatePaymentAPI() throws Exception {

    String createRequest = """
            {
                "customerId": 101,
                "amount": 500.50,
                "paymentMethod": "UPI"
            }
            """;

    String response = mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequest))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String id = response.replaceAll("[^0-9]", "");

    String updateRequest = """
            {
                "customerId": 202,
                "amount": 750.25,
                "paymentMethod": "CARD"
            }
            """;

    mockMvc.perform(put("/payments/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerId").value(202))
            .andExpect(jsonPath("$.amount").value(750.25))
            .andExpect(jsonPath("$.paymentMethod").value("CARD"));
}
@Test
void testUpdatePaymentStatusAPI() throws Exception {

    String requestBody = """
            {
                "customerId": 101,
                "amount": 500.50,
                "paymentMethod": "UPI"
            }
            """;

    String response = mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String id = response.replaceAll("[^0-9]", "");

    mockMvc.perform(patch("/payments/" + id + "/status")
            .param("status", "SUCCESS"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"));
}
@Test
void testCannotChangeCompletedPaymentStatus() throws Exception {

    String requestBody = """
            {
                "customerId": 101,
                "amount": 500.50,
                "paymentMethod": "UPI"
            }
            """;

    String response = mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String id = response.replaceAll("[^0-9]", "");

    mockMvc.perform(patch("/payments/" + id + "/status")
            .param("status", "FAILED"))
            .andExpect(status().isOk());

    mockMvc.perform(patch("/payments/" + id + "/status")
            .param("status", "SUCCESS"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString(
                            "Payment status cannot be changed"
                    )
            ));
}
@Test
void testUpdatePaymentNotFound() throws Exception {

    String updateRequest = """
            {
                "customerId": 202,
                "amount": 750.25,
                "paymentMethod": "CARD"
            }
            """;

    mockMvc.perform(put("/payments/999999999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateRequest))
            .andExpect(status().isNotFound())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString("Payment not found")
            ));
}
@Test
void testDeletePaymentNotFound() throws Exception {

    mockMvc.perform(delete("/payments/999999999"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString("Payment not found")
            ));
}
@Test
void testCreatePaymentWithInvalidCustomerId() throws Exception {

    String requestBody = """
            {
                "customerId": 0,
                "amount": 500.50,
                "paymentMethod": "UPI"
            }
            """;

    mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString(
                            "Customer ID must be greater than 0"
                    )
            ));
}
@Test
void testCreatePaymentWithBlankPaymentMethod() throws Exception {

    String requestBody = """
            {
                "customerId": 101,
                "amount": 500.50,
                "paymentMethod": " "
            }
            """;

    mockMvc.perform(post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(
                    org.hamcrest.Matchers.containsString(
                            "Payment method cannot be empty"
                    )
            ));
}
}
