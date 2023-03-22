package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.LoginReqDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.security.MyUserDetails;
import ru.skypro.homework.security.UserDetailsServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthController authController;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private static ObjectMapper mapper = new ObjectMapper();


    @Test
    void contextLoads() {
        Assertions.assertThat(authController).isNotNull();
    }

    private static User getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("password");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setPhone("+70000000000");
        user.setRole(Role.USER);
        return user;
    }

    @BeforeEach
    public void init() {
        User user = getMockUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(getMockUser(), "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(new MyUserDetails(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        User user = getMockUser();
        LoginReqDto loginReq = new LoginReqDto(user.getEmail(), user.getPassword());
        String json = mapper.writeValueAsString(loginReq);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticateUserAdmin() throws Exception {
        User user = getMockUser();
        user.setRole(Role.ADMIN);
        LoginReqDto loginReq = new LoginReqDto(user.getEmail(), user.getPassword());
        String json = mapper.writeValueAsString(loginReq);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticateUserWrongPassword() throws Exception {
        User user = getMockUser();
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        LoginReqDto loginReq = new LoginReqDto(user.getEmail(), user.getPassword());
        String json = mapper.writeValueAsString(loginReq);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(json))
                .andExpect(status().is(403));
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = getMockUser();
        RegisterReqDto registerReq = new RegisterReqDto(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                null);
        String json = mapper.writeValueAsString(registerReq);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
