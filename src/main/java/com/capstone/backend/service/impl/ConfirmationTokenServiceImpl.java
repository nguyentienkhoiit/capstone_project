package com.capstone.backend.service.impl;

import com.capstone.backend.entity.ConfirmationToken;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.repository.ConfirmTokenRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.ConfirmationTokenService;
import com.capstone.backend.utils.Constants;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    ConfirmTokenRepository confirmTokenRepository;
    UserRepository userRepository;
    MessageException messageException;

    @Override
    public String generateTokenEmail(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken ct = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(Constants.EMAIL_WAITING_EXPIRATION))
                .user(user)
                .build();
        confirmTokenRepository.save(ct);
        return token;
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmTokenRepository.findByToken(token)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw ApiException.badRequestException(messageException.MSG_USER_EMAIL_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw ApiException.badRequestException(messageException.MSG_TOKEN_EXPIRED);
        }

        confirmTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
        userRepository.setActiveUser(confirmationToken.getUser().getEmail());

        return "confirmed";
    }

    @Override
    public String goToForgotPassword(String token) {
        ConfirmationToken confirmationToken = confirmTokenRepository.findByToken(token)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw ApiException.badRequestException(messageException.MSG_USER_EMAIL_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw ApiException.badRequestException(messageException.MSG_TOKEN_EXPIRED);
        }

        return token;
    }
}
