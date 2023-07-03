package API.eparking.Conrollers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    private final String PATH_TO_USERS = "/api/users";

    private final String username = "petsa.yuri@gmail.com";
    private final String password = "1234";
    private final String credentials = username + ":" + password;
    private final String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

    private final String PATH_TO_UPLOAD_IMAGES = System.getProperty("user.dir") + "/uploads/";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddUserSuccess() throws Exception  {
        int count = 3;
        String first_name = "test", last_name = "test", phone_number = String.valueOf(count), email = String.valueOf(count), password = "1234";
        String body = "{\"first_name\": \"" + first_name + "\", \"last_name\": \"" + last_name + "\", \"phone_number\": \"" + phone_number + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(PATH_TO_USERS + "/add")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value(first_name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value(last_name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value(phone_number))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    void testAddUserWithExistingPhone() throws Exception {
        String body = "{\"first_name\": \"test\", \"last_name\": \"test\", \"phone_number\": \"0992950357\", \"email\": \"ttt\", \"password\": \"1234\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post(PATH_TO_USERS + "/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Phone already used"));
    }

    @Test
    void testAddUserWithExistingEmail() throws Exception {
        String body = "{\"first_name\": \"test\", \"last_name\": \"test\", \"phone_number\": \"09929503576\", \"email\": \"test@gmail.com\", \"password\": \"1234\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post(PATH_TO_USERS + "/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Email already used"));
    }

    @Test
    void testGetByIdExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value("Yuri"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("Petsa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("petsa.yuri@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("0954410994"));
    }

    @Test
    void testGetByIdNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/9999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Not Found"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(8));
    }

    @Test
    void testGetAllUsersSortingByRole() throws Exception {
        String role = "user";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "?role=" + role))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value(role));
    }

    @Test
    void testGetAllUsersSortingByInvalidRole() throws Exception {
        String role = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "?role=" + role))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void testGetAllUsersSortingByIsBanned() throws Exception {
        boolean banned = false;
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "?is_banned=" + banned))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_banned").value(banned));
    }

    @Test
    void testGetAllUsersSortingByInvalidIsBanned() throws Exception {
        String banned = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "?is_banned=" + banned ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_banned").value("false"));
    }

    @Test
    void testSearchByEmptyField() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/search?test=test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    @Test
    void testSearchByEmail() throws Exception {
        String email = "test@gmail.com";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/search?email=" + email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    void testSearchByInvalidEmail() throws Exception {
        String email = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/search?email=" + email))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    @Test
    void testSearchByPhoneNumber() throws Exception {
        String phone_number = "0992950356";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/search?phone_number=" + phone_number))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value(phone_number));
    }

    @Test
    void testSearchByInvalidPhoneNumber() throws Exception {
        String phone_number = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/search?phone_number=" + phone_number))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    @Test
    void testUploadImageSuccess() throws Exception {
        String filename = "28a50223-9db4-495a-a853-d18e23119585.jpg";
        FileInputStream inputStream = new FileInputStream(PATH_TO_UPLOAD_IMAGES);
        MultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/5/upload?file=" + multipartFile))
                .andExpect(MockMvcResultMatchers.status().isOk());
              //  .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(filename))
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        String firstName = "Yuri", lastName = "Petsa", phoneNumber = "0954410994";
        String body = "{\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", \"phone_number\": \"" + phoneNumber + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(PATH_TO_USERS + "/1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value(phoneNumber));
    }

    @Test
    void testUpdateUserNonExistingId() throws Exception {
        String firstName = "", lastName = "", phoneNumber = "";
        String body = "{\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", \"phone_number\": \"" + phoneNumber + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(PATH_TO_USERS + "/9999").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Not Found"));
    }

    @Test
    void testUpdateUserWithBadFieldsInTheBody() throws Exception {
        String firstName = "Yuri";
        String body = "{\"first_name\": \"" + firstName + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.put(PATH_TO_USERS + "/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Bad fields in the body"));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        long id = 11;
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH_TO_USERS + "/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully deleted"));

        mockMvc.perform(MockMvcRequestBuilders.get(PATH_TO_USERS + "/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Not Found"));
    }

    @Test
    void testDeleteUserNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH_TO_USERS + "/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Not Found"));
    }

    @Test
    void testSetAdminToUserSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/setAdmin/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("admin"));
    }

    @Test
    void testSetAdminToUserWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/setAdmin/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("User has no rights"));
    }

    @Test
    void testSetAdminToUserNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/setAdmin/999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    @Test
    void testRemoveAdminFromUserSuccess() throws Exception  {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/removeAdmin/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("user"));
    }

    @Test
    void testRemoveAdminFromUserWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/removeAdmin/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("User has no rights"));
    }

    @Test
    void testRemoveAdminFromUserNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/removeAdmin/9999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    @Test
    void testBanToUserSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/banTo/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_banned").value(true));
    }

    @Test
    void testBanToUserWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/banTo/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("User has no rights"));
    }

    @Test
    void testBanToUserNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/banTo/9999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }

    @Test
    void testUnbanToUserSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/unbanTo/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_banned").value(false));
    }

    @Test
    void testUnbanToUserWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/unbanTo/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("User has no rights"));
    }

    @Test
    void testUnbanToUserNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH_TO_USERS + "/unbanTo/9999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }
}