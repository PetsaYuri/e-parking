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
class ReviewsControllerTest {

    private final String URI_TO_REVIEWS = "/api/reviews";
    private final String username = "petsa.yuri@gmail.com";
    private final String password = "1234";
    private final String credentials = username + ":" + password;
    private final String encodedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    private final String AUTHORIZATION = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getReviewWithExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS + "/2")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("Все було чудово"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade").value(5));
    }

    @Test
    void getReviewWithNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS + "/9999")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getReviewWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS + "/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getAllReviewsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }

    @Test
    void getAllReviewsSortingByGrade() throws Exception {
        int grade = 5;

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS + "?grade=" + grade)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].grade").value(grade));
    }

    @Test
    void getAllReviewsSortingByGradeWithInvalidGrade() throws Exception {
        String grade = "test";
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS + "?grade=" + grade)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid value grade"));
    }

    @Test
    void getAllReviewsWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_REVIEWS))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void addReviewSuccess() throws Exception {
        String fieldBody = "test";
        int grade = 1;
        String body = "{\"body\": \"" + fieldBody + "\", \"grade\": \"" + grade + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URI_TO_REVIEWS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value(fieldBody))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade").value(grade));
    }

    @Test
    void addReviewWithoutRequiredFieldInTheBody() throws Exception {
        int grade = 1;
        String body = "{\"grade\": \"" + grade + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_REVIEWS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Not required field in the body"));
    }

    @Test
    void addReviewWithoutAuthorization() throws Exception {
        String fieldBody = "test";
        int grade = 1;
        String body = "{\"body\": \"" + fieldBody + "\", \"grade\": \"" + grade + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_REVIEWS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void updateReviewSuccess() throws Exception {
        String fieldBody = "update review";
        int grade = 4;
        long id = 3L;
        String body = "{\"body\": \"" + fieldBody + "\", \"grade\": \"" + grade + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_REVIEWS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value(fieldBody))
                .andExpect(MockMvcResultMatchers.jsonPath("$.grade").value(grade));
    }

    @Test
    void updateReviewWithNonExistingId() throws Exception {
        String fieldBody = "update review";
        int grade = 4;
        Long id = 9999L;
        String body = "{\"body\": \"" + fieldBody + "\", \"grade\": \"" + grade + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_REVIEWS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateReviewWithoutAuthorization() throws Exception {
        String fieldBody = "update review";
        int grade = 4;
        Long id = 3L;
        String body = "{\"body\": \"" + fieldBody + "\", \"grade\": \"" + grade + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_REVIEWS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void deleteReviewSuccess() throws Exception {
        long id = 5;
        mockMvc.perform(MockMvcRequestBuilders
                .delete(URI_TO_REVIEWS + "/" + id)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully deleted"));
    }

    @Test
    void deleteReviewWithNonExistingId() throws Exception {
        long id = 9999;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URI_TO_REVIEWS + "/" + id)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteReviewWithoutAuthorization() throws Exception {
        long id = 5;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URI_TO_REVIEWS + "/" + id))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}