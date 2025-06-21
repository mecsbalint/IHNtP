package com.mecsbalint.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mecsbalint.backend.controller.dto.*;
import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.model.Publisher;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.DeveloperRepository;
import com.mecsbalint.backend.repository.GameRepository;
import com.mecsbalint.backend.repository.PublisherRepository;
import com.mecsbalint.backend.repository.TagRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class IhntpBackendIT {

    @Autowired
    private ObjectMapper objectMapper;

    private final GameRepository gameRepository;

    private final TagRepository tagRepository;

    private final DeveloperRepository developerRepository;

    private final PublisherRepository publisherRepository;

    @Value("${mecsbalint.app.file-upload-dir}")
    private String testDir;

    @Autowired
    private MockMvc mvc;

    @Autowired
    public IhntpBackendIT(GameRepository gameRepository, TagRepository tagRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.tagRepository = tagRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
    }

    @BeforeEach
    public void setupTestDirectory() throws IOException {
        new File(testDir).mkdirs();
    }

    @AfterEach
    public void cleanUpTestDirectory() throws IOException {
        Files.walk(Path.of(testDir)).sorted((a, b) -> b.compareTo(a)).map(Path::toFile).forEach(File::delete);
    }

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

        signupUser(userRegistrationDto);

        mvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userEmailPasswordDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllGamesSummary_OneGameExist_returnListOfOneGame() throws Exception {
        Game game = new Game();
        game.setName("One Game");
        game.setTags(new HashSet<>());
        gameRepository.save(game);

        String responseBody = mvc.perform(get("/api/games/all"))
                .andReturn().getResponse().getContentAsString();

        List<GameForListDto> games = objectMapper.createParser(responseBody).readValueAs(new TypeReference<List<GameForListDto>>() {});

        String expectedGameName = game.getName();
        String actualGameName = games.get(0).name();

        assertEquals(expectedGameName, actualGameName);
    }

    @Test void getUserWishlist_noJwt_responseStatus401() throws Exception {
        mvc.perform(get("/api/user/games/wishlist"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUserWishlist_userLoggedIn_responseStatus200() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Boci", "boci@email.com", "abcde");
        var userEmailPasswordDto = new UserEmailPasswordDto("boci@email.com", "abcde");

        signupUser(userRegistrationDto);

        String responseBody = loginUser(userEmailPasswordDto);

        String jwtToken = getJwtFromResponseBody(responseBody);

        mvc.perform(get("/api/user/games/wishlist")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserBacklog_userLoggedIn_responseStatus200() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Kuh", "kuh@email.de", "abcde");
        var userEmailPasswordDto = new UserEmailPasswordDto("kuh@email.de", "abcde");

        signupUser(userRegistrationDto);

        String responseBody = loginUser(userEmailPasswordDto);

        String jwtToken = getJwtFromResponseBody(responseBody);

        mvc.perform(get("/api/user/games/backlog")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    public void addGame_happyCaseWithNewFiles_responseStatusOk() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Kuh", "kuh@email.ch", "abcde");
        var userEmailPasswordDto = new UserEmailPasswordDto("kuh@email.ch", "abcde");

        signupUser(userRegistrationDto);
        String responseBody = loginUser(userEmailPasswordDto);
        String jwtToken = getJwtFromResponseBody(responseBody);

        Developer newDeveloper = new Developer();
        newDeveloper.setName("add game developer");
        Publisher newPublisher = new Publisher();
        newPublisher.setName("add game publisher");
        Tag newTag = new Tag();
        newTag.setName("add game tag");
        long newDeveloperId = developerRepository.save(newDeveloper).getId();
        long newPublisherId = publisherRepository.save(newPublisher).getId();
        long newTagId = tagRepository.save(newTag).getId();

        GameToAdd gameToAdd = new GameToAdd("new game", LocalDate.of(2020, 1, 15), "short description", "long description", Set.of(newDeveloperId), Set.of(newPublisherId), Set.of(newTagId));
        MockMultipartFile gameToAddJsonFile = new MockMultipartFile("game", "", "application/json", objectMapper.writeValueAsBytes(gameToAdd));

        mvc.perform(multipart("/api/games/add")
                        .file(gameToAddJsonFile)
                        .file(getMultipartImageFileMock("headerImg"))
                        .file(getMultipartImageFileMock("screenshots"))
                        .file(getMultipartImageFileMock("screenshots"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void editGame_happyCaseWithNewFiles_responseStatusOk() throws Exception {
        var userRegistrationDto = new UserRegistrationDto("Kuh", "kuh@email.at", "abcde");
        var userEmailPasswordDto = new UserEmailPasswordDto("kuh@email.at", "abcde");

        signupUser(userRegistrationDto);
        String responseBody = loginUser(userEmailPasswordDto);
        String jwtToken = getJwtFromResponseBody(responseBody);

        Game game = new Game();
        game.setName("Game to edit");
        game.setTags(new HashSet<>());
        long gameId = gameRepository.save(game).getId();

        Developer newDeveloper = new Developer();
        newDeveloper.setName("edit game developer");
        Publisher newPublisher = new Publisher();
        newPublisher.setName("edit game publisher");
        Tag newTag = new Tag();
        newTag.setName("edit game tag");
        long newDeveloperId = developerRepository.save(newDeveloper).getId();
        long newPublisherId = publisherRepository.save(newPublisher).getId();
        long newTagId = tagRepository.save(newTag).getId();

        GameToEdit gameToEdit = new GameToEdit("Game to Edit new name", LocalDate.of(2020, 1, 15), "short description", "long description", null, Set.of(), Set.of(newDeveloperId), Set.of(newPublisherId), Set.of(newTagId));
        MockMultipartFile gameToEditJsonFile = new MockMultipartFile("game", "", "application/json", objectMapper.writeValueAsBytes(gameToEdit));

        mvc.perform(multipart("/api/games/edit/" + gameId)
                        .file(gameToEditJsonFile)
                        .file(getMultipartImageFileMock("headerImg"))
                        .file(getMultipartImageFileMock("screenshots"))
                        .file(getMultipartImageFileMock("screenshots"))
                        .header("Authorization", "Bearer " + jwtToken)
                        .with(request -> {request.setMethod("PUT"); return request;})
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    private void signupUser(UserRegistrationDto userRegistrationDto) throws Exception {
        mvc.perform(post("/api/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationDto)));
    }

    private String loginUser(UserEmailPasswordDto userEmailPasswordDto) throws Exception {
        return mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEmailPasswordDto)))
                .andReturn().getResponse().getContentAsString();
    }

    private String getJwtFromResponseBody(String responseBody) throws IOException {
        JwtResponseDto jwtResponseDto = objectMapper.createParser(responseBody).readValueAs(JwtResponseDto.class);
        return jwtResponseDto.jwt();
    }

    private MockMultipartFile getMultipartImageFileMock(String name) {
        byte[] validPngImage = Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=");
        return new MockMultipartFile(name, "image.png", "image/png", validPngImage);
    }
}
