package API.eparking.Conrollers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class ParkingControllerTest {

    @Value("${parking.price.hour}")
    private String pricePerHour;

    @Value("${parking.price.day}")
    private String pricePerDay;

    @Value("${parking.count}")
    private int parkingCount;

    private final String URI_TO_PARKING = "/api/parking";
    private final String username = "petsa.yuri@gmail.com";
    private final String password = "1234";
    private final String credentials = username + ":" + password;
    private final String encodedCredentials = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    private final String AUTHORIZATION = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getByIdWithExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING + "/1")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price_per_hour").value(pricePerHour))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price_per_day").value(pricePerDay));
    }

    @Test
    void getByIdWithNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING + "/9999")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getByIdWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING + "/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getAllSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(parkingCount));
    }

    @Test
    void getAllSortingByAvailable() throws Exception {
        boolean is_available = true;
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING + "?available=" + is_available)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].is_available").value(is_available));
    }

    @Test
    void getAllWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void createParkingLotsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI_TO_PARKING + "/create")
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void editPriceSuccess() throws Exception {
        int pricePerHour = 45, pricePerDay = 950;
        String body = "{\"price_per_hour\": \"" + pricePerHour + "\", \"price_per_day\": \"" + pricePerDay + "\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put(URI_TO_PARKING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price_per_hour").value(pricePerHour))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price_per_day").value(pricePerDay));
    }

    @Test
    void editPriceWithNonExistingId() throws Exception {
        int pricePerHour = 45, pricePerDay = 950;
        String body = "{\"price_per_hour\": \"" + pricePerHour + "\", \"price_per_day\": \"" + pricePerDay + "\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void editPriceWithoutAuthorization() throws Exception {
        int pricePerHour = 45, pricePerDay = 950;
        String body = "{\"price_per_hour\": \"" + pricePerHour + "\", \"price_per_day\": \"" + pricePerDay + "\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void rentParkingLotSuccess() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 2L;
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(URI_TO_PARKING + "/rent/2")
                .content(body)
                .param("id_car", String.valueOf(id_car))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.busy_hours").value(busyHours))
                .andExpect(MockMvcResultMatchers.jsonPath("$.busy_days").value(busyDays))
                .andExpect(MockMvcResultMatchers.jsonPath("$.car.id").value(id_car));
    }

    @Test
    void rentParkingLotSuccessWithPromoCode() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 4L;
        String promoCode = "2ebcda";
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(URI_TO_PARKING + "/rent/4")
                .content(body)
                .param("id_car", String.valueOf(id_car))
                .param("promocode", promoCode)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.busy_hours").value(busyHours))
                .andExpect(MockMvcResultMatchers.jsonPath("$.busy_days").value(busyDays))
                .andExpect(MockMvcResultMatchers.jsonPath("$.car.id").value(id_car))
                .andExpect(MockMvcResultMatchers.jsonPath("$.promocode").value(promoCode));
    }

    @Test
    void rentParkingLotWithNonExistingPromoCode() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 2L;
        String promoCode = "test";
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/rent/2")
                        .content(body)
                        .param("id_car", String.valueOf(id_car))
                        .param("promocode", promoCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Promo code does not exists"));
    }

    @Test
    void rentParkingLotWithUsedPromoCode() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 3L;
        String promoCode = "d758c4";
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/rent/3")
                        .content(body)
                        .param("id_car", String.valueOf(id_car))
                        .param("promocode", promoCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("This promo code was used by the maximum number of people"));
    }

    @Test
    void rentParkingLotWithExpiredPromoCode() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 3L;
        String promoCode = "4669e7";
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/rent/3")
                        .content(body)
                        .param("id_car", String.valueOf(id_car))
                        .param("promocode", promoCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Promo code has expired"));
    }

    @Test
    void rentParkingLotWithNonExistingParkingId() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 3L;
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/rent/9999")
                        .content(body)
                        .param("id_car", String.valueOf(id_car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void rentParkingLotWithNonExistingCarId() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 9999L;
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/rent/3")
                        .content(body)
                        .param("id_car", String.valueOf(id_car))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void rentParkingLotWithoutAuthorization() throws Exception {
        int busyHours = 1, busyDays = 1;
        Long id_car = 3L;
        String body = "{\"busy_hours\": \"" + busyHours + "\", \"busy_days\": \"" + busyDays + "\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URI_TO_PARKING + "/rent/3")
                        .content(body)
                        .param("id_car", String.valueOf(id_car))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void removeCarFromParkingSuccess() throws Exception {
        Long parkingLotId = 3L;
        mockMvc.perform(MockMvcRequestBuilders
                .delete(URI_TO_PARKING + "/" + parkingLotId)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.car").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_available").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.busy_start").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.busy_end").isEmpty());
    }

    @Test
    void removeCarFromParkingWithNonExistingId() throws Exception {
        Long parkingLotId = 9999L;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URI_TO_PARKING + "/" + parkingLotId)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void removeCarFromParkingWithoutAuthorization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(URI_TO_PARKING + "1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void setPricePerHourSuccess() throws Exception {
        int price = 45;
        mockMvc.perform(MockMvcRequestBuilders
                .post(URI_TO_PARKING + "/properties/pricePerHour?value=" + price)
                .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void setPricePerHourWithInvalidPrice() throws Exception {
        String price = "test";
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PARKING + "/properties/pricePerHour?value=" + price)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void setPricePerHourWithoutAuthorization() throws Exception {
        int price = 45;
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PARKING + "/properties/pricePerHour?value=" + price))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void setPricePerDaySuccess() throws Exception {
        int price = 45;
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PARKING + "/properties/pricePerDay?value=" + price)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void setPricePerDayWithInvalidPrice() throws Exception {
        String price = "test";
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PARKING + "/properties/pricePerDay?value=" + price)
                        .header(AUTHORIZATION, encodedCredentials))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void setPricePerDayWithoutAuthorization() throws Exception {
        int price = 45;
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URI_TO_PARKING + "/properties/pricePerDay?value=" + price))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}