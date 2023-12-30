package API.eparking.Conrollers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PromocodesControllerTest {

    private final String URI_TO_PROMOCODES = "/api/promocodes";
    private final String username = "petsa.yuri@gmail.com";
    private final String password = "1234";
    private final String credentials = username + ":" + password;
    private final String encodedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    private final String AUTHORIZATION = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllPromoCodesSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PROMOCODES)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
    }

    @Test
    void getAllPromoCodesSortingByAvailable() throws Exception {
        boolean available = false;

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI_TO_PROMOCODES + "?available=" + available)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void getAllPromoCodesWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI_TO_PROMOCODES))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getByTitleSuccess() throws Exception {
        String title = "4669e7";

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PROMOCODES + "/search")
                .param("title", title)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title));
    }

    @Test
    void getByTitleWithNonExistingTitle() throws Exception {
        String title = "test";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI_TO_PROMOCODES + "/search")
                        .param("title", title)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getByTitleWithoutAuthorization() throws Exception {
        String title = "4669e7";

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URI_TO_PROMOCODES + "/search")
                        .param("title", title))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getOnePromoCodeSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PROMOCODES + "/2")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("4669e7"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.days").value(1));
    }

    @Test
    void getOnePromoCodeWithNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PROMOCODES + "/9999")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getOnePromoCodeWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PROMOCODES + "/2"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void createPromoCodeSuccessWithPercent() throws Exception {
        int count = 100, percent = 10, days = 5;
        String body = "{\"count\": \"" + count + "\", \"percent\": \"" + percent + "\", \"days\": \"" + days + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URI_TO_PROMOCODES)
                .header(AUTHORIZATION, encodedCredentials)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(count))
                .andExpect(MockMvcResultMatchers.jsonPath("$.percent").value(percent))
                .andExpect(MockMvcResultMatchers.jsonPath("$.days").value(days));
    }

    @Test
    void createPromoCodeSuccessWithAmount() throws Exception {
        int count = 100, amount = 100, days = 5;
        String body = "{\"count\": \"" + count + "\", \"amount\": \"" + amount + "\", \"days\": \"" + days + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PROMOCODES)
                        .header(AUTHORIZATION, encodedCredentials)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(count))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.days").value(days));
    }

    @Test
    void createPromoCodeWithoutRequiredFieldInTheBody() throws Exception {
        int count = 100, days = 5;
        String body = "{\"count\": \"" + count + "\", \"days\": \"" + days + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PROMOCODES)
                        .header(AUTHORIZATION, encodedCredentials)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("The body is missing a required field"));
    }

    @Test
    void createPromoCodeWithoutAuthorization() throws Exception {
        int count = 100, percent = 10, days = 5;
        String body = "{\"count\": \"" + count + "\", \"percent\": \"" + percent + "\", \"days\": \"" + days + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PROMOCODES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void updatePromoCodeSuccess() throws Exception {
        int count = 20;
        String body = "{\"count\": \"" + count + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(URI_TO_PROMOCODES + "/2")
                .header(AUTHORIZATION, encodedCredentials)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(count));
    }



    @Test
    void deletePromoCode() {
    }
}