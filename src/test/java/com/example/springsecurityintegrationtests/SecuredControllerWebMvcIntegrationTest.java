package com.example.springsecurityintegrationtests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecuredController.class)
public class SecuredControllerWebMvcIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("return 401 เมื่อ request private service โดยไม่ได้ authentication")
    @Test
    public void givenRequestOnPrivateService_shouldFailWith401() throws Exception {
        mvc.perform(get("/private/hello")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("return 200 เมื่อ request private service โดย authentication แล้ว")
    @WithMockUser(value = "spring")
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/private/hello").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("return 401 เมื่อ request private service สำหรับ ROLE_ADMIN โดยไม่ได้ authentication")
    @Test
    public void givenRequestOnPrivateServiceRoleAdmin_shouldFailWith401() throws Exception {
        mvc.perform(get("/private/role-admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("return 403 เมื่อ request private service สำหรับ role-admin โดย authentication แล้ว แต่ไม่มี ROLE_ADMIN")
    @Test
    @WithMockUser(username = "john")
    public void givenRequestOnPrivateAdminService_shouldFailWith403() throws Exception {
        mvc.perform(get("/private/role-admin").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("return 200 เมื่อ request private service สำหรับ role-admin โดย authentication แล้ว และมี ROLE_ADMIN")
    @Test
    @WithMockUser(username = "john", roles = {"ADMIN"})
    public void givenRequestOnPrivateAdminService_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/private/role-admin").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
