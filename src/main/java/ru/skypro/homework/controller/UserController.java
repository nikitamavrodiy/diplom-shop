package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.ResponseWrapper;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.service.UserService;

import java.util.Collection;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "UserController")
public class UserController {
    private final UserService userService;

    @Operation(summary = "updateUser", description = "updateUser")
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(new UserDto());
    }

    @Operation(summary = "setPassword", description = "setPassword")
    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> setPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return ResponseEntity.ok(newPasswordDto);
    }

    @Operation(summary = "updateUserImage", description = "updateUserImage")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultipartFile> updateUserImage(@RequestBody MultipartFile image) {
        return ResponseEntity.ok(image);
    }

    @Operation(summary = "getUsers", description = "getUsers")
    @GetMapping("/me")
    public ResponseWrapper<UserDto> getUsers() {
        return userService.getUser();
    }
}
