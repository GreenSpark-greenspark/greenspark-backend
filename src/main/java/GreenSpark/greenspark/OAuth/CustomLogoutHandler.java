package GreenSpark.greenspark.OAuth;

import GreenSpark.greenspark.domain.Refresh;
import GreenSpark.greenspark.repository.RefreshRepository;
import GreenSpark.greenspark.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLogoutHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        // refresh token 존재 여부 확인
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // refresh token이 만료된 경우
        if (jwtUtil.isExpired(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // refresh token에 해당하는 사용자 정보 추출
        String username = jwtUtil.getUsername(refreshToken);

        // 해당 사용자에 대한 refresh token이 DB에 존재하는지 확인하고 삭제
        Optional<Refresh> existingRefreshToken = refreshRepository.findByUsername(username);
        existingRefreshToken.ifPresent(refreshRepository::delete);

        // 쿠키에서 access와 refresh 토큰 삭제
        removeCookie(response, "access");
        removeCookie(response, "refresh");

        // 로그아웃 완료 후 상태 코드 200
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // 쿠키 삭제 메소드
    private void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); // 쿠키 만료 설정
        cookie.setPath("/"); // 전체 경로에 대해 유효하지 않도록 설정
        cookie.setSecure(true); // 만약 HTTPS를 사용하는 경우, secure 옵션을 추가할 수 있습니다.
        cookie.setHttpOnly(true); // 가능하면 HttpOnly 설정으로, JavaScript에서 접근할 수 없도록 설정
        response.addCookie(cookie);
    }
}