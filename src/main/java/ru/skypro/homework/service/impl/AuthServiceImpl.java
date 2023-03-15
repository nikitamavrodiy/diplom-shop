package ru.skypro.homework.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsManager manager) {
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {
        if (manager.userExists(registerReqDto.getUsername())) {
            return false;
        }
        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReqDto.getPassword())
                        .username(registerReqDto.getUsername())
                        .roles(role.name())
                        .build()
        );
        return true;
    }

    @Override
    public Optional<String> changePassword(String name, String currentPassword, String newPassword) {
        if (!manager.userExists(name)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = manager.loadUserByUsername(name);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);

        return encoder.matches(currentPassword, encryptedPasswordWithoutEncryptionType) ?
                Optional.of(newPassword) : Optional.empty();
    }
}
