package com.qiao.assetmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jm;

    String adminToken;
    String operatorToken;

    @BeforeEach
    public void login() throws Exception {
        adminToken = login("admin", "password");
        operatorToken = login("user", "password");
    }

    String login(String username, String password) throws Exception {
        var res = mvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jm.writeValueAsString
                                        (Map.of("username", username
                                                , "password", password)
                                        )
                                )

                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return jm.readTree(res).get("result").asString();
    }

    String bearer(String token) {
        return "Bearer " + token;
    }


    // authN

    @Test
    void noToken_is401() throws Exception {
        mvc.perform(
                        get("/api/equipment")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tamperedToken_is401() throws Exception {
        mvc.perform(
                        get("/api/equipment")
                                .header("Authorization"
                                        , bearer(adminToken + "x")
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void badCredentials_is401() throws Exception {
        mvc.perform(
                        post("/api/auth/login")
                                .header("Authorization"
                                        , bearer(operatorToken + "x"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jm.writeValueAsString
                                        (Map.of("username", "admin"
                                                , "password", "wrong")
                                        )
                                )
                )
                .andExpect(status().isUnauthorized());
    }


    // authZ

    @Test
    void validToken_canRead() throws Exception {
        mvc.perform(
                        get("/api/equipment")
                                .header("Authorization"
                                        , bearer(operatorToken))
                )
                .andExpect(status().isOk());
    }

    @Test
    void operator_cannotDeleteEquipment_403() throws Exception {
        mvc.perform(
                        delete("/api/equipment/{id}", 1)
                                .header("Authorization", bearer(operatorToken))

                )
                .andExpect(status().isForbidden());
    }

    @Test
    void operator_cannotDeleteEmployee_404() throws Exception {
        mvc.perform(
                        delete("/api/employees/{id}", 1)
                                .header("Authorization", bearer(operatorToken))
                )
                .andExpect(status().isForbidden());
    }

    // domain rules

    @Test
    void admin_deleteReferencedEquipment_is409() throws Exception {
        // equipment 2 is assigned -> FK blocks delete
        mvc.perform(
                        delete("/api/equipment/{id}", 2)
                                .header("Authorization", bearer(adminToken))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void duplicateAssign_is409() throws Exception {
        var body = jm.writeValueAsString(Map.of(
                        "employeeId", 1, "equipmentId", 2
                )
        );

        mvc.perform(
                        post("/api/assignments")
                                .header("Authorization", bearer(operatorToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isCreated());

        mvc.perform(
                        post("/api/assignments")
                                .header("Authorization", bearer(operatorToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isConflict());
    }

}
