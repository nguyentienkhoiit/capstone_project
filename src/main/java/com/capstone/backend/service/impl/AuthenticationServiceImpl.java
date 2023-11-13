package com.capstone.backend.service.impl;

import com.capstone.backend.entity.*;
import com.capstone.backend.entity.type.TokenType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.EmailInfo;
import com.capstone.backend.model.dto.authentication.AuthenticationDTORequest;
import com.capstone.backend.model.dto.authentication.AuthenticationDTOResponse;
import com.capstone.backend.model.dto.register.RegisterDTORequest;
import com.capstone.backend.model.dto.register.RegisterDTOResponse;
import com.capstone.backend.model.dto.register.RegisterDTOUpdate;
import com.capstone.backend.model.dto.user.UserEmailDTORequest;
import com.capstone.backend.model.dto.user.UserForgotPasswordDTORequest;
import com.capstone.backend.model.mapper.UserMapper;
import com.capstone.backend.repository.*;
import com.capstone.backend.security.jwt.JwtService;
import com.capstone.backend.service.AuthenticationService;
import com.capstone.backend.service.ConfirmationTokenService;
import com.capstone.backend.utils.EmailHandler;
import com.capstone.backend.utils.EmailHtml;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.capstone.backend.utils.Constants.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    ConfirmationTokenService confirmationTokenService;
    RoleRepository roleRepository;
    EmailHandler emailHandler;
    MessageException messageException;
    ConfirmTokenRepository confirmTokenRepository;
    EmailHtml emailHtml;
    TokenRepository tokenRepository;
    UserRoleRepository userRoleRepository;

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw ApiException.unAuthorizedException(messageException.MSG_USER_UNAUTHORIZED);
        }
    }

    private AuthenticationDTOResponse buildDTOAuthenticationResponse(User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<UserRole> set = userRoleRepository.getUserRoleByActiveAndUser(user);
        set.forEach(s -> authorities.add(new SimpleGrantedAuthority(s.getRole().getName())));
        var accessToken = jwtService.generateToken(user, authorities);
        return AuthenticationDTOResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public AuthenticationDTOResponse login(AuthenticationDTORequest request) throws Exception {
        var user = userRepository.findByUsernameOrEmailActive(request.getEmail())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        authenticate(user.getEmail(), request.getPassword());
        saveUserToken(user);
        return buildDTOAuthenticationResponse(user);
    }

    public UserRole addRoleToUser(User user, Long roleId) {
        Role role = roleRepository.findByIdAndActiveTrue(roleId)
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_ROLE_NOT_FOUND));
        return UserRole.builder()
                .role(role)
                .createdAt(LocalDateTime.now())
                .active(true)
                .user(user)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validTokens = tokenRepository.findAlValidTokenByUser(user.getId());
        if(validTokens.isEmpty()) return;
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    @Override
    public void saveUserToken(User user) {
        revokeAllUserTokens(user);
        var jwtToken = buildDTOAuthenticationResponse(user).getAccessToken();
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .createdAt(LocalDateTime.now())
                .build();
        token = tokenRepository.save(token);
    }

    @Override
    public RegisterDTOResponse register(RegisterDTORequest request) {
        if (userRepository.findByUsernameOrEmailActive(request.getEmail()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_USER_EMAIL_EXISTED);
        }
        if (userRepository.findByUsernameOrEmailActive(request.getUsername()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_USER_USERNAME_EXISTED);
        }
        User user = UserMapper.toUser(request, passwordEncoder);
        user.setUserRoleList(Set.of(addRoleToUser(user, 1L)));
        user = userRepository.save(user);
        return UserMapper.toRegisterDTOResponse(user);
    }

    @Override
    public String updateRegister(RegisterDTOUpdate request) {
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw ApiException.badRequestException(messageException.MSG_USER_PHONE_EXISTED);
        }
        User user = userRepository.findById(request.getId()).orElseThrow();
        user = userRepository.save(UserMapper.toUser(request, user));

        saveUserToken(user);

        //todo: create token send link to gmail
        String token = confirmationTokenService.generateTokenEmail(user);
        String link = HOST + API_VERSION + "/register/confirm?token=" + token;

        //todo: send mail to register
        String body = "Thank you for registering. Please click on the below link to activate your account:";
        String subject = "Confirm your email";
        String content = emailHtml.buildEmail(user.getUsername(), link, subject, body, EMAIL_WAITING_EXPIRATION);
        EmailInfo emailInfo = EmailInfo.builder()
                .to(user.getEmail())
                .content(content)
                .subject(subject)
                .build();
        emailHandler.send(emailInfo);

        return link;
    }

    @Override
    public String forgotPassword(UserEmailDTORequest request) {
        User user = userRepository.findByUsernameOrEmailActive(request.getEmail())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        //todo: create token send link to gmail
        String token = confirmationTokenService.generateTokenEmail(user);
        String link = HOST + API_VERSION + "/auth/confirm?token=" + token;

        String body = "Please click on the below link to change password in your account:";
        String subject = "Confirm your email to change password";
        String content = emailHtml.buildEmail(user.getUsername(), link, subject, body, EMAIL_WAITING_EXPIRATION);
        EmailInfo emailInfo = EmailInfo.builder()
                .to(request.getEmail())
                .content(content)
                .subject(subject)
                .build();
        emailHandler.send(emailInfo);
        return link;
    }

    @Override
    public Boolean changePasswordForgot(UserForgotPasswordDTORequest request) {
        ConfirmationToken confirmationToken = confirmTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw ApiException.internalServerException(messageException.MSG_INTERNAL_SERVER_ERROR);
        }

        User user = userRepository
                .findByUsernameOrEmailActive(confirmationToken.getUser().getEmail())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw ApiException.badRequestException(messageException.MSG_USER_NOT_MATCH_PASSWORD);
        }

        confirmTokenRepository.updateConfirmedAt(request.getToken(), LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getConfirmationPassword()));
        user = userRepository.save(user);
        return true;
    }
}
