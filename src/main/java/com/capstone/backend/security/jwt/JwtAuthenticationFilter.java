package com.capstone.backend.security.jwt;

import com.capstone.backend.entity.type.MethodType;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.model.CustomError;
import com.capstone.backend.repository.UserRolePermissionRepository;
import com.capstone.backend.utils.Constants;
import com.capstone.backend.utils.MessageException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtService jwtService;
    UserDetailsService userDetailsService;
    UserRolePermissionRepository userRolePermissionRepository;
    MessageException messageException;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //get url from link api
        String url = request.getRequestURI();
        var data = new Object() {
            final String path = getPath(url);
            final MethodType methodType = MethodType.valueOf(request.getMethod().toUpperCase());
        };

        log.info("Path: {}, method: {}", data.path, request.getMethod());

        //check token exist from header
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //permit all api
            if (isPermissionApi(data.path)) {
                filterChain.doFilter(request, response);
            } else responseToClient(response, messageException.MSG_BEARER_NOT_FOUND);
            return;
        }

        //get role from jwt
        String[] roles;
        jwt = authHeader.split(" ")[1].trim();
        try {
            roles = jwtService.getRolesFromToken(jwt);
        } catch (Exception ex) {
            responseToClient(response, messageException.MSG_TOKEN_EXPIRED);
            return;
        }
        //check permission by role from database
        boolean isPermission = Arrays.stream(roles).anyMatch(role -> {
            log.info("role: {}", role);
            return (userRolePermissionRepository.needCheckPermission(data.path, data.methodType, role) != null);
        });
        log.info("permission: {}", isPermission);
        if (!isPermission) {
            responseToClient(response, messageException.MSG_NO_PERMISSION);
            return;
        }
        //save SecurityContextHolder
        username = jwtService.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.stream(roles).forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                responseToClient(response, messageException.MSG_TOKEN_INVALID);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public boolean isPermissionApi(String url) {
        return Arrays.stream(Constants.LIST_PERMIT_ALL)
                .anyMatch(o -> {
                    String urlOriginal = o.replace("*", "");
                    urlOriginal = urlOriginal.endsWith("/") ?
                            urlOriginal.substring(0, urlOriginal.length() - 1) : urlOriginal;
                    return url.contains(urlOriginal);
                });
    }

    public String getPath(String path) {
        int index = path.lastIndexOf("/");
        String uri = path.substring(index);
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            path = path.replace(uri, "");
        }

        //loại bỏ path variable là đuôi file
        for (ResourceType resourceType : ResourceType.values()) {
            String finish = path.substring(index);
            if (finish.contains(resourceType.toString().toLowerCase())) {
                return path.replace(uri, "");
            }
        }
        return path;
    }

    private void responseToClient(HttpServletResponse response, String message) throws IOException {
        CustomError customError = CustomError.builder()
                .code("403")
                .message(message)
                .build();
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        response.getOutputStream().print(mapper.writeValueAsString(customError));
        response.flushBuffer();
    }
}
