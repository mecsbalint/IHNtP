package com.mecsbalint.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mecsbalint.backend.controller.dto.JwtResponseDto;
import com.mecsbalint.backend.controller.dto.UserEmailPasswordDto;
import com.mecsbalint.backend.controller.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class IhntpBackendIT {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Test
    public void registration_usernameIsNotOccupied_responseStatus200() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Cow", "cow@email.com", "12345");

        mvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void login_emailAndPasswordExist_responseStatus201() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Kitten", "kitten@email.com", "password");
        var userEmailPasswordDto = new UserEmailPasswordDto("kitten@email.com", "password");

        mvc.perform(post("/api/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationDto)));

        mvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userEmailPasswordDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllGamesSummary_responseStatus200() throws Exception {
        mvc.perform(get("/api/games/all"))
                .andExpect(status().isOk());
    }

    @Test void getUserWishlist_noJwt_responseStatus401() throws Exception {
        mvc.perform(get("/api/user/games/wishlist"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserWishlist_userLoggedIn_responseStatus200() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Boci", "boci@email.com", "abcde");
        var userEmailPasswordDto = new UserEmailPasswordDto("boci@email.com", "abcde");

        mvc.perform(post("/api/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationDto)));

        String responseBody = mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEmailPasswordDto)))
                .andReturn().getResponse().getContentAsString();

        JwtResponseDto jwtResponseDto = objectMapper.createParser(responseBody).readValueAs(JwtResponseDto.class);
        String jwtToken = jwtResponseDto.jwt();

        mvc.perform(get("/api/user/games/wishlist")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}
