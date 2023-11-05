package com.capstone.backend.service;

import com.capstone.backend.entity.User;

public interface ConfirmationTokenService {
    public String generateTokenEmail(User user);

    public String confirmToken(String token);

    public String goToForgotPassword(String token);
}
