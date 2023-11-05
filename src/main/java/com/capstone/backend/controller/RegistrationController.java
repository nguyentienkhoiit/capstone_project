package com.capstone.backend.controller;

import com.capstone.backend.model.dto.register.RegisterDTORequest;
import com.capstone.backend.model.dto.register.RegisterDTOUpdate;
import com.capstone.backend.service.AuthenticationService;
import com.capstone.backend.service.ConfirmationTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/register")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Registration", description = "API for Registration")
@CrossOrigin
public class RegistrationController {
    ConfirmationTokenService confirmationTokenService;
    AuthenticationService authenticationService;

    @PostMapping
    @Operation(summary = "Register a user")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTORequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PutMapping
    @Operation(summary = "Update information register user")
    public ResponseEntity<?> registerInfo(@Valid @RequestBody RegisterDTOUpdate request) {
        return ResponseEntity.ok(authenticationService.updateRegister(request));
    }

    @GetMapping("/confirm")
    @Operation(summary = "Confirm token email to register and active user")
    public String confirm(@RequestParam(value = "token", required = true) String token) {
        return confirmationTokenService.confirmToken(token);
    }
}
