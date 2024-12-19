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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
        addCookieWithSameSite(response, "access", newAccess, 600);
        addCookieWithSameSite(response, "refresh", newRefresh, 1209600);

        return DataResponseDto.of("토큰 재발급 성공");
    }

    private void addCookieWithSameSite(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true) // HTTPS에서만 전송
                .sameSite("None") // 크로스 사이트에서 작동 가능
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

//    private void addCookieWithSameSite(HttpServletResponse response, String name, String value, int maxAge) {
//    // Host 헤더에서 localhost 여부 판단
//        boolean isLocalhost = "localhost".equals(response.getHeader("Host"));
//
//        ResponseCookie cookie = ResponseCookie.from(name, value)
//            .maxAge(maxAge) // 쿠키의 유효기간 설정 (초 단위)
//            .httpOnly(true) // 클라이언트 스크립트에서 접근 불가
//            .secure(!isLocalhost) // localhost에서는 secure=false, 배포 환경에서는 true
//            .sameSite(isLocalhost ? "Lax" : "None") // 개발 환경에서는 Lax, 배포 환경에서는 None
//            .domain(isLocalhost ? null : "api.greenspark.shop") // localhost에서는 도메인 제거, 배포 환경에서는 도메인 지정
//            .path("/") // 모든 경로에서 접근 가능
//            .build();
//
//    // Set-Cookie 헤더에 추가
//    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//}

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