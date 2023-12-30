package API.eparking.Conrollers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    private final String URI_TO_USERS = "/api/users";
    private final String username = "petsa.yuri@gmail.com";
    private final String password = "1234";
    private final String credentials = username + ":" + password;
    private final String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

    private final String PATH_TO_UPLOAD_IMAGES = "C:\\Users\\Yuri\\Desktop";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetById_ReturnUserDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value("Yuri"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("Petsa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("petsa.yuri@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value("1234567899"));
    }

    @Test
    void testGetById_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/9999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetAll_ReturnListUserDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(8));
    }

    @Test
    void testGetAll_SortingByRole_ReturnListUserDTO() throws Exception {
        String role = "user";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "?role=" + role))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value(role));
    }

    @Test
    void testGetAll_SortingByInvalidRole_ReturnEmptyList() throws Exception {
        String role = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "?role=" + role))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void testGetAll_SortingByIsBanned_ReturnListUserDTO() throws Exception {
        boolean banned = false;
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "?is_banned=" + banned))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_banned").value(banned));
    }

    @Test
    void testGetAll_SortingByInvalidIsBanned_ReturnListUserDTO() throws Exception {
        String banned = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "?is_banned=" + banned ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_banned").value("false"));
    }

    @Test
    void testSearch_ByEmail_ReturnUserDTO() throws Exception {
        String email = "test@gmail.com";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/search?email=" + email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    void testSearch_ByInvalidEmail_ReturnNotFoundStatus() throws Exception {
        String email = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/search?email=" + email))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testSearch_ByPhoneNumber_ReturnUserDTO() throws Exception {
        String phone_number = "1234567899";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/search?phone_number=" + phone_number))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value(phone_number));
    }

    @Test
    void testSearch_ByInvalidPhoneNumber_ReturnNotFoundStatus() throws Exception {
        String phone_number = "test";
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/search?phone_number=" + phone_number))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testSearch_ByEmptyField_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/search?test=test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testAdd_ReturnUserDTO() throws Exception  {
        String first_name = "test", last_name = "test", phone_number = "12345678997", email = "test@gmail1.com123456", password = "1234";
        String body = "{\"first_name\": \"" + first_name + "\", \"last_name\": \"" + last_name + "\", \"phone_number\": \"" + phone_number + "\", \"email\": \"" + email + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_USERS + "/add")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value(first_name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value(last_name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value(phone_number))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    void testAdd_WithExistingPhone_ReturnBadRequestStatus() throws Exception {
        String body = "{\"first_name\": \"test\", \"last_name\": \"test\", \"phone_number\": \"0992950352\", \"email\": \"tttt\", \"password\": \"1234\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post(URI_TO_USERS + "/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Phone already used"));
    }

    @Test
    void testAdd_WithExistingEmail_ReturnBadRequestStatus() throws Exception {
        String body = "{\"first_name\": \"test\", \"last_name\": \"test\", \"phone_number\": \"0\", \"email\": \"test@gmail.com\", \"password\": \"1234\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post(URI_TO_USERS + "/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Email already used"));
    }

    @Test
    void testUploadImage_ReturnUserDTO() throws Exception {
        String filename = "OIP.jpg";
        FileInputStream inputStream = new FileInputStream(PATH_TO_UPLOAD_IMAGES + "//" + filename);
        MultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(URI_TO_USERS + "/5/upload")
                        .file("file", multipartFile.getBytes())
                        .header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String uploadedFilename = result.getResponse().getContentAsString().split("\"image\":\"", 2)[1].split("\"")[0];
        assertTrue(new File(System.getProperty("user.dir") + "/uploads/" + uploadedFilename).exists());
    }

    @Test
    void testUploadImage_WithNonExistingUserId_ReturnNotFoundStatus() throws Exception {
        String filename = "OIP.jpg";
        FileInputStream inputStream = new FileInputStream(PATH_TO_UPLOAD_IMAGES + "//" + filename);
        MultipartFile multipartFile = new MockMultipartFile(filename, inputStream);

        mockMvc.perform(MockMvcRequestBuilders.multipart(URI_TO_USERS + "/9999/upload")
                        .file("file", multipartFile.getBytes())
                        .header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdate_ReturnUserDTO() throws Exception {
        String firstName = "Yuri", lastName = "Petsa", phoneNumber = "123456789910";
        String body = "{\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", \"phone_number\": \"" + phoneNumber + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(URI_TO_USERS + "/1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value(firstName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_number").value(phoneNumber));
    }

    @Test
    void testUpdate_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        String firstName = "", lastName = "", phoneNumber = "";
        String body = "{\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", \"phone_number\": \"" + phoneNumber + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.put(URI_TO_USERS + "/9999").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdate_WithoutRequiredFieldInTheBody_ReturnBadRequestStatus() throws Exception {
        String firstName = "Yuri";
        String body = "{\"first_name\": \"" + firstName + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.put(URI_TO_USERS + "/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Bad field in the body"));
    }

    //Error
    @Test
    void testDelete_ReturnString() throws Exception {
        long id = 29;
        mockMvc.perform(MockMvcRequestBuilders.delete(URI_TO_USERS + "/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully deleted"));

        mockMvc.perform(MockMvcRequestBuilders.get(URI_TO_USERS + "/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDelete_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI_TO_USERS + "/9999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testSetAdmin_ReturnUserDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/setAdmin/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("admin"));
    }

    @Test
    void testSetAdmin_WithoutAuthentication_ReturnForbiddenStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/setAdmin/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testSetAdmin_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/setAdmin/999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testRemoveAdmin_ReturnUserDTO() throws Exception  {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/removeAdmin/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("user"));
    }

    @Test
    void testRemoveAdmin_WithoutAuthentication_ReturnForbiddenStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/removeAdmin/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testRemoveAdmin_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/removeAdmin/9999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testBanToUser_ReturnUserDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/banTo/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_banned").value(true));
    }

    @Test
    void testBanToUser_WithoutAuthentication_ReturnForbiddenStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/banTo/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testBanToUser_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/banTo/9999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUnbanToUser_ReturnUserDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/unbanTo/2").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_banned").value(false));
    }

    @Test
    void testUnbanToUser_WithoutAuthentication_ReturnForbiddenStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/unbanTo/2"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testUnbanToUser_WithNonExistingId_ReturnNotFoundStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_TO_USERS + "/unbanTo/9999").header("Authorization", "Basic " + encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}