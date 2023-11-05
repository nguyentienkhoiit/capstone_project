package com.capstone.backend.controller;

import com.capstone.backend.model.dto.profle.ProfileDTOUpdate;
import com.capstone.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/profile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Profile", description = "API for Profile")
@CrossOrigin
public class ProfileController {
    UserService userService;

    @GetMapping
    public ResponseEntity<?> viewProfile() {
        return ResponseEntity.ok(userService.viewProfile());
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTOUpdate profileDTOUpdate) {
        return ResponseEntity.ok(userService.updateProfile(profileDTOUpdate));
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> changeAvatar(@RequestParam("avatar") MultipartFile avatar) {
        return ResponseEntity.ok(userService.changeAvatar(avatar));
    }
}
