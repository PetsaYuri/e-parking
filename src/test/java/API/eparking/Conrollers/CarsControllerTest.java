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

@SpringBootTest
@AutoConfigureMockMvc
class CarsControllerTest {

    private final String URI_TO_CARS = "/api/cars";

    private final String username = "petsa.yuri@gmail.com";
    private final String password = "1234";
    private final String credentials = username + ":" + password;
    private final String encodedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    private final String AUTHORIZATION = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetById_ReturnCarDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_CARS + "/2").header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("blue"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numbers").value("AO1234AA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("C"));
    }

    @Test
    void testGetById_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_CARS + "/9999").header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetById_WithoutAuthorization_ReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_CARS + "/2"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testAdd_ReturnCarDTO() throws Exception {
        String numbers = "AO1235AB", color = "yellow";
        char type = 'A';
        String body = "{\"numbers\": \"" + numbers + "\", \"color\": \"" + color + "\", \"type\": \"" + type + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URI_TO_CARS)
                .header(AUTHORIZATION, encodedCredentials)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numbers").value(numbers))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(color))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(String.valueOf(type)));
    }

    @Test
    void testAdd_WithExistingNumbers_ReturnBadRequestStatus() throws Exception {
        String numbers = "AO1235AB", color = "yellow";
        char type = 'A';
        String body = "{\"numbers\": \"" + numbers + "\", \"color\": \"" + color + "\", \"type\": \"" + type + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_CARS)
                        .header(AUTHORIZATION, encodedCredentials)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("A car with this numbers already exists"));
    }

    @Test
    void testAdd_WithoutAuthorization_ReturnUnauthorizedStatus() throws Exception {
        String numbers = "AO1236AB", color = "yellow";
        char type = 'A';
        String body = "{\"numbers\": \"" + numbers + "\", \"color\": \"" + color + "\", \"type\": \"" + type + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_CARS)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testUpdate_ReturnCarDTO() throws Exception {
        String numbers = "AO5321AB", color = "green";
        char type = 'C';
        String body = "{\"numbers\": \"" + numbers + "\", \"color\": \"" + color + "\", \"type\": \"" + type + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_CARS + "/2")
                        .header(AUTHORIZATION, encodedCredentials)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numbers").value(numbers))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(color))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(String.valueOf(type)));
    }

    @Test
    void testUpdate_WithNonExistingId_ReturnNotFoundStatus() throws Exception  {
        String numbers = "AO1235AB", color = "green";
        char type = 'C';
        String body = "{\"numbers\": \"" + numbers + "\", \"color\": \"" + color + "\", \"type\": \"" + type + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_CARS + "/9999")
                        .header(AUTHORIZATION, encodedCredentials)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdate_WithInvalidCharType_ReturnBadRequestStatus() throws Exception {
        String type = "test";
        String body = "{\"type\": \"" + type + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_CARS + "/8")
                        .header(AUTHORIZATION, encodedCredentials)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUpdate_WithoutAuthorization_ReturnUnauthorizedStatus() throws Exception {
        String body = "{\"numbers\": \"test\", \"color\": \"test\", \"type\": \"test\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put(URI_TO_CARS + "/8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testDelete_ReturnBoolean() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(URI_TO_CARS + "/9")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(true)));
    }

    @Test
    void testDelete_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(URI_TO_CARS + "/9999")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDelete_WithoutAuthorization_ReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI_TO_CARS + "/7"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}