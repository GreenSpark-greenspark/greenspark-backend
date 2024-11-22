package GreenSpark.greenspark.OAuth;

import GreenSpark.greenspark.domain.Refresh;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        String username = oauthUser.getUsername();
        String role = oauthUser.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createJwt("access",username, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh",username, role, 1209600000L);
        addRefreshEntity(username,refreshToken,1209600000L);

        response.addCookie(createCookie("access", accessToken));
        response.addCookie(createCookie("refresh", refreshToken));

        response.sendRedirect("http://localhost:8080/swagger-ui/index.html#/");
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }


    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        // 이미 해당 username에 대한 refresh token이 DB에 존재하는지 확인
        Optional<Refresh> existingRefreshToken = refreshRepository.findByUsername(username);

        // 기존 Refresh Token이 있으면 삭제
        existingRefreshToken.ifPresent(refreshRepository::delete);

        // 현재 시간에 expiredMs 밀리초를 더해서 LocalDateTime 생성
        LocalDateTime expirationDate = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));

        // 새 Refresh Token 엔티티 생성
        Refresh refreshEntity = new Refresh();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(expirationDate);

        // 새 Refresh 엔티티 저장
        refreshRepository.save(refreshEntity);
    }
}
