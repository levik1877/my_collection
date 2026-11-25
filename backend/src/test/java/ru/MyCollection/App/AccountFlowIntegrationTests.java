package ru.MyCollection.App;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.MyCollection.App.model.RoleEntity;
import ru.MyCollection.App.repository.RoleRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.config.import=",
        "spring.datasource.url=jdbc:h2:mem:account_flow;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS my_collection",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=false",
        "jwt.app.secret=isolated-test-secret-with-at-least-32-bytes-only",
        "jwt.app.lifetime=3600000"
})
@AutoConfigureMockMvc
class AccountFlowIntegrationTests {
    @Autowired MockMvc mvc;
    @Autowired RoleRepository roles;
    @Autowired ObjectMapper mapper;

    @BeforeEach
    void roleExists() {
        if (roles.findByName("ROLE_USER").isEmpty()) roles.save(new RoleEntity(null, "ROLE_USER", false));
    }

    @Test
    void registrationLoginProfileAndRepeatLogin() throws Exception {
        var name = "collector_" + UUID.randomUUID().toString().substring(0, 8);
        var registration = mvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON).content("""
                {"username":"%s","email":"%s@example.test","password":"TestPassword123","confirmPassword":"TestPassword123"}
                """.formatted(name, name))).andExpect(status().isOk()).andReturn();
        long id = mapper.readTree(registration.getResponse().getContentAsString()).get("id").asLong();
        String token = login(name);
        mvc.perform(get("/user/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.user.id").value(id))
                .andExpect(jsonPath("$.user.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.profile").isEmpty());
        String body = """
                {"userId":%d,"publicName":"My collection","description":"Collecting coins"}
                """.formatted(id);
        mvc.perform(post("/registration/create_profile").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.publicName").value("My collection"));
        mvc.perform(get("/user/me").header("Authorization", "Bearer " + login(name)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.profile.description").value("Collecting coins"));
        mvc.perform(post("/registration/create_profile").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
        mvc.perform(post("/registration/create_profile").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body.replace("\"userId\":" + id, "\"userId\":" + (id + 1000))))
                .andExpect(status().isForbidden());
    }

    @Test
    void accountEndpointsRequireAuthentication() throws Exception {
        mvc.perform(get("/user/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/user/me").header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/registration/create_profile").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"publicName\":\"Test\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"missing_user\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    private String login(String name) throws Exception {
        var result = mvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + name + "\",\"password\":\"TestPassword123\"}"))
                .andExpect(status().isOk()).andReturn();
        return mapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }
}
