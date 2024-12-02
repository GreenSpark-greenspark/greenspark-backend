package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.domain.Refresh;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.RefreshRepository;
import GreenSpark.greenspark.response.DataResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public DataResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token from cookies
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            // If refresh token is null
            return DataResponseDto.of("refresh token null");
        }

        // Check if refresh token is expired
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // If refresh token is expired
            return DataResponseDto.of("refresh token expired");
        }

        // Verify that the token is a refresh token
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            // If it's not a refresh token
            return DataResponseDto.of("invalid refresh token");
        }

        // Check if the refresh token exists in DB
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            // If the refresh token doesn't exist in DB
            return DataResponseDto.of("invalid refresh token");
        }

        // Retrieve user details
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // Create new access and refresh tokens
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // Store the new refresh token in the database
        addRefreshEntity(username, newRefresh, 86400000L);

        // Set response headers and cookies
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return DataResponseDto.of("토큰 재발급 성공");
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Optional<Refresh> existingRefreshToken = refreshRepository.findByUsername(username);

        existingRefreshToken.ifPresent(refreshRepository::delete);

        LocalDateTime expirationDate = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));

        Refresh refreshEntity = new Refresh();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(expirationDate);

        refreshRepository.save(refreshEntity);
    }
}