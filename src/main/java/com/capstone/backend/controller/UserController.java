package com.capstone.backend.controller;

import com.capstone.backend.model.dto.user.UserChangePasswordDTORequest;
import com.capstone.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/users")
@Tag(name = "User", description = "API for User")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class UserController {
    UserService userService;
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePasswordDTORequest request
    ) {
        return ResponseEntity.ok(userService.changePassword(request));
    }
}
