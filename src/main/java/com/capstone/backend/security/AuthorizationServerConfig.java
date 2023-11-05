package com.capstone.backend.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.stereotype.Component;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {
//    @Bean
//    @Order(1)
//    public SecurityFilterChain webFilterChainForOAuth(HttpSecurity httpSecurity) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
//        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                .oidc(Customizer.withDefaults());
//
//        httpSecurity.exceptionHandling(e -> e.authenticationEntryPoint(
//                new LoginUrlAuthenticationEntryPoint("/api/v1/login")
//        ));
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        var registerClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("public-client-react-app")
//                .clientSecret("secret")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .redirectUri("http://127.0.0.1:8083/login/oauth2/code/public-client-react-app")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                .authorizationGrantTypes(grantType -> {
//                    grantType.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//                    grantType.add(AuthorizationGrantType.REFRESH_TOKEN);
//                    grantType.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
//                })
//                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
//                .build();
//        return new InMemoryRegisteredClientRepository(registerClient);
//    }
//
//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder().build();
//    }
//
//    @Bean
//    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//
//        var keys = keyPairGenerator.generateKeyPair();
//        var publicKey = (RSAPublicKey) keys.getPublic();
//        var privateKey = keys.getPrivate();
//
//        var rsaKey = new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
//
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return new ImmutableJWKSet<>(jwkSet);
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//    }

//    @Bean
//    public SecurityFilterChain authorizationServerSecurityFilterChain(
//            HttpSecurity http
//    ) throws Exception {
//
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//
//        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                .oidc(Customizer.withDefaults());
//
//        http.exceptionHandling(c -> c
//                .defaultAuthenticationEntryPointFor(
//                        new LoginUrlAuthenticationEntryPoint("/login"),
//                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
//                )
//        );
//
//        return http.build();
//    }
//
//    @Bean
//    public RegisteredClientRepository registeredClientRepository() {
//        RegisteredClient client = RegisteredClient.withId("1")
//                .clientId("client")
//                .clientSecret("secret")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .scope(OidcScopes.PROFILE)
//                .scope(OidcScopes.OPENID)
//                .redirectUri("http://localhost:8081/login/oauth2/code/client")
//                .build();
//
//        return new InMemoryRegisteredClientRepository(client);
//    }
//
//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder().build();
//    }
}
